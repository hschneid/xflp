package xf.xflp.opt.construction;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.HighestLowerLeft;
import xf.xflp.opt.construction.strategy.WidthProportionFactor;

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
 * This packer puts the items in a sequence into one container with one container type.
 * Items will only be added to a container.
 *
 * @author hschneid
 *
 */
public class SingleContainerAddPacker extends XFLPBase {

	public static boolean VERBOSE = false;

	private boolean isInit = false;
	private ZSingleBinAddPacker packer;

	public SingleContainerAddPacker() {}

	private void init(XFLPModel model) {
		if(!isInit) {
			isInit = true;
			model.getParameter().
		}
	}

	@Override
	public void execute(XFLPModel model) {
		Container container = new Container(model.getContainerTypes()[0], model.getParameter().getLifoImportance());

		List<Item> unplannedItemList = new ArrayList<>();

		// For all items with respect to given sort order
		Item[] items = model.getItems();
		// Reset eventual presets
		resetItems(items);

		for (Item item : items) {
			Position insertPosition = null;

			// Check if item is allowed to this container type
			if (container.isItemAllowed(item)) {
				// Fetch existing insert positions
				List<Position> posList = container.getPossibleInsertPositionList(item);

				if (posList.size() != 0) {
					// Choose according to select strategy
					insertPosition = strategy.choose(item, container, posList);
				}
			}

			// Add item to container
			if (insertPosition != null) {
				container.add(item, insertPosition);
			} else {
				if (VERBOSE)
					System.out.println("Item " + item.index + " konnte nicht hinzugef�gt werden.");
				unplannedItemList.add(item);
			}
		}

		// Put result into model
		model.setContainers(new Container[]{container});
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
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
