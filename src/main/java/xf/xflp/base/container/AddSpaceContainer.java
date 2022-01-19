package xf.xflp.base.container;

import xf.xflp.base.container.constraints.LoadBearingChecker2;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;
import xf.xflp.base.item.Space;
import xf.xflp.base.space.SpaceService;

import java.util.*;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class AddSpaceContainer extends ContainerBase implements LoadBearingContainer {

	private final Set<String> uniquePositionKeys = new HashSet<>();
	private final Map<Position, List<Space>> spacePositions = new HashMap<>();

	private final SpaceService spaceService = new SpaceService();

	/** Item index -> current bearing capacity **/
	private final Map<Integer, Float> bearingCapacities = new HashMap<>();
	private final LoadBearingChecker2 loadBearingChecker2 = new LoadBearingChecker2();

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
		super(width, length, height, maxWeight, containerType, groundContactRule, lifoImportance);
		init();
	}

	@Override
	public Container newInstance() {
		return new AddSpaceContainer(this);
	}

	public AddSpaceContainer(Container containerPrototype) {
		super(containerPrototype);
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

		updateBearingCapacity(item);

		history.add(item);

		return item.index;
	}

	private void updateBearingCapacity(Item item) {
		loadBearingChecker2.update(this, List.of(item));
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

	/**
	 *
	 */
	private void init() {
		Position start = createPosition(0, 0, 0, PositionType.ROOT);
		activePosList.add(start);
		spacePositions.put(start, Collections.singletonList(Space.of(length, width, height)));
	}

	public List<Space> getSpace(Position pos) {
		return spacePositions.get(pos);
	}

	@Override
	public Map<Integer, Float> getBearingCapacities() {
		return bearingCapacities;
	}
}
