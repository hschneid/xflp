package xf.xflp.opt.construction.multitype;

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
 * Multi-bin variant of the Biased Randomized heuristic.
 * Uses a geometric distribution to pick the next item from the remaining list
 * instead of always taking them in fixed order.
 *
 * @author hschneid
 */
public class MultiBinAddHeuristicBiasRandom {

    private static final double DEFAULT_BETA = 0.3;

    private final BaseStrategy strategy;
    private final StatusManager statusManager;
    private final XFLPParameter parameter;
    private final Random random;
    private final double beta;

    public MultiBinAddHeuristicBiasRandom(Strategy s, StatusManager statusManager, XFLPParameter parameter) {
        this(s, statusManager, parameter, new Random(), DEFAULT_BETA);
    }

    public MultiBinAddHeuristicBiasRandom(Strategy s, StatusManager statusManager, XFLPParameter parameter, Random random, double beta) {
        this.strategy = s.getStrategy();
        this.statusManager = statusManager;
        this.parameter = parameter;
        this.random = random;
        this.beta = beta;
    }

    public List<Item> createLoadingPlan(List<Item> items, List<Container> containers) throws XFLPException {
        List<Item> unplannedItems = new ArrayList<>();

        // Create a mutable copy of the item list so we can remove picked items
        List<Item> remainingItems = new ArrayList<>(items);

        while (!remainingItems.isEmpty()) {
            // Pick an item index using geometric distribution (biased towards front)
            int pickedIndex = nextGeometricIndex(remainingItems.size());
            Item item = remainingItems.remove(pickedIndex);
            PlacedItem placedItem = new PlacedItem(item);

            List<ContainerPosition> containerPositions = getBestContainerPositions(placedItem, containers, strategy);

            // Add item to container
            if (!containerPositions.isEmpty()) {
                if (reachedMaxNbrOfItems(containers, parameter)) {
                    setUnplanned(unplannedItems, item);
                    setUnplanned(unplannedItems, remainingItems.toArray(new Item[0]));
                    break;
                }

                insertIntoContainer(containerPositions);
            } else {
                setUnplanned(unplannedItems, item);
            }
        }

        return unplannedItems;
    }

    /**
     * Returns an index in [0, size) drawn from a geometric-like distribution.
     * Lower indices have higher probability (biased towards the front of the list).
     */
    int nextGeometricIndex(int size) {
        if (size == 1) {
            return 0;
        }
        double u = random.nextDouble();
        if (u == 0.0) {
            u = Double.MIN_VALUE;
        }
        int index = (int) Math.floor(Math.log(u) / Math.log(1.0 - beta));
        return Math.min(index, size - 1);
    }

    private boolean reachedMaxNbrOfItems(List<Container> containers, XFLPParameter parameter) {
        return containers.stream().mapToInt(c -> c.getItems().size()).sum() >= parameter.getMaxNbrOfItems();
    }

    private void setUnplanned(List<Item> unplannedItems, Item... items) {
        for (Item item : items) {
            statusManager.fireMessage(StatusCode.RUNNING, "Item " + item.externalIndex() + " could not be added.");
            unplannedItems.add(item);
        }
    }

    private List<ContainerPosition> getBestContainerPositions(PlacedItem item, List<Container> containers, BaseStrategy strategy) throws XFLPException {
        List<ContainerPosition> containerPositions = new ArrayList<>();
        for (Container container : containers) {
            PositionCandidate bestPosition = getBestInsertPosition(item, container, strategy);
            if (bestPosition != null) {
                containerPositions.add(new ContainerPosition(container, bestPosition));
            }
        }
        return containerPositions;
    }

    private PositionCandidate getBestInsertPosition(PlacedItem item, Container container, BaseStrategy strategy) throws XFLPException {
        if (container.isItemAllowed(item.getItem())) {
            List<PositionCandidate> posList = PositionService.findPositionCandidates(container, item);
            if (!posList.isEmpty()) {
                return strategy.choose(item, container, posList);
            }
        }
        return null;
    }

    private void insertIntoContainer(List<ContainerPosition> containerPositions) {
        ContainerPosition containerPosition = containerPositions.getFirst();
        containerPosition.getContainer().add(
                containerPosition.getPosition().item(),
                containerPosition.getPosition().position(),
                containerPosition.getPosition().isRotated()
        );
    }
}

