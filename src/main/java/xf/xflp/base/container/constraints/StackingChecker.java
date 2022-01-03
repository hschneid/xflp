package xf.xflp.base.container.constraints;

import xf.xflp.base.container.Container;
import xf.xflp.base.container.GroundContactRule;
import xf.xflp.base.container.ParameterType;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2012-2021 Holger Schneider
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
            int itemL,
            int rotation) {
        // New item will be placed at ground. So no stacking needs to be checked.
        if (pos.getZ() == 0)
            return true;

        // Check stacking group - All lower items must have the same stacking group
        // Check ground contact - Items must fulfill certain constraints, if items must be placed on other items
        if(!checkStackingGroupAndGroundContact(container, pos, itemW, itemL, newItem.stackingGroup))
            return false;

        // Check bearing capacity - All lower items can bear the additional weight
        return checkLoadBearingAndItemCount(container, pos, newItem, rotation);
    }

    /**
     * Checks whether the new item is placed on top of remaining items. It is tested
     * that all 4 corners of the new item have at least one current item directly below that item.
     */
    private static boolean checkStackingGroupAndGroundContact(Container container, Position pos, int w, int l, int stackingGroup) {
        List<Integer> zList = container.getBaseData().getZMap().get(pos.getZ());
        if(zList == null || zList.isEmpty())
            return true;

        int itemW = pos.getX() + w;
        int itemL = pos.getY() + l;
        int cornerItem1, cornerItem2, cornerItem3, cornerItem4;
        boolean corner1, corner2, corner3, corner4;
        corner1 = corner2 = corner3 = corner4 = false;
        cornerItem1 = cornerItem2 = cornerItem3 = cornerItem4 = -1;

        // Check for all lower items if stacking group restriction is valid
        for(int i = 0, size = zList.size(); i < size; i++) {
            Item fi = container.getItems().get(zList.get(i));
            if(fi.zh == pos.getZ()) {
                // Is the sgItem below the newItem at position pos
                if(fi.xw > pos.getX() && fi.x < itemW && fi.yl > pos.getY() && fi.y < itemL) {
                    // AND-operation of two binary representations. If no bit fits
                    // then result is zero
                    if((fi.allowedStackingGroups & stackingGroup) == 0) {
                        return false;
                    }
                } else
                    continue;

                if(pos.getX() >= fi.x && pos.getX() <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl) {
                    cornerItem1 = fi.externalIndex;
                    corner1 = true;
                }
                if(itemW > fi.x && itemW <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl) {
                    cornerItem2 = fi.externalIndex;
                    corner2 = true;
                }
                if(pos.getX() >= fi.x && pos.getX() <= fi.xw && itemL > fi.y && itemL <= fi.yl) {
                    cornerItem3 = fi.externalIndex;
                    corner3 = true;
                }
                if(itemW > fi.x && itemW <= fi.xw && itemL > fi.y && itemL <= fi.yl) {
                    cornerItem4 = fi.externalIndex;
                    corner4 = true;
                }
            }
        }

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

    /**
     * This checks the both restrictions load bearing and item count.
     *
     * Hereby the new item is added to container and removed afterwards
     */
    private static boolean checkLoadBearingAndItemCount(Container container, Position pos, Item item, int rotation) {
        // Add to container
        if(rotation == 1)
            item.rotate();
        item.setPosition(pos);
        container.getItems().add(item);
        container.getBaseData().getZGraph().add(item, container.getItems(), container.getBaseData().getZMap());

        boolean isValid = true;

        // Check number of allowed stacked items OR
        // Check load bearing restriction by Z-tree traversal
        if(!checkStackedItemCount(container, item) ||
                !checkLoadBearing(container, item)
        ) {
            isValid = false;
        }

        // Remove from container
        container.getBaseData().getZGraph().remove(item);
        item.clearPosition();
        container.getItems().remove(item.index);
        if(rotation == 1)
            item.rotate();

        return isValid;
    }

    /**
     * Check, if number of below items exceed the number of allowed stacked items.
     * If parameter "number of allowed stacked items" is undefined, it is always valid.
     */
    private static boolean checkStackedItemCount(Container container, Item item) {
        if(item.getNbrOfAllowedStackedItems() == Item.UNDEF_PARAMETER)
            return true;

        List<Item> itemsBelow = container.getBaseData().getZGraph().getItemsBelow(item);
        return itemsBelow.size() <= item.getNbrOfAllowedStackedItems();
    }

    /**
     * Check the load bearing restriction
     *
     * New item will be placed at the position in container, then the bearing check
     * is done by tree traversal through all touched items and then removed again from
     * container.
     *
     */
    private static boolean checkLoadBearing(Container container, Item item) {
        List<Item> ceilItems = container.getBaseData().getZGraph().getCeilItems(item, container.getItems());
        LoadBearingChecker lbc = new LoadBearingChecker();
        return lbc.checkLoadBearing(ceilItems, container.getBaseData().getZGraph());
    }

    private static boolean allEqual(int... values) {
        Arrays.sort(values);
        return values[0] == values[values.length - 1];
    }
}
