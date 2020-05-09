package xf.xflp.opt.construction;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.strategy.HighestLowerLeft;
import xf.xflp.opt.construction.strategy.StrategyIf;

import java.util.ArrayList;
import java.util.List;

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
public class DoubleContainerAddPacker extends XFLPBase {

	public static boolean VERBOSE = false;

	private StrategyIf strategy;

	public DoubleContainerAddPacker() {
		this.strategy = new HighestLowerLeft();
	}

	@Override
	public void execute(XFLPModel model) {
		Container container1 = new Container(model.getContainerTypes()[0], model.getParameter().getLifoImportance());
		Container container2 = new Container(model.getContainerTypes()[1], model.getParameter().getLifoImportance());

		List<Item> unplannedItemList = new ArrayList<>();

		// For all items with respect to given sort order
		Item[] items = model.getItems();
		// Reset eventual presets
		resetItems(items);

		for (Item item : items) {
			Position insertPosition1 = getBestInsertPosition(item, container1);
			Position insertPosition2 = getBestInsertPosition(item, container2);

			// Add item to container
			if (insertPosition1 != null || insertPosition2 != null) {
				insertIntoContainer(
						item, insertPosition1, container1,
						item, insertPosition2, container2
				);
			} else {
				if (VERBOSE)
					System.out.println("Item " + item.index + " konnte nicht hinzugef�gt werden.");
				unplannedItemList.add(item);
			}
		}

		// Put result into model
		model.setContainers(new Container[]{container1, container2});
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
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

	private void insertIntoContainer(Item item, Position insertPosition1, Container container1, Item item1, Position insertPosition2, Container container2) {
		if(insertPosition1 != null) {
			container1.add(item, insertPosition1);
		} else {
			container2.add(item, insertPosition2);
		}
	}

	private void resetItems(Item[] items) {
		for (int i = items.length - 1; i >= 0; i--) {
			items[i].reset();
		}
	}

	public void setStrategy(StrategyIf strategy) {
		this.strategy = strategy;
	}

}
