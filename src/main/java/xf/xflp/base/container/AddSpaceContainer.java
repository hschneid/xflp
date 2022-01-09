package xf.xflp.base.container;

import com.google.common.collect.HashBiMap;
import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;
import xf.xflp.base.item.Space;
import xf.xflp.base.space.SpaceService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class AddSpaceContainer implements Container, ContainerBaseData {

	/* Idx of the container. There are no two containers, with same index. */
	private int index = -1;

	private final int width, height, length;
	private final float maxWeight;
	private final int containerType;
	private float weight = 0;

	private final IndexedArrayList<Item> itemList = new IndexedArrayList<>();

	private final Set<String> uniquePositionKeys = new HashSet<>();
	private final List<Position> activePosList = new ArrayList<>();
	private final Map<Position, List<Space>> spacePositions = new HashMap<>();

	private final LPListMap<Integer, Integer> xMap = new LPListMap<>();
	private final LPListMap<Integer, Integer> yMap = new LPListMap<>();
	private final LPListMap<Integer, Integer> zMap = new LPListMap<>();

	/* Relation graph of upper and lower items */
	private final ZItemGraph zGraph = new ZItemGraph();

	/* Item -> Position */
	private final HashBiMap<Item, Position> itemPositionMap = HashBiMap.create();

	/* History of loaded items - is relevant for creating the solution report */
	private final List<Item> history = new ArrayList<>();

	private int maxPosIdx = 0;
	private ContainerParameter parameter = new DirectContainerParameter();

	private final SpaceService spaceService = new SpaceService();

	/* Is called by reflection */
	public AddSpaceContainer(
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
		return new AddSpaceContainer(this);
	}

	public AddSpaceContainer(Container containerPrototype) {
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
	 * - New positions with spaces
	 * - Remove covered positions
	 */
	@Override
	public int add(Item item, Position pos, boolean isRotated) {
		pos = normPosition(item, pos, isRotated);

		addItem(item, pos);

		removeCoveredPositions(item);

		// Check existing spaces, if new item will shrink them
		checkExistingSpaces(item);

		// Create new insert positions and spaces
		List<Position> newPosList = findInsertPositions(item);
		for (Position newPos : newPosList) {
			if(uniquePositionKeys.contains(newPos.getKey())) {
				continue;
			}

			activePosList.add(newPos);
			uniquePositionKeys.add(newPos.getKey());

			/* Create spaces
			 * Begin with maximal space and check for each item in max-space
			 * if smaller spaces are possible.
			 */
			Space maxSpace = Space.of(
					length - newPos.y,
					width - newPos.x,
					height - newPos.z
			);
			List<Item> spaceItems = spaceService.getItemsInSpace(newPos, maxSpace, itemList);

			Set<Space> spaces = new HashSet<>(Set.of(maxSpace));
			for (Item spaceItem : spaceItems) {

				Set<Space> nextSpaces = new HashSet<>();
				for (Space space : spaces) {
					nextSpaces.addAll(
							spaceService.createSpacesAtPosition(newPos, space, spaceItem)
					);
				}
				spaces = nextSpaces;
			}

			List<Space> newSpaces = spaceService.getDominatingSpaces(spaces);
			if(newSpaces.size() > 0) {
				spacePositions.put(newPos, newSpaces);
			} else {
				removePosition(newPos);
			}
		}

		history.add(item);

		return item.index;
	}

	private void removePosition(Position position) {
		activePosList.remove(position);
		uniquePositionKeys.remove(position.getKey());
		spacePositions.remove(position);
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
	private Position normPosition(Item item, Position pos, boolean isRotated) {
		// Rotate if necessary
		if(isRotated) {
			item.rotate();
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
				// Liegt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
			else if(pos.z == item.z && pos.y >= item.y && pos.y < item.yl && pos.x == item.x)
				coveredPositionList.add(pos);
		}

		for (Position position : coveredPositionList) {
			removePosition(position);
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
		removePosition(pos);
	}

	/**
	 *
	 */
	private List<Position> findInsertPositions(Item item) {
		List<Position> posList = new ArrayList<>();

		// 3 basic positions
		Position verticalPosition = null, horizontalPosition = null;
		if(item.yl < this.length) {
			verticalPosition = createPosition(item.x, item.yl, item.z, PositionType.BASIC);
			posList.add(verticalPosition);
		}
		if(item.xw < this.width) {
			horizontalPosition = createPosition(item.xw, item.y, item.z, PositionType.BASIC);
			posList.add(horizontalPosition);
		}
		if(item.z + item.h < this.height) {
			posList.add(createPosition(item.x, item.y, item.z + item.h, PositionType.BASIC));
		}

		// 2 projected positions
		if(item.z == 0) {
			if(item.x > 0 && verticalPosition != null) {
				Item leftElement = findNextLeftElement(verticalPosition);
				int leftPos = (leftElement != null) ? leftElement.xw : 0;

				if(leftPos < item.x) {
					posList.add(createPosition(leftPos, item.yl, item.z, PositionType.EXTENDED_H));
				}
			}

			if(item.y > 0 && horizontalPosition != null) {
				Item lowerElement = findNextDeeperElement(horizontalPosition);
				int lowerPos = (lowerElement != null) ? lowerElement.yl : 0;

				if(lowerPos < item.y) {
					posList.add(createPosition(item.xw, lowerPos, item.z, PositionType.EXTENDED_V));
				}
			}
		}

		return posList;
	}

	private void checkExistingSpaces(Item newItem) {
		List<Position> removablePositions = new ArrayList<>();
		for (Position position : activePosList) {
			// Is position out of reach for newItem
			if(position.x >= newItem.xw ||
					position.y >= newItem.yl ||
					position.z >= newItem.zh)
				continue;

			Set<Space> newSpaces = new HashSet<>();
			for (Space space : spacePositions.get(position)) {
				newSpaces.addAll(
						spaceService.createSpacesAtPosition(
								position,
								space,
								newItem
						)
				);
			}

			List<Space> spaces = spaceService.getDominatingSpaces(newSpaces);
			if(spaces.size() > 0) {
				spacePositions.put(position, spaces);
			} else {
				removablePositions.add(position);
			}
		}

		for (Position removablePosition : removablePositions) {
			removePosition(removablePosition);
		}
	}

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

	private Item findNextDeeperElement(Position pos) {
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
	private Position createPosition(int x, int y, int z, PositionType type) {
		return Position.of(maxPosIdx++, x, y, z, type);
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

		int newHeight = item.h - minImmersiveDepth;
		return (newHeight <= 0) ? 1 : newHeight;
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
		Position start = createPosition(0, 0, 0, PositionType.ROOT);
		activePosList.add(start);
		spacePositions.put(start, Collections.singletonList(Space.of(length, width, height)));
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

	public List<Space> getSpace(Position pos) {
		return spacePositions.get(pos);
	}
}