package xf.xflp.opt.construction.strategy;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public enum Strategy {

    TOUCHING_PERIMETER(new TouchingPerimeter()),
    HIGH_LOW_LEFT(new HighestLowerLeft()),
    SAME_BASE(new SameBaseStrategy()),
    WIDTH_PROPORTION(new WidthProportionFactor());

    private final BaseStrategy strategy;

    Strategy(BaseStrategy strategy) {
        this.strategy = strategy;
    }

    public BaseStrategy getStrategy() {
        return strategy;
    }
}
