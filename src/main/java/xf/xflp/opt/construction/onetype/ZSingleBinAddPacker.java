package xf.xflp.opt.construction.onetype;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This class presents a function to add the given items to a given single container.
 * All unfitting items will be returned.
 *
 * The used algorithm to add items is a greedy heuristic. It takes the order of given items
 * and places one after another to the best available position in container. The best position
 * is chosen by a strategy.
 *
 * @author hschneid
 *
 */
public class ZSingleBinAddPacker {

	public static boolean VERBOSE = false;

	private final BaseStrategy strategy;

	public ZSingleBinAddPacker(Strategy s) {
		this.strategy = s.getStrategy();
	}

	public List<Item> createLoadingPlan(List<Item> items, Container container) {
		List<Item> unplannedItemList = new ArrayList<>();

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

		return unplannedItemList;
	}

	private void resetItems(List<Item> items) {
		items.forEach(Item::reset);
	}
}
