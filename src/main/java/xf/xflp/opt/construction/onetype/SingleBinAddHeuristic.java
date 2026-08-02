package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.XFLPParameter;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.PlacedItem;
import xf.xflp.base.position.PositionCandidate;
import xf.xflp.base.position.PositionService;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.BaseHeuristic;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012-2026 Holger Schneider
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
public class SingleBinAddHeuristic extends BaseHeuristic implements Heuristic{

	public SingleBinAddHeuristic(XFLPModel model) {
		super(model);
	}

	public List<Item> createLoadingPlan(List<Item> items, Container container) throws XFLPException {
		List<Item> unplannedItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
			PlacedItem placedItem = new PlacedItem(item);
            PositionCandidate insertPosition = null;

            // Check if item is allowed to this container type
            if (container.isItemAllowed(item)) {
                // Fetch existing insert position condidates
                List<PositionCandidate> posList = PositionService.findPositionCandidates(container, placedItem);

                if (!posList.isEmpty()) {
                    // Choose according to select strategy
                    insertPosition = strategy.choose(placedItem, container, posList);
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

	private boolean reachedMaxNbrOfItems(Container container, XFLPParameter parameter) {
		return container.getItems().size() >= parameter.getMaxNbrOfItems();
	}
}
