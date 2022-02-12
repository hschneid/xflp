package xf.xflp.base.container;

import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
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
public class AddSpaceContainer extends ContainerBase {

	private final Set<String> uniquePositionKeys = new HashSet<>();
	private final Map<Position, List<Space>> spacePositions = new HashMap<>();

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
		super(width, length, height, maxWeight, containerType, groundContactRule, lifoImportance);
		init();
	}

	public AddSpaceContainer(Container containerPrototype) {
		super(containerPrototype);
		init();
	}

	private void init() {
		spacePositions.put(activePosList.get(0), Collections.singletonList(Space.of(length, width, height)));
	}

	@Override
	public Container newInstance() {
		return new AddSpaceContainer(this);
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

		// Active position gets inactive by adding item
		removePosition(pos);

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

		updateBearingCapacity(List.of(item));

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

	private void removeCoveredPositions(Item item) {
		for (Position position : findCoveredPositions(item)) {
			removePosition(position);
		}
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

	public List<Space> getSpace(Position pos) {
		return spacePositions.get(pos);
	}
}
