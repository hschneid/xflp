package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPParameter;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.base.monitor.StatusManager;
import xf.xflp.base.position.PositionCandidate;
import xf.xflp.base.position.PositionService;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012-2025 Holger Schneider
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
public class SingleBinAddHeuristic {

	private final BaseStrategy strategy;
	private final StatusManager statusManager;
	private final XFLPParameter parameter;

	public SingleBinAddHeuristic(Strategy s, StatusManager statusManager, XFLPParameter parameter) {
		this.strategy = s.getStrategy();
		this.statusManager = statusManager;
		this.parameter = parameter;
	}

	public List<Item> createLoadingPlan(List<Item> items, Container container) throws XFLPException {
		List<Item> unplannedItems = new ArrayList<>();

		// Reset eventual presets
		resetItems(items);

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            PositionCandidate insertPosition = null;

            // Check if item is allowed to this container type
            if (container.isItemAllowed(item)) {
                // Fetch existing insert positions
                List<PositionCandidate> posList = PositionService.findPositionCandidates(container, item);

                if (!posList.isEmpty()) {
                    // Choose according to select strategy
                    insertPosition = strategy.choose(item, container, posList);
                }
            }

            // Add item to container
            if (insertPosition != null) {
				if (reachedMaxNbrOfItems(container, parameter)) {
					setUnplanned(unplannedItems, items.subList(i, items.size()).toArray(new Item[0]));
					break;
				}

				container.add(insertPosition.item(), insertPosition.position(), insertPosition.isRotated());
            } else {
                setUnplanned(unplannedItems, item);
            }
        }

		return unplannedItems;
	}

	private void resetItems(List<Item> items) {
		for (Item item : items) {
			item.reset();
		}
	}

	private boolean reachedMaxNbrOfItems(Container container, XFLPParameter parameter) {
		return container.getItems().size() >= parameter.getMaxNbrOfItems();
	}

	private void setUnplanned(List<Item> unplannedItems, Item... items) {
		for (Item item : items) {
			statusManager.fireMessage(StatusCode.RUNNING, "Item " + item.index + " could not be added.");
			unplannedItems.add(item);
		}
	}
}
