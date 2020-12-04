package xf.xflp.opt.ga;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.base.problem.RotatedPosition;
import xf.xflp.report.PackageEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Item packer for single container with adding and removing items
 * with given planning parameters for each item (index, orientation, strategy)
 *
 * @author hschneid
 *
 */
public class SingleContainerParameterPacker {

	public float execute(XFLPModel model, ItemParameter[] parameter) {
		Container container = executeInner(model, parameter, new ArrayList<>());

		return container.getLoadedVolume() / container.getMaxVolume();
	}

	public void executeFinally(XFLPModel model, ItemParameter[] parameter) {
		List<Item> unplannedItemList = new ArrayList<>();
		Container container = executeInner(model, parameter, unplannedItemList);

		model.setContainers(new Container[]{container});
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
	}

	private Container executeInner(XFLPModel model, ItemParameter[] parameter, List<Item> unplannedItemList) {
		Container container = new Container(model.getContainerTypes()[0], model.getParameter().getLifoImportance());

		Map<Integer, Item> loadedItemMap = new HashMap<>();

		// For all items with respect to given sort order
		Item[] items = getItems(model, parameter);

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
					posList = filterOrientation(posList, parameter[i].getOrientation());

					if(posList.size() != 0) {
						// Choose according to select strategy
						insertPosition = parameter[i].getStrategy().getStrategy().choose(item, container, posList);
					}
				}

				// Add item to container
				if(insertPosition != null) {
					container.add(item, insertPosition);
					loadedItemMap.put(item.externalIndex, item);
				} else {
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

		return container;
	}

	private List<Position> filterOrientation(List<Position> posList, Orientation orientation) {
		return posList.stream()
				.filter(pos ->
						(orientation == Orientation.UNDEF) ||
								(orientation == Orientation.NORMAL && !(pos instanceof RotatedPosition)) ||
								(orientation == Orientation.ROTATED && pos instanceof RotatedPosition)
				)
				.collect(Collectors.toList());
	}

	private Item[] getItems(XFLPModel model, ItemParameter[] parameter) {
		Item[] items = new Item[model.getItems().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = model.getItems()[parameter[i].getIndex()];
		}

		return items;
	}

	private void resetItems(Item[] items) {
		for (int i = items.length - 1; i >= 0; i--) {
			items[i].reset();
		}
	}

}
