package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPParameter;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.PlacedItem;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.base.monitor.StatusManager;
import xf.xflp.base.position.PositionCandidate;
import xf.xflp.base.position.PositionService;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This class presents a function to add the given items to a given single container
 * using a Biased Randomized heuristic.
 *
 * Instead of always picking the next item in order, a geometric distribution is used
 * to occasionally select a later item in the remaining list. This reduces the
 * sensitivity to initial item ordering while still ensuring that every item is
 * considered exactly once.
 *
 * @author hschneid
 */
public class SingleBinAddHeuristicBiasRandom {

	private static final double DEFAULT_BETA = 0.6;

	private final BaseStrategy strategy;
	private final StatusManager statusManager;
	private final XFLPParameter parameter;
	private final Random random;
	private final double beta;

	public SingleBinAddHeuristicBiasRandom(Strategy s, StatusManager statusManager, XFLPParameter parameter, Random random) {
		this(s, statusManager, parameter, random, DEFAULT_BETA);
	}

	public SingleBinAddHeuristicBiasRandom(Strategy s, StatusManager statusManager, XFLPParameter parameter, Random random, double beta) {
		this.strategy = s.getStrategy();
		this.statusManager = statusManager;
		this.parameter = parameter;
		this.beta = beta;
		this.random = random;
	}

	public List<Item> createLoadingPlan(List<Item> items, Container container) throws XFLPException {
		List<Item> unplannedItems = new ArrayList<>();

		// Reset eventual presets
		resetItems(items);

		// Create a mutable copy of the item list so we can remove picked items
		List<Item> remainingItems = new ArrayList<>(items);
		while (!remainingItems.isEmpty()) {
			// Pick an item index using geometric distribution (biased towards front)
			int pickedIndex = nextGeometricIndex(remainingItems.size());
			Item item = remainingItems.remove(pickedIndex);
			PlacedItem placedItem = new PlacedItem(item);

			PositionCandidate insertPosition = null;

			// Check if item is allowed to this container type
			if (container.isItemAllowed(item)) {
				// Fetch existing insert positions
				List<PositionCandidate> posList = PositionService.findPositionCandidates(container, placedItem);

				if (!posList.isEmpty()) {
					// Choose according to select strategy
					insertPosition = strategy.choose(placedItem, container, posList);
				}
			}

			// Add item to container
			if (insertPosition != null) {
				if (reachedMaxNbrOfItems(container, parameter)) {
					// Put this item and all remaining back as unplanned
					setUnplanned(unplannedItems, item);
					setUnplanned(unplannedItems, remainingItems.toArray(new Item[0]));
					break;
				}

				container.add(insertPosition.item(), insertPosition.position(), insertPosition.isRotated());
			} else {
				setUnplanned(unplannedItems, item);
			}
		}

		return unplannedItems;
	}

	/**
	 * Returns an index in [0, size) drawn from a geometric-like distribution.
	 * Lower indices have higher probability (biased towards the front of the list).
	 *
	 * Uses the inverse transform: index = floor(ln(U) / ln(1 - beta))
	 * clamped to [0, size - 1].
	 *
	 * @param size the number of remaining items
	 * @return a biased random index
	 */
	int nextGeometricIndex(int size) {
		if (size == 1) {
			return 0;
		}
		double u = random.nextDouble(); // uniform in [0, 1)
		// Avoid log(0)
		if (u == 0.0) {
			u = Double.MIN_VALUE;
		}
		int index = (int) Math.floor(Math.log(u) / Math.log(1.0 - beta));
		// Clamp to valid range
		return Math.min(index, size - 1);
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
			statusManager.fireMessage(StatusCode.RUNNING, "Item " + item.externalIndex + " could not be added.");
			unplannedItems.add(item);
		}
	}
}

