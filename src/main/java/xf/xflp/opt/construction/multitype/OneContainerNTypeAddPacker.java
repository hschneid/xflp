package xf.xflp.opt.construction.multitype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.HighestLowerLeft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * This packer puts the items in a sequence into two containers with two different container types.
 * Items will only be added to a container.
 *
 * @author hschneid
 *
 */
public class OneContainerNTypeAddPacker extends XFLPBase {

	public static boolean VERBOSE = false;

	private BaseStrategy strategy;

	public OneContainerNTypeAddPacker() {
		this.strategy = new HighestLowerLeft();
	}

	@Override
	public void execute(XFLPModel model) {
		List<Container> containers = getContainers(model);

		List<Item> unplannedItemList = new ArrayList<>();

		// For all items with respect to given sort order
		Item[] items = model.getItems();
		// Reset eventual presets
		resetItems(items);

		for (Item item : items) {
			List<ContainerPosition> containerPositions = getBestContainerPositions(item, containers);

			// Add item to container
			if (!containerPositions.isEmpty()) {
				insertIntoContainer(item, containerPositions);
			} else {
				if (VERBOSE)
					System.out.println("Item " + item.index + " could not be added.");
				unplannedItemList.add(item);
			}
		}

		// Put result into model
		model.setContainers(containers.toArray(new Container[0]));
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
	}

	private List<ContainerPosition> getBestContainerPositions(Item item, List<Container> containers) {
		return containers.stream()
				.map(con -> {
					Position bestPosition = getBestInsertPosition(item, con);
					if(bestPosition != null) {
						return new ContainerPosition(con, bestPosition);
					} else {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private List<Container> getContainers(XFLPModel model) {
		return Arrays.stream(model.getContainerTypes())
				.map(cT -> new Container(cT, model.getParameter().getLifoImportance()))
				.collect(Collectors.toList());
	}

	private Position getBestInsertPosition(Item item, Container container) {
		// Check if item is allowed to this container type
		if (container.isItemAllowed(item)) {
			// Fetch existing insert positions
			List<Position> posList = container.getPossibleInsertPositionList(item);

			if (posList.size() != 0) {
				// Choose according to select strategy
				return strategy.choose(item, container, posList);
			}
		}

		return null;
	}

	private void insertIntoContainer(Item item, List<ContainerPosition> containerPositions) {
		// Simply take first - Could be improved later
		ContainerPosition containerPosition = containerPositions.get(0);
		containerPosition.getContainer().add(item, containerPosition.getPosition());
	}

	private void resetItems(Item[] items) {
		for (int i = items.length - 1; i >= 0; i--) {
			items[i].reset();
		}
	}

	public void setStrategy(BaseStrategy strategy) {
		this.strategy = strategy;
	}

}
