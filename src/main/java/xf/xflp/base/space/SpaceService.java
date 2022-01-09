package xf.xflp.base.space;

import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.Space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class SpaceService {

    public List<Space> createSpacesAtPosition(Position position, Space space, Item newItem) {
        // Are position and space out of reach for newItem
        if (isItemNotInSpace(position, space, newItem))
            return Collections.singletonList(space);

        // New item is touching this space!

        // New item is over the position
        boolean itemHovering = position.z < newItem.z;
        // New item is in view range (upper right of position)
        boolean widthLimited = position.y >= newItem.y && position.y < newItem.yl;
        boolean lengthLimited = position.x >= newItem.x && position.x < newItem.xw;
        boolean itemOverPosition = widthLimited && lengthLimited;

        List<Space> spaces = new ArrayList<>(3);
        if (itemHovering) {
            spaces.add(Space.of(
                            space.l,
                            space.w,
                            (newItem.z - position.z)
                    ));
            if(!itemOverPosition) {
                spaces.add(Space.of(
                                (lengthLimited) ? (newItem.y - position.y) : space.l,
                                (widthLimited) ? (newItem.x - position.x) : space.w,
                                space.h
                        ));
            }
        }

        if (widthLimited || lengthLimited) {
            if(!itemHovering && !itemOverPosition) {
                spaces.add(Space.of(
                                (lengthLimited) ? (newItem.y - position.y) : space.l,
                                (widthLimited) ? (newItem.x - position.x) : space.w,
                                space.h
                        ));
            }
        }
        // New item is only partially in view range (cutting position coordinates)
        else {
            spaces.add(Space.of(
                            newItem.y - position.y,
                            space.w,
                            space.h
                    ));
            spaces.add(Space.of(
                            space.l,
                            newItem.x - position.x,
                            space.h
                    ));
        }

        return spaces;
    }

    private boolean isItemNotInSpace(Position position, Space space, Item item) {
        return position.x + space.w <= item.x ||
                position.y + space.l <= item.y ||
                position.z + space.h <= item.z ||
                position.x >= item.xw ||
                position.y >= item.yl ||
                position.z >= item.zh;
    }

    public List<Item> getItemsInSpace(Position position, Space space, List<Item> allItems) {
        List<Item> itemsInSpace = new ArrayList<>(allItems);
        for (Item item : allItems) {
            if(isItemNotInSpace(position, space, item)) {
                continue;
            }
            itemsInSpace.add(item);
        }

        return itemsInSpace;
    }

    public List<Space> getDominatingSpaces(Collection<Space> spaces) {
        List<Space> dominatingSpaces = new ArrayList<>(spaces);

        if(spaces.size() == 1) {
            return dominatingSpaces;
        }

        List<Space> dominatedSpaces = new ArrayList<>();
        for (Space spaceA : spaces) {
            for (Space spaceB : spaces) {
                if(spaceA.l == spaceB.l && spaceA.w == spaceB.w && spaceA.h > spaceB.h)
                    dominatedSpaces.add(spaceB);
                if(spaceA.l == spaceB.l && spaceA.h == spaceB.h && spaceA.w > spaceB.w)
                    dominatedSpaces.add(spaceB);
                if(spaceA.h == spaceB.h && spaceA.w == spaceB.w && spaceA.l > spaceB.l)
                    dominatedSpaces.add(spaceB);
            }
        }

        dominatingSpaces.removeAll(dominatedSpaces);

        return dominatingSpaces;
    }
}
