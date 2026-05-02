package xf.xflp.base.container;

import xf.xflp.base.item.PlacedItem;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.Space;

import java.util.*;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public final class AddContainer extends ContainerBase implements Container {

	/* Is called by reflection */
	public AddContainer(
			int width,
			int length,
			int height,
			float maxWeight,
			int containerType,
			ContainerParameter parameter
	) {
		super(width, length, height, maxWeight, containerType, parameter);
		init();
	}

	public AddContainer(Container containerPrototype) {
		super(containerPrototype);
		init();
	}

	private void init() {
		spacePositions.put(activePosList.getFirst(), Collections.singletonList(Space.of(length, width, height)));
	}

	@Override
	public Container newInstance() {
		return new AddContainer(this);
	}

	/**
	 * Adds item to container and update internal data structure
	 * - New positions with spaces
	 * - Remove covered positions
	 */
	@Override
	public int add(PlacedItem item, Position pos, boolean isRotated) {
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

			List<Space> newSpaces = createSpaces(newPos);
			if(!newSpaces.isEmpty()) {
				spacePositions.put(newPos, newSpaces);
			} else {
				removePosition(newPos);
			}
		}

		updateBearingCapacity(List.of(item));

		addToCenterOfGravity(item, pos);

		history.add(item);

		return item.index;
	}

	@Override
	protected void removePosition(Position position) {
		activePosList.remove(position);
		uniquePositionKeys.remove(position.getKey());
		spacePositions.remove(position);
		immersiveDepthCache.remove(position);
	}

	/**
	 * Remove item from container and update internal data structure
	 */
	@Override
	public void remove(PlacedItem item) {
		throw new UnsupportedOperationException("Remove in AddContainer is not supported. Use AddRemoveContainer");
	}

	private void removeCoveredPositions(PlacedItem item) {
		for (Position position : findCoveredPositions(item)) {
			removePosition(position);
		}
	}
}
