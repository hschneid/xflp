package xf.xflp.opt.construction.onetype;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;

import java.util.List;

public interface Heuristic {

    List<Item> createLoadingPlan(List<Item> items, Container container) throws XFLPException;
}
