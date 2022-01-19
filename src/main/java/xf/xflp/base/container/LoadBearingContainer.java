package xf.xflp.base.container;

import java.util.Map;

public interface LoadBearingContainer extends Container {

    /**
     * Returns a mapping of item index -> current bearing capacity of this item
     */
    Map<Integer, Float> getBearingCapacities();
}
