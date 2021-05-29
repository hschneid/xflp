package xf.xflp.base.problem.constraints;

import xf.xflp.base.problem.*;

import java.util.List;
import java.util.Set;

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

    public boolean checkStackingRestrictions(
            Container container,
            Position pos,
            Item newItem,
            int itemW,
            int itemL,
            int rotation) {
        // New item will be placed at ground. So no stacking needs to be checked.
        if (pos.getZ() == 0)
            return true;

        // Check ground contact restriction
        // New item must not hang in the air and maybe cover multiple items
        if(container.getGroundContactRule() != GroundContactRule.FREE &&
                !checkGroundContact(container, pos, newItem))
            return false;

        // Check stacking group and weight bearing capacity
        // All lower items must have the same stacking group
        if(!checkStackingGroupAndBearing(container, pos, itemW, itemL, newItem.stackingGroup))
            return false;

        // Check bearing capacity
        // All lower items can bear the additional weight
        return checkLoadBearingAndItemCount(container, pos, newItem, rotation);
    }

    /**
     * Checks whether there is an item which reaches over two stacks. Stacks are
     * settled by the base item on the floor. no item should overlap the borders
     * of the base item.
     *
     * Use the simple function to get all items, which are below the new item.
     * If number of lower items is bigger than 1 then it is overlapping.
     */
    private boolean checkGroundContact(Container container, Position pos, Item item) {
        item.setPosition(pos);

        Set<Item> foundSet = Tools.getAllFloorItems(item, container.getItemList());

        item.clearPosition();

        return foundSet.size() <= 1;
    }

    /**
     * Checks whether the new item is placed on top of remaining items. It is tested
     * that all 4 corners of the new item have at least one current item directly below that item.
     */
    private boolean checkStackingGroupAndBearing(Container container, Position pos, int w, int l, int stackingGroup) {
        List<Integer> zList = container.getZMap().get(pos.getZ());
        if(zList == null || zList.isEmpty())
            return true;

        int itemW = pos.getX() + w;
        int itemL = pos.getY() + l;
        boolean corner1, corner2, corner3, corner4;
        corner1 = corner2 = corner3 = corner4 = false;

        // Check for all lower items if stacking group restriction is valid
        for(int i = 0, size = zList.size(); i < size; i++) {
            Item fi = container.getItemList().get(zList.get(i));
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

                if(pos.getX() >= fi.x && pos.getX() <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl)
                    corner1 = true;
                if(itemW > fi.x && itemW <= fi.xw && pos.getY() >= fi.y && pos.getY() <= fi.yl)
                    corner2 = true;
                if(pos.getX() >= fi.x && pos.getX() <= fi.xw && itemL > fi.y && itemL <= fi.yl)
                    corner3 = true;
                if(itemW > fi.x && itemW <= fi.xw && itemL > fi.y && itemL <= fi.yl)
                    corner4 = true;
            }
        }

        return corner1 && corner2 && corner3 && corner4;
    }

    /**
     * This checks the both restrictions load bearing and item count.
     *
     * Hereby the new item is added to container and removed afterwards
     */
    private boolean checkLoadBearingAndItemCount(Container container, Position pos, Item item, int rotation) {
        // Add to container
        if(rotation == 1)
            item.rotate();
        item.setPosition(pos);
        container.getItemList().add(item);
        container.getZGraph().add(item, container.getItemList(), container.getZMap());

        // Check number of allowed stacked items
        if(!checkStackedItemCount(container, item))
            return false;

        // Check load bearing restriction by Z-tree traversal
        if(!checkLoadBearing(container, item))
            return false;

        // Remove from container
        container.getZGraph().remove(item);
        item.clearPosition();
        container.getItemList().remove(item.index);
        if(rotation == 1)
            item.rotate();

        return true;
    }

    /**
     * Check if number of placed items exceed the number of allowed stacked items
     */
    private boolean checkStackedItemCount(Container container, Item item) {
        List<Item> itemsBelow = container.getZGraph().getItemsBelow(item);
        for (int i = 0; i < itemsBelow.size(); i++) {
            Item itemBelow = itemsBelow.get(i);
            if (itemBelow.getNbrOfAllowedStackedItems() > 0 &&
                    container.getZGraph().getItemsAbove(itemBelow).size() > itemBelow.getNbrOfAllowedStackedItems()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the load bearing restriction
     *
     * New item will be placed at the position in container, then the bearing check
     * is done by tree traversal through all touched items and then removed again from
     * container.
     *
     */
    private boolean checkLoadBearing(Container container, Item item) {
        List<Item> ceilItems = container.getZGraph().getCeilItems(item, container.getItemList());
        LoadBearingChecker lbc = new LoadBearingChecker();
        return lbc.checkLoadBearing(ceilItems, container.getZGraph());
    }
}
