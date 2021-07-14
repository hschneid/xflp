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
public class AddRemoveContainer implements Container, ContainerBaseData {

	private static final Position rootPos = new Position(-1, -1, -1);
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
	private final Set<Position> inactivePosList = new HashSet<>();
	private final List<Position> coveredPosList = new ArrayList<>();

	/* Relation graph between position - Father position -> [child positions] */
	private final LPListMap<Position, Position> posFollowerMap = new LPListMap<>();
	private final Map<Position, Position> posAncestorMap = new HashMap<>();

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

	/* Is called by reflection */
	public AddRemoveContainer(
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

	@Override
	public Container newInstance() {
		return new AddRemoveContainer(this);
	}

	public AddRemoveContainer(Container containerPrototype) {
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

		// Setze �berlagerte Positionen auf inaktiv
		List<Position> covPosList = findCoveredPositions(item);
		for (Position covPos : covPosList)
			switchActive2Covered(covPos);

		// Erzeuge neue Einf�ge-Punkte und f�ge sie in Tree ein
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			activePosList.add(newPos);
			// Die neue Position ist von der �bergebenen Position aus abh�ngig.
			insertTree(newPos, pos);
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
		// Entferne Objekt
		removeItem(item);

		Position position = itemPositionMap.remove(item);
		if(position == null)
			throw new ArrayIndexOutOfBoundsException("BIG FEHLER: Stack ist keiner Position zugeordnet");

		// Setze Position wieder aktiv
		switchInactive2Active(position);

		// Setze alle �berdeckten Positionen auf aktiv
		List<Position> coveredPosList = findUncoveringPositions(item);
		for (Position pos : coveredPosList)
			switchCovered2Active(pos);

		// Projeziere alle Positionen auf der Oberfl�che des Objektes (Hori und Verti)
		List<Position> projectablePosHList = findProjectableHorizontalPositions(item);
		for (Position pos : projectablePosHList) {
			Item leftItem = findNextLeftElement(pos);
			pos.x = (leftItem != null) ? leftItem.xw : 0;
		}
		List<Position> projectablePosVList = findProjectableVerticalPositions(item);
		for (Position pos : projectablePosVList) {
			Item lowerItem = findNextLowerElement(pos);
			pos.y = (lowerItem != null) ? lowerItem.yl : 0;
		}

		// L�sche die alte Position, wenn deren Elter noch aktiv ist und selbst seine Nachfolger alle weg sind
		// Das muss rekusriv dann f�r dessen Eltern gepr�ft werden.
		// SONST setze Position auf aktiv statt inaktiv
		checkPosition(position);

		history.add(item);
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
	private List<Position> findCoveredPositions(Item item) {
		List<Position> coveredPositionList = new ArrayList<>();

		// Suche alle Einf�gepositionen ab
		for (Position pos : activePosList) {
			// Liegt eine Position auf der unteren Kante des Objekts, ist sie �berdeckt.
			if(pos.z == item.z && pos.x >= item.x && pos.x < item.xw && pos.y == item.y)
				coveredPositionList.add(pos);
				// Leigt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
			else if(pos.z == item.z && pos.y >= item.y && pos.y < item.yl && pos.x == item.x)
				coveredPositionList.add(pos);
		}

		return coveredPositionList;
	}

	private void switchInactive2Active(Position pos) {
		inactivePosList.remove(pos);
		activePosList.add(pos);
	}

	private void switchCovered2Active(Position pos) {
		coveredPosList.remove(pos);
		activePosList.add(pos);
	}

	private void switchActive2Inactive(Position pos) {
		activePosList.remove(pos);
		inactivePosList.add(pos);
	}

	private void switchActive2Covered(Position pos) {
		activePosList.remove(pos);
		coveredPosList.add(pos);
	}

	/**
	 *
	 */
	private List<Position> findProjectableHorizontalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_H)
				if(pos.x == item.xw && pos.y >= item.y && pos.y < item.yl)
					list.add(pos);
		}
		return list;
	}

	/**
	 *
	 */
	private List<Position> findProjectableVerticalPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : activePosList) {
			if(pos.type == EXTENDED_V)
				if(pos.y == item.yl && pos.x >= item.x && pos.x < item.xw)
					list.add(pos);
		}
		return list;
	}

	/**
	 * Search inactive positions, which got uncovered through removal of an item.
	 */
	private List<Position> findUncoveringPositions(Item item) {
		List<Position> list = new ArrayList<>();
		for (Position pos : coveredPosList) {
			if(pos.z == item.z && pos.x == item.x && pos.y >= item.y && pos.y < item.yl)
				list.add(pos);
			else if(pos.z == item.z && pos.x == item.xw && pos.y >= item.y && pos.y < item.yl && pos.type  == EXTENDED_H && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.y && pos.x >= item.x && pos.x < item.xw)
				list.add(pos);
			else if(pos.z == item.z && pos.y == item.yl && pos.x >= item.x && pos.x < item.xw && pos.type == EXTENDED_V && !itemPositionMap.inverse().containsKey(pos))
				list.add(pos);
		}
		return list;
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
	private void insertTree(Position entry, Position ancestor) {
		posAncestorMap.put(entry, ancestor);
		posFollowerMap.put(ancestor, entry);
	}

	/**
	 * Recursive function
	 *
	 * Checks for a certain position, if all following positions are inactive.
	 * If so, then these follower positions can be deleted.
	 */
	private void checkTreeAndRemove2(Position pos) {
		List<Position> mapList = posFollowerMap.get(pos);
		if(mapList != null) {
			// List copy, because iteration on this list to change its contents
			List<Position> followerList = new ArrayList<>(mapList);
			for (Position follower: followerList) {
				if(inactivePosList.contains(follower))
					return;

				// If follower has more followers, deep into and check them
				if(posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0)
					checkTreeAndRemove2(follower);

				// If follower has still followers after deep check, then ignore
				if(posFollowerMap.containsKey(follower) && posFollowerMap.get(follower).size() > 0)
					return;
			}

			// Only if all positions are active and have no more follower
			// all followers can be deleted
			for (Position follower: followerList)
				removePosition(follower);
		}
	}

	/**
	 * Removes a position if it is not used as possible insert position anymore.
	 */
	private void removePosition(Position pos) {
		if(pos.type != ROOT) {
			posFollowerMap.remove(pos);
			posFollowerMap.get(posAncestorMap.get(pos)).remove(pos);
			posAncestorMap.remove(pos);
			activePosList.remove(pos);
			inactivePosList.remove(pos);
			coveredPosList.remove(pos);
			positionItemMap.remove(pos);
		}
	}

	/**
	 *
	 */
	private void checkPosition(Position pos) {
		// Removes active unused follower-positions
		checkTreeAndRemove2(pos);

		Position ancestor = posAncestorMap.get(pos);

		if(
			// Wenn die Position keine Nachfolger mehr hat, weil durch CheckTreeAndRemove gel�scht wurde und
				(!posFollowerMap.containsKey(pos) || posFollowerMap.get(pos).isEmpty())
						// Wenn Vorg�nger (der die Position erzeugt hat) frei ist und
						&& activePosList.contains(ancestor)
						// die Position nicht der Root ist, dann l�sche die Position
						&& pos.type != ROOT) {
			// L�sche pos
			removePosition(pos);

			// Pr�fe den Vorg�nger ebenfalls (rekursiv)
			checkPosition(ancestor);
		}
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
		switchActive2Inactive(pos);
	}

	/**
	 *
	 */
	private void removeItem(Item item) {
		Integer index = item.index;

		// Delete from Z-Graph
		zGraph.remove(item);

		itemList.remove(item.index);

		xMap.get(item.x).remove(index);
		xMap.get(item.xw).remove(index);
		yMap.get(item.y).remove(index);
		yMap.get(item.yl).remove(index);
		zMap.get(item.z).remove(index);
		zMap.get(item.zh).remove(index);

		weight -= item.weight;
		item.h = item.origH;

		item.containerIndex = -1;
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

		// Die root-Position befindet sich nicht im 3D-Raum. Alle
		// realen Positionen erben von dieser virtuellen.
		insertTree(start, rootPos);
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
