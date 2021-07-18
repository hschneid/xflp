package xf.xflp.base.position;

import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.container.AddRemoveContainer;
import xf.xflp.base.container.Container;
import xf.xflp.base.container.ParameterType;
import xf.xflp.base.container.constraints.StackingChecker;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.RotatedPosition;
import xf.xflp.base.item.RotationType;

import java.util.ArrayList;
import java.util.List;

public class PositionService {

    /**
     * Returns all possible and valid insert positions for this item.
     */
    public static List<Position> getPossibleInsertPositionList(Container container, Item item) {
        List<Position> posList = new ArrayList<>();

        int itemW = item.w, itemL = item.l;
        int nbrOfActivePositions = container.getActivePositions().size();

        // Check weight capacity of container
        if(container.getLoadedWeight() + item.weight > container.getMaxWeight()) {
            return posList;
        }

        // For every rotation state
        for (int rotation = 0; rotation <= getRotationType(item).getRotationType(); rotation++) {
            if(rotation > 0) {
                itemW = item.l;
                itemL = item.w;
            }

            // For every active position
            for (int k = nbrOfActivePositions - 1; k >= 0; k--) {
                Position pos = container.getActivePositions().get(k);

                int itemH = retrieveHeight(item, pos, container);

                // Check overlapping with walls
                if((pos.x + itemW) > container.getWidth())
                    continue;
                if((pos.z + itemH) > container.getHeight())
                    continue;
                if((pos.y + itemL) > container.getLength())
                    continue;

                if (checkOverlappingWithItems(container, item, itemW, itemL, pos, itemH)) {
                    continue;
                }

                // Check stacking restrictions
                if(!StackingChecker.checkStackingRestrictions(container, pos, item, itemW, itemL, rotation))
                    continue;

                // Create RotatedPosition if this item is rotated
                posList.add((rotation == 0) ? pos : new RotatedPosition(pos));
            }
        }

        return posList;
    }

    private static boolean checkOverlappingWithItems(Container container, Item item, int itemW, int itemL, Position pos, int itemH) {
        IndexedArrayList<Item> items = (IndexedArrayList<Item>) container.getItems();

        for (int idx = items.length() - 1; idx >= 0; idx--) {
            Item otherItem = items.get(idx);
            if(otherItem == null)
                continue;

            if(otherItem.x < (pos.x + itemW) && otherItem.xw > pos.x &&
                    otherItem.y < (pos.y + itemL) && otherItem.yl > pos.y &&
                    otherItem.z < (pos.z + itemH) && otherItem.zh > pos.z
            ) {
                return true;
            }

            // Pr�fe die LIFO-Eigenschaften
            if (checkLIFO(container, otherItem, pos, item, itemW)) {
                return true;
            }

            // Sonst passt die Position
            // => Ergo mache nix
        }
        return false;
    }

    private static boolean checkLIFO(Container container, Item otherItem, Position pos, Item newItem, int itemW) {
        if(!(container instanceof AddRemoveContainer)) {
            return false;
        }

        float lifoImportance = (Float)container.getParameter().get(ParameterType.LIFO_IMPORTANCE);

        if(lifoImportance == 1) {
            // Liegt das Item weiter entfernt von der Ladekante als die Position
            // Liegt das Item im Entladekorridor zur Ladekante
            if(otherItem.yl <= pos.y && otherItem.x < (pos.x + itemW) && otherItem.xw > pos.x) {
                // Wenn der Entladerang des neuen Items gr��er als der
                // Entladeran des Items ist, dann geht diese Position nicht.
                // Das bestehende Item m�sste fr�her entladen werden, als
                // das verstellende neue Item
                // Das Item muss fr�her entladen werden, als
                // das neue Item, was laut LIFO nicht sein darf.
                return newItem.unLoadingLoc > otherItem.unLoadingLoc;
            }
        }
        return false;
    }

    private static RotationType getRotationType(Item item) {
        return (item.spinable && item.w != item.l) ? RotationType.SPINNABLE : RotationType.FIX;
    }

    /**
     * If it is a stacking position (z > 0), then the immersive depth of lower items
     * must be checked. If this is the case, then the height of given item is reduced.
     */
    private static int retrieveHeight(Item item, Position pos, Container container) {
        if(pos.z == 0) {
            return item.h;
        }

        int minImmersiveDepth = getMinImmersiveDepthOfBelow(pos, item, container);
        int newHeight = item.h - minImmersiveDepth;
        return (newHeight <= 0) ? 1 : newHeight;
    }

    private static int getMinImmersiveDepthOfBelow(Position pos, Item newItem, Container container) {
        LPListMap<Integer, Integer> zMap = container.getBaseData().getZMap();

        if(!zMap.containsKey(pos.z)) {
            return 0;
        }

        int minImmersiveDepthOfBelow = Integer.MAX_VALUE;

        List<Integer> zItems = zMap.get(pos.z);
        for (int i = zItems.size() - 1; i >= 0; i--) {
            Item lowerItem = container.getItems().get(zItems.get(i));
            if(lowerItem.zh == pos.z &&
                    lowerItem.x < pos.x + newItem.w &&
                    lowerItem.xw > pos.x &&
                    lowerItem.y < pos.y + newItem.l &&
                    lowerItem.yl > pos.y) {
                minImmersiveDepthOfBelow = Math.min(minImmersiveDepthOfBelow, lowerItem.getImmersiveDepth());
            }
        }

        return minImmersiveDepthOfBelow;
    }
}
