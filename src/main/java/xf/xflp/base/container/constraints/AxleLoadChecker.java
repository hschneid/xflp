package xf.xflp.base.container.constraints;

import xf.xflp.base.container.Container;
import xf.xflp.base.container.ParameterType;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

/**
 * Copyright (c) 2012-2025 Holger Schneider
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
     *   true, if item is valid at this position. Or if there is no axle load parameter.
     *   false, if item is invalid at this position.
     */
    public static boolean checkPermissibleAxleLoad(Container container, Item item, Position pos) {
        var axleLoadParameter = (AxleLoadParameter) container.getParameter().get(ParameterType.AXLE_LOAD);
        if(axleLoadParameter == null || axleLoadParameter.axleDistance() == 0)
            return true;

        var totalWeight = container.getLoadedWeight() + item.getWeight();

        // Center of truck
        var centerOfTruck = axleLoadParameter.axleDistance() / 2f;
        var centerOfLoad =
                Math.max(
                        container.getItems().stream().mapToDouble(Item::getYl).max().orElse(0),
                        pos.y() + item.l)
                        / 2f;
        var padY = Math.max(0, centerOfTruck - centerOfLoad);

        // Get current center of gravity for y(length), which is the direction of axles.
        var currentCenterOfGravityForY = container.getBaseData().getCenterOfGravityForY();
        var newCenterOfGravityForY = (currentCenterOfGravityForY + ((pos.y() + (item.l / 2f)) * item.getWeight())) / totalWeight;

        // Major formular to calculate the load at one of the 2 axles
        var loadAtSecondAxle = (totalWeight * (newCenterOfGravityForY + padY)) / axleLoadParameter.axleDistance();
        var loadAtFirstAxle = totalWeight - loadAtSecondAxle;

        return (loadAtFirstAxle <= axleLoadParameter.firstPermissibleAxleLoad()) &&
                (loadAtSecondAxle <= axleLoadParameter.secondPermissibleAxleLoad());
    }
}
