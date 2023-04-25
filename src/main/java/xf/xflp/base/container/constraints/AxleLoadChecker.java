package xf.xflp.base.container.constraints;

import xf.xflp.base.container.Container;
import xf.xflp.base.container.ParameterType;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

/**
 * Copyright (c) 2012-2023 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 * Checks, whether the given item at given position would be valid, if the
 * permissible axle load is restricted.
 */
public class AxleLoadChecker {

    /**
     *
     * @return
     *   true, if item is valid at this position.
     *   false, if item is invalid at this position.
     */
    public static boolean checkPermissibleAxleLoad(Container container, Item item, Position pos) {
        var axleLoadParameter = (AxleLoadParameter) container.getParameter().get(ParameterType.AXLE_LOAD);

        // Get current center of gravity for y(length), which is the direction of axles.
        var currentCenterOfGravityForY = container.getBaseData().getCenterOfGravityForY();
        var newCenterOfGravityForY = currentCenterOfGravityForY + ((pos.x() + (item.w / 2f)) * item.getWeight());

        var totalWeight = container.getLoadedWeight() + item.getWeight();
        // Major formular to calculate the load at one of the 2 axles
        var loadAtSecondAxle = (totalWeight * newCenterOfGravityForY) / axleLoadParameter.axleDistance();
        var loadAtFirstAxle = totalWeight - loadAtSecondAxle;

        return (loadAtFirstAxle <= axleLoadParameter.firstPermissibleAxleLoad()) &&
                (loadAtSecondAxle <= axleLoadParameter.secondPermissibleAxleLoad());
    }
}
