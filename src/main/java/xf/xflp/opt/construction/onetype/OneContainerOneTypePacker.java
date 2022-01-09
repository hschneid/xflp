package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.base.position.PositionCandidate;
import xf.xflp.base.position.PositionService;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.Packer;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.report.LoadType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
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
 * @author hschneid
 */
public class OneContainerOneTypePacker implements Packer {

	@Override
	public void execute(XFLPModel model) throws XFLPException {
		Container container = model.getContainerTypes()[0].newInstance();
		BaseStrategy strategy = model.getParameter().getPreferredPackingStrategy().getStrategy();
		
		Map<Integer, Item> loadedItemMap = new HashMap<>();

		List<Item> unplannedItemList = new ArrayList<>();

		// For all items with respect to given sort order
		Item[] items = model.getItems();
		// Reset eventual presets
		resetItems(items);
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];

			if(item.loadingType == LoadType.LOAD) {
				PositionCandidate insertPosition = null;

				// Check if item is allowed to this container type
				if(container.isItemAllowed(item)) {
					// Fetch existing insert positions
					List<PositionCandidate> candidates = PositionService.findPositionCandidates(container, item);

					if(!candidates.isEmpty()) {
						// Choose according to select strategy
						insertPosition = strategy.choose(item, container, candidates);
					}
				}

				// Add item to container
				if(insertPosition != null) {						
					container.add(
							insertPosition.item,
							insertPosition.position,
							insertPosition.isRotated
					);
					loadedItemMap.put(item.externalIndex, item);
				} else {
					model.getStatusManager().fireMessage(StatusCode.RUNNING, "Item " + item.index + " could not be added.");
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

}
