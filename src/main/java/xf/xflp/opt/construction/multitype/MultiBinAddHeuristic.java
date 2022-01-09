package xf.xflp.opt.construction.multitype;

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

public class MultiBinAddHeuristic {

    private final BaseStrategy strategy;
    private final StatusManager statusManager;

    public MultiBinAddHeuristic(Strategy s, StatusManager statusManager) {
        this.strategy = s.getStrategy();
        this.statusManager = statusManager;
    }

    public List<Item> createLoadingPlan(List<Item> items, List<Container> containers) throws XFLPException {
        List<Item> unplannedItems = new ArrayList<>();

        // Reset eventual presets
        resetItems(items);

        for (Item item : items) {
            List<ContainerPosition> containerPositions = getBestContainerPositions(item, containers, strategy);

            // Add item to container
            if (!containerPositions.isEmpty()) {
                insertIntoContainer(containerPositions);
            } else {
                statusManager.fireMessage(StatusCode.RUNNING, "Item " + item.index + " could not be added.");
                unplannedItems.add(item);
            }
        }

        return unplannedItems;
    }

    private List<ContainerPosition> getBestContainerPositions(Item item, List<Container> containers, BaseStrategy strategy) throws XFLPException {
        List<ContainerPosition> containerPositions = new ArrayList<>();
        for (Container container : containers) {
            PositionCandidate bestPosition = getBestInsertPosition(item, container, strategy);
            if(bestPosition != null) {
                containerPositions.add(new ContainerPosition(container, bestPosition));
            }
        }

        return containerPositions;
    }

    private PositionCandidate getBestInsertPosition(Item item, Container container, BaseStrategy strategy) throws XFLPException {
        // Check if item is allowed to this container type
        if (container.isItemAllowed(item)) {
            // Fetch existing insert positions
            List<PositionCandidate> posList = PositionService.findPositionCandidates(container, item);

            if (!posList.isEmpty()) {
                // Choose according to select strategy
                return strategy.choose(item, container, posList);
            }
        }

        return null;
    }

    private void insertIntoContainer(List<ContainerPosition> containerPositions) {
        // Simply take first - Could be improved later
        ContainerPosition containerPosition = containerPositions.get(0);
        containerPosition.getContainer().add(
                containerPosition.getPosition().item,
                containerPosition.getPosition().position,
                containerPosition.getPosition().isRotated
        );
    }

    private void resetItems(List<Item> items) {
        for (Item item : items) {
            item.reset();
        }
    }

}
