package xf.xflp.base.container;

import com.google.common.collect.HashBiMap;
import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.RotatedPosition;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public class AddContainer implements Container, ContainerBaseData {

	private static final int ROOT = 0;
	private static final int BASIC = 1;
	private static final int EXTENDED = 2;
	private static final int EXTENDED_H = 3;
	private static final int EXTENDED_V = 4;

	/* Idx of the container. There are no two containers, with same index. */
	private int index = -1;

	private final int width, height, length;
	private final float maxWeight;
	private final int containerType;
	private float weight = 0;

	private final IndexedArrayList<Item> itemList = new IndexedArrayList<>();
	private final List<Position> activePosList = new ArrayList<>();

	private final LPListMap<Integer, Integer> xMap = new LPListMap<>();
	private final LPListMap<Integer, Integer> yMap = new LPListMap<>();
	private final LPListMap<Integer, Integer> zMap = new LPListMap<>();

	private final Position tmpPosition = new Position(-1, -1);

	/* Relation graph of upper and lower items */
	private final ZItemGraph zGraph = new ZItemGraph();

	/* Position -> Item */
	private final Map<Position, Item> positionItemMap = new HashMap<>();
	/* Item -> Position */
	private final HashBiMap<Item, Position> itemPositionMap = HashBiMap.create();

	/* History of loaded items - is relevant for creating the solution report */
	private final List<Item> history = new ArrayList<>();

	private int maxPosIdx = 0;
	private ContainerParameter parameter = new DirectContainerParameter();

	public AddContainer(
		int width,
		int length,
		int height,
		float maxWeight,
		int containerType,
		GroundContactRule groundContactRule,
		float lifoImportance
	) {
		this.width = width;
		this.length = length;
		this.height = height;
		this.maxWeight = maxWeight;
		this.containerType = containerType;
		parameter.add(ParameterType.GROUND_CONTACT_RULE, groundContactRule);
		parameter.add(ParameterType.LIFO_IMPORTANCE, lifoImportance);

		init();
	}

	public AddContainer(Container containerPrototype) {
		this.width = containerPrototype.getWidth();
		this.length = containerPrototype.getLength();
		this.height = containerPrototype.getHeight();
		this.maxWeight = containerPrototype.getMaxWeight();
		this.containerType = containerPrototype.getContainerType();
		this.parameter = containerPrototype.getParameter();
		init();
	}

	/**
	 * Adds item to container and update internal data structure
	 */
	@Override
	public int add(Item item, Position pos) {
		pos = normPosition(item, pos);

		// F�ge Element in Container ein
		addItem(item, pos);

		if(item.z == 0) {
			// Pr�fe, ob das neue Objekt Projektionslinien schneidet
			List<Position> projHPosList = findHorizontalProjectedPositions(item);
			for (Position projPos : projHPosList) {
				Item s = positionItemMap.get(projPos);
				tmpPosition.setXY(s.x, s.yl);
				Item leftItem = findNextLeftElement(tmpPosition);
				// Projeziere diese Position komplett neu von ihrem Objekt aus
				// Ersetze dabei nur die x-y-Koordinaten
				projPos.x = (leftItem != null) ? leftItem.xw : 0;
			}
			List<Position> projVPosList = findVerticalProjectedPositions(item);
			for (Position projPos : projVPosList) {
				Item s = positionItemMap.get(projPos);
				tmpPosition.setXY(s.xw, s.y);
				Item lowerItem = findNextLowerElement(tmpPosition);
				// Projeziere diese Position komplett neu von ihrem Objekt aus
				// Ersetze dabei nur die x-y-Koordinaten
				projPos.y = (lowerItem != null) ? lowerItem.yl : 0;
			}
		}

		removeCoveredPositions(item);

		// Erzeuge neue Einf�ge-Punkte und f�ge sie in Tree ein
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			activePosList.add(newPos);
			// Diese Position wurde von diesem Item erzeugt.
			positionItemMap.put(newPos, item);
		}

		history.add(item);

		return item.index;
	}

	/**
	 * Remove item from container and update internal data structure
	 */
	@Override
	public void remove(Item item) {
		throw new UnsupportedOperationException("Remove in AddContainer is not supported. Use AddRemoveContainer");
	}

	@Override
	public boolean isItemAllowed(Item item) {
		return
				// If item can be loaded on any container
				(item.allowedContainerSet.size() == 1 && item.allowedContainerSet.contains(ContainerData.DEFAULT_CONTAINER_TYPE))
						// or only on specific ones
						|| item.allowedContainerSet.contains(containerType);
	}

	@Override
	public long getLoadedVolume() {
		long sum = 0;
		for (Item item : this.itemList)
			if(item != null)
				sum += item.volume;

		return sum;
	}

	@Override
	public float getLoadedWeight() {
		float sum = 0;
		for (Item item : this.itemList)
			sum += (item != null) ? item.weight : 0;

		return sum;
	}

	@Override
	public List<Item> getItems() {
		return itemList;
	}

	@Override
	public List<Position> getActivePositions() {
		return activePosList;
	}

	@Override
	public List<Item> getHistory() {
		return history;
	}

	@Override
	public ContainerParameter getParameter() {
		return parameter;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public float getMaxWeight() {
		return maxWeight;
	}

	@Override
	public int getContainerType() {
		return containerType;
	}

	/**
	 * The given position will be normed to an unrotated position.
	 */
	private Position normPosition(Item item, Position pos) {
		// Rotate if necessary
		if(pos instanceof RotatedPosition) {
			RotatedPosition rPos = (RotatedPosition)pos;
			item.rotate();
			pos = rPos.pos;
		}
		return pos;
	}

	/**
	 *
	 */
	private void removeCoveredPositions(Item item) {
		List<Position> coveredPositionList = new ArrayList<>();

		for (Position pos : activePosList) {
			// Liegt eine Position auf der unteren Kante des Objekts, ist sie �berdeckt.
			if(pos.z == item.z && pos.x >= item.x && pos.x < item.xw && pos.y == item.y)
				coveredPositionList.add(pos);
				// Leigt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
			else if(pos.z == item.z && pos.y >= item.y && pos.y < item.yl && pos.x == item.x)
				coveredPositionList.add(pos);
		}

		for (Position position : coveredPositionList) {
			activePosList.remove(position);
		}
	}

	/**
	 *
	 */
	private List<Position> findHorizontalProjectedPositions(Item item) {
		List<Position> posList = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_H) {
				Item s = positionItemMap.get(pos);
				if(pos.z == item.z && pos.x <= item.xw && s.x > item.x && pos.y >= item.y && pos.y <= item.yl)
					posList.add(pos);
			}
		}
		return posList;
	}

	/**
	 *
	 */
	private List<Position> findVerticalProjectedPositions(Item item) {
		List<Position> posList = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_V) {
				Item s = positionItemMap.get(pos);

				if(pos.z == item.z && pos.y <= item.yl && s.y > item.y && pos.x >= item.x && pos.x <= item.xw)
					posList.add(pos);
			}
		}
		return posList;
	}

	/**
	 *
	 */
	private void addItem(Item item, Position pos) {
		// Adjust height for immersive depth
		item.h = retrieveHeight(item, pos);

		item.setPosition(pos);
		itemList.add(item);
		item.containerIndex = this.index;

		itemPositionMap.put(item, pos);

		xMap.put(item.x, item.index);
		xMap.put(item.xw, item.index);
		yMap.put(item.y, item.index);
		yMap.put(item.yl, item.index);
		zMap.put(item.z, item.index);
		zMap.put(item.zh, item.index);

		weight += item.weight;

		// Insert into Z-Graph
		zGraph.add(item, itemList, zMap);

		// Active position gets inactive by adding item
		activePosList.remove(pos);
	}

	/**
	 *
	 */
	private List<Position> findInsertPositions(Item item) {
		List<Position> posList = new ArrayList<>();

		// 2 simple positions
		Position verticalPosition = null, horizontalPosition = null;
		if(item.yl < this.length) {
			verticalPosition = createPosition(item.x, item.yl, item.z, BASIC, false);
			posList.add(verticalPosition);
		}
		if(item.xw < this.width) {
			horizontalPosition = createPosition(item.xw, item.y, item.z, BASIC, false);
			posList.add(horizontalPosition);
		}

		if(item.z == 0) {
			// 2 projected positions
			if(item.x > 0 && verticalPosition != null) {
				Item leftElement = findNextLeftElement(verticalPosition);
				int leftPos = (leftElement != null) ? leftElement.xw : 0;
				posList.add(createPosition(leftPos, item.yl, item.z, EXTENDED_H, false));
			}

			if(item.y > 0 && horizontalPosition != null) {
				Item lowerElement = findNextLowerElement(horizontalPosition);
				int lowerPos = (lowerElement != null) ? lowerElement.yl : 0;
				posList.add(createPosition(item.xw, lowerPos, item.z, EXTENDED_V, false));
			}
		}

		// 1 ceiling position
		if(item.z + item.h < this.height)
			posList.add(createPosition(item.x, item.y, item.z + item.h, BASIC, false));

		return posList;
	}

	/**
	 *
	 */
	private Item findNextLeftElement(Position pos) {
		Item leftItem = null;

		for (Item item : itemList) {
			if(item == null || item.y > pos.y || item.yl < pos.y || item.x > pos.x || item.xw > pos.x || pos.y == item.yl)
				continue;

			if(leftItem == null || item.xw > leftItem.xw)
				leftItem = item;
		}

		return leftItem;
	}

	/**
	 *
	 */
	private Item findNextLowerElement(Position pos) {
		Item lowerItem = null;

		for (Item item : itemList) {
			if(item == null || item.x > pos.x || item.xw < pos.x || item.y > pos.y || item.yl > pos.y || pos.x == item.xw)
				continue;

			if(lowerItem == null || item.yl > lowerItem.yl)
				lowerItem = item;
		}

		return lowerItem;
	}

	/**
	 *
	 */
	private Position createPosition(int x, int y, int z, int type, boolean isProjected) {
		return new Position(maxPosIdx++, x, y, z, type, isProjected);
	}

	/**
	 * If it is a stacking position (z > 0), then the immersive depth of lower items
	 * must be checked. If this is the case, then the height of given item is reduced.
	 */
	private int retrieveHeight(Item item, Position pos) {
		if(pos.z == 0) {
			return item.h;
		}

		List<Item> lowerItems = getItemsBelow(pos, item);
		int minImmersiveDepth = lowerItems.stream().mapToInt(Item::getImmersiveDepth).min().orElse(0);

		return item.h - minImmersiveDepth;
	}

	private List<Item> getItemsBelow(Position pos, Item newItem) {
		if(!zMap.containsKey(pos.z)) {
			return Collections.emptyList();
		}

		return zMap.get(pos.z)
				.stream()
				.map(idx -> itemList.get(idx))
				.filter(lowerItem -> lowerItem.zh == pos.z &&
								lowerItem.x < pos.x + newItem.w &&
								lowerItem.xw > pos.x &&
								lowerItem.y < pos.y + newItem.l &&
								lowerItem.yl > pos.y
						)
				.collect(Collectors.toList());
	}

	/**
	 *
	 */
	private void init() {
		Position start = createPosition(0, 0, 0, ROOT, false);
		activePosList.add(start);
	}

	@Override
	public ContainerBaseData getBaseData() {
		return this;
	}

	@Override
	public LPListMap<Integer, Integer> getXMap() {
		return xMap;
	}

	@Override
	public LPListMap<Integer, Integer> getYMap() {
		return yMap;
	}


	@Override
	public LPListMap<Integer, Integer> getZMap() {
		return zMap;
	}

	@Override
	public ZItemGraph getZGraph() {
		return zGraph;
	}
}
