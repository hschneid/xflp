package xf.xflp.base.space;

import com.google.common.collect.Sets;
import xf.xflp.base.item.ItemPlacement;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.Space;

import java.util.*;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class SpaceService {

    public List<Space> createSpacesAtPosition(Position position, Space space, ItemPlacement newItem) {
        // Are position and space out of reach for newItem
        if (isItemNotInSpace(position, space, newItem))
            return Collections.singletonList(space);

        // New item is touching this space!

        // New item is over the position
        boolean itemHovering = position.z() < newItem.z;
        // New item is in view range (upper right of position)
        boolean widthLimited = position.y() >= newItem.y && position.y() < newItem.yl;
        boolean lengthLimited = position.x() >= newItem.x && position.x() < newItem.xw;
        boolean itemOverPosition = widthLimited && lengthLimited;

        List<Space> spaces = new ArrayList<>(3);
        if (itemHovering) {
            spaces.add(Space.of(
                    space.l(),
                    space.w(),
                    (newItem.z - position.z())
            ));
            if(!itemOverPosition) {
                spaces.add(Space.of(
                        (lengthLimited) ? (newItem.y - position.y()) : space.l(),
                        (widthLimited) ? (newItem.x - position.x()) : space.w(),
                        space.h()
                ));
            }
        }

        if (widthLimited || lengthLimited) {
            if(!itemHovering && !itemOverPosition) {
                spaces.add(Space.of(
                        (lengthLimited) ? (newItem.y - position.y()) : space.l(),
                        (widthLimited) ? (newItem.x - position.x()) : space.w(),
                        space.h()
                ));
            }
        }
        // New item is only partially in view range (cutting position coordinates)
        else {
            spaces.add(Space.of(
                    newItem.y - position.y(),
                    space.w(),
                    space.h()
            ));
            spaces.add(Space.of(
                    space.l(),
                    newItem.x - position.x(),
                    space.h()
            ));
        }

        return spaces;
    }

    public boolean isItemNotInSpace(Position position, Space space, ItemPlacement item) {
        return position.x() + space.w() <= item.x ||
                position.y() + space.l() <= item.y ||
                position.z() + space.h() <= item.z ||
                position.x() >= item.xw ||
                position.y() >= item.yl ||
                position.z() >= item.zh;
    }

    public Set<ItemPlacement> getItemsInSpace(Position position, Space space, List<ItemPlacement> allItems) {
        Set<ItemPlacement> itemsInSpace = Sets.newHashSetWithExpectedSize(allItems.size());
        for (ItemPlacement item : allItems) {
            // entries can be null, because they can be removed from item list
            if(item == null || isItemNotInSpace(position, space, item)) {
                continue;
            }
            itemsInSpace.add(item);
        }

        return itemsInSpace;
    }

    public List<Space> getDominatingSpaces(Collection<Space> spaces) {
        if(spaces.size() <= 1) {
            return new ArrayList<>(spaces);
        }

        // Convert to array for indexed access (avoids iterator overhead in nested loop)
        Space[] arr = spaces.toArray(new Space[0]);
        int n = arr.length;
        boolean[] dominated = new boolean[n];

        for (int a = 0; a < n; a++) {
            if (dominated[a]) continue;
            Space sa = arr[a];
            for (int b = a + 1; b < n; b++) {
                if (dominated[b]) continue;
                Space sb = arr[b];
                // A dominates B: 2 dimensions equal, 1 strictly greater
                if (sa.l() == sb.l() && sa.w() == sb.w() && sa.h() > sb.h()) dominated[b] = true;
                else if (sa.l() == sb.l() && sa.h() == sb.h() && sa.w() > sb.w()) dominated[b] = true;
                else if (sa.h() == sb.h() && sa.w() == sb.w() && sa.l() > sb.l()) dominated[b] = true;
                // B dominates A: 2 dimensions equal, 1 strictly greater
                else if (sb.l() == sa.l() && sb.w() == sa.w() && sb.h() > sa.h()) { dominated[a] = true; break; }
                else if (sb.l() == sa.l() && sb.h() == sa.h() && sb.w() > sa.w()) { dominated[a] = true; break; }
                else if (sb.h() == sa.h() && sb.w() == sa.w() && sb.l() > sa.l()) { dominated[a] = true; break; }
            }
        }

        List<Space> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!dominated[i]) {
                result.add(arr[i]);
            }
        }
        return result;
    }

}
