package xf.xflp.opt.construction;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.strategy.HighestLowerLeft;
import xf.xflp.opt.construction.strategy.StrategyIf;
import xf.xflp.report.PackageEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Item packer for single container with adding and removing items
 * 
 * This packer puts the items in a sequence into one single container.
 * It is able to add and to remove the items with respect to their loading type (LOAD, UNLOAD).
 * There is no optimization in container allocation or item sequence.
 * 
 * @author hschneid
 *
 */
public class SingleContainerPacker extends XFLPBase {

	public static boolean VERBOSE = false;

	private StrategyIf strategy;

	public SingleContainerPacker() {
		this.strategy = new HighestLowerLeft();
	}

	@Override
	public void execute(XFLPModel model) {
		Container container = new Container(model.getContainerTypes()[0], model.getParameter().getLifoImportance());
		
		Map<Integer, Item> loadedItemMap = new HashMap<>();

		List<Item> unplannedItemList = new ArrayList<>();

		// For all items with respect to given sort order
		Item[] items = model.getItems();
		// Reset eventual presets
		resetItems(items);
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];

			if(item.loadingType == PackageEventType.LOAD) {
				Position insertPosition = null;

				// Check if item is allowed to this container type
				if(container.isItemAllowed(item)) {
					// Fetch existing insert positions
					List<Position> posList = container.getPossibleInsertPositionList(item);

					if(posList.size() != 0) {
						// Choose according to select strategy
						insertPosition = strategy.choose(item, container, posList);
					}
				}

				// Add item to container
				if(insertPosition != null) {						
					container.add(item, insertPosition);
					loadedItemMap.put(item.externalIndex, item);
				} else {
					if(VERBOSE)
						System.out.println("Item "+item.index +" konnte nicht hinzugef�gt werden.");
					unplannedItemList.add(item);
				}
			} else {
				// Remove item from container
				// It is not checked if item was really loaded to container.
				// Before removing the unloading item must be replaced by the loaded item object
				// for index problems
				if(loadedItemMap.containsKey(item.externalIndex))
					container.remove(loadedItemMap.get(item.externalIndex));
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

	public void setStrategy(StrategyIf strategy) {
		this.strategy = strategy;
	}

}
