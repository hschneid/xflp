package xf.xflp.base.container.constraints;

import xf.xflp.base.container.Container;
import xf.xflp.base.container.GroundContactRule;
import xf.xflp.base.container.LoadBearingContainer;
import xf.xflp.base.container.ParameterType;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class StackingChecker {

    public static boolean checkStackingRestrictions(
            Container container,
            Position pos,
            Item newItem,
            int itemW,
            int itemL) {
        // New item will be placed at ground. So no stacking needs to be checked.
        if (pos.getZ() == 0)
            return true;

        // Check stacking group - All lower items must have the same stacking group
        // Check ground contact - Items must fulfill certain constraints, if items must be placed on other items
        if(!checkStackingGroupAndGroundContact(container, newItem, pos, itemW, itemL, newItem.stackingGroup))
            return false;

        return !checkInvalidLoadBearing(container, pos, newItem, itemW, itemL);
    }

    /**
     * Checks whether the new item is placed on top of remaining items. It is tested
     * that all 4 corners of the new item have at least one current item directly below that item.
     */
    private static boolean checkStackingGroupAndGroundContact(Container container, Item item, Position pos, int itemW, int itemL, int stackingGroup) {
        List<Integer> zList = container.getBaseData().getZMap().get(pos.getZ());
        if(zList == null || zList.isEmpty())
            return true;

        int posXW = pos.getX() + itemW;
        int posYL = pos.getY() + itemL;

        int nbrOfItemsBelow = 0;
        int cornerItem1, cornerItem2, cornerItem3, cornerItem4;
        boolean corner1, corner2, corner3, corner4;
        corner1 = corner2 = corner3 = corner4 = false;
        cornerItem1 = cornerItem2 = cornerItem3 = cornerItem4 = -1;

        // Check for all lower items if stacking group restriction is valid
        for(int i = zList.size() - 1; i >= 0; i--) {
            Item fi = container.getItems().get(zList.get(i));
            if(isNotBelow(pos, itemW, itemL, fi)) {
                continue;
            }

            nbrOfItemsBelow++;

            // AND-operation of two binary representations. If no bit fits
            // then result is zero
            if((fi.allowedStackingGroups & stackingGroup) == 0) {
                return false;
            }

            if(pos.getX() >= fi.x && pos.getX() <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl) {
                cornerItem1 = fi.externalIndex;
                corner1 = true;
            }
            if(posXW > fi.x && posXW <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl) {
                cornerItem2 = fi.externalIndex;
                corner2 = true;
            }
            if(pos.getX() >= fi.x && pos.getX() <= fi.xw && posYL > fi.y && posYL <= fi.yl) {
                cornerItem3 = fi.externalIndex;
                corner3 = true;
            }
            if(posXW > fi.x && posXW <= fi.xw && posYL > fi.y && posYL <= fi.yl) {
                cornerItem4 = fi.externalIndex;
                corner4 = true;
            }
        }

        /*
         * Check, if number of below items exceed the number of allowed stacked items.
         * If parameter "number of allowed stacked items" is undefined, it is always valid.
         */
        if(nbrOfItemsBelow > item.getNbrOfAllowedStackedItems())
            return false;

        boolean hasAnyGroundContact = (corner1 || corner2 || corner3 || corner4);
        boolean hasFullGroundContact = (corner1 && corner2 && corner3 && corner4);
        if(container.getParameter().get(ParameterType.GROUND_CONTACT_RULE) == GroundContactRule.SINGLE) {
            return allEqual(cornerItem1, cornerItem2, cornerItem3, cornerItem4) && hasFullGroundContact;
        }
        if(container.getParameter().get(ParameterType.GROUND_CONTACT_RULE) == GroundContactRule.FREE) {
            return hasAnyGroundContact;
        }
        return hasFullGroundContact;
    }

    private static boolean allEqual(int... values) {
        Arrays.sort(values);
        return values[0] == values[values.length - 1];
    }

    private static boolean checkInvalidLoadBearing(Container container, Position position, Item newItem, int itemW, int itemL) {
        Map<Integer, Float> bearingCapacities = ((LoadBearingContainer)container).getBearingCapacities();
        List<Integer> sameZItems = container.getBaseData().getZMap().get(position.z);
        for (int i = sameZItems.size() - 1; i >= 0; i--) {
            int lowerItemIdx = sameZItems.get(i);
            // Is item below?
            Item lowerItem = container.getItems().get(lowerItemIdx);
            if(isNotBelow(position, itemW, itemL, lowerItem)) {
                continue;
            }

            // Is bearing capacity enough?
            float bearingCapacity = bearingCapacities.get(lowerItemIdx);
            float areaRatio = Tools.getCutRatio(position.x, position.y, itemW, itemL, lowerItem);
            if(bearingCapacity - (newItem.getWeight() * areaRatio) < 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean isNotBelow(Position position, int itemW, int itemL, Item lowerItem) {
        return lowerItem.zh != position.z ||
                lowerItem.xw <= position.x ||
                lowerItem.yl <= position.y ||
                lowerItem.x >= position.x + itemW ||
                lowerItem.y >= position.y + itemL;
    }
}
