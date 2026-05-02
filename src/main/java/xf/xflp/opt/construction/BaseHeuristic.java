package xf.xflp.opt.construction;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.XFLPParameter;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.base.monitor.StatusManager;
import xf.xflp.opt.construction.strategy.BaseStrategy;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.List;

public abstract class BaseHeuristic {

    protected final BaseStrategy strategy;
    protected final StatusManager statusManager;
    protected final XFLPParameter parameter;

    protected BaseHeuristic(XFLPModel model) {
        this.strategy = model.getParameter().getPreferredPackingStrategy().getStrategy();
        this.statusManager = model.getStatusManager();
        this.parameter = model.getParameter();
    }

    public BaseHeuristic(Strategy s, StatusManager statusManager, XFLPParameter parameter) {
        this.strategy = s.getStrategy();
        this.statusManager = statusManager;
        this.parameter = parameter;
    }

    protected void setUnplanned(List<Item> unplannedItems, Item... items) {
        for (Item item : items) {
            statusManager.fireMessage(StatusCode.RUNNING, "Item " + item.externalIndex() + " could not be added.");
            unplannedItems.add(item);
        }
    }
}
