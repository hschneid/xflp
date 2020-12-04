package xf.xflp.opt.ga;

import xf.xflp.opt.construction.strategy.HighestLowerLeft;
import xf.xflp.opt.construction.strategy.StrategyIf;
import xf.xflp.opt.construction.strategy.TouchingPerimeter;

public enum Strategy {

    TOUCHING_PERIMETER(new TouchingPerimeter()),
    HIGH_LOW_LEFT(new HighestLowerLeft());

    private final StrategyIf strategy;

    Strategy(StrategyIf strategy) {
        this.strategy = strategy;
    }

    public StrategyIf getStrategy() {
        return strategy;
    }
}
