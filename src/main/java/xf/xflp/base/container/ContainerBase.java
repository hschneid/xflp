package xf.xflp.base.container;

import com.google.common.collect.HashBiMap;
import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.container.constraints.LoadBearingChecker;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;

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
public abstract sealed class ContainerBase implements Container, ContainerBaseData permits AddContainer, AddRemoveContainer {

    /* Idx of the container. There are no two containers, with same index. */
    protected int index = -1;

    protected final int width, height, length;
    protected final float maxWeight;
    protected final int containerType;
    protected float weight = 0;

    protected final IndexedArrayList<Item> itemList = new IndexedArrayList<>();

    protected final List<Position> activePosList = new ArrayList<>();

    protected final LPListMap<Integer, Integer> xMap = new LPListMap<>();
    protected final LPListMap<Integer, Integer> yMap = new LPListMap<>();
    protected final LPListMap<Integer, Integer> zMap = new LPListMap<>();

    /* Relation graph of upper and lower items */
    protected final ZItemGraph zGraph = new ZItemGraph();

    /* Item -> Position */
    protected final HashBiMap<Item, Position> itemPositionMap = HashBiMap.create();

    /* History of loaded items - is relevant for creating the solution report */
    protected final List<Item> history = new ArrayList<>();

    /** Item index - current bearing capacity **/
    protected final Map<Integer, Float> bearingCapacities = new HashMap<>();
    protected final LoadBearingChecker loadBearingChecker = new LoadBearingChecker();

    /** Position -> precomputed minimum immersive depth of items below this position */
    protected final Map<Position, Integer> immersiveDepthCache = new HashMap<>();

    protected int maxPosIdx = 0;
    protected final ContainerParameter parameter;
    protected float centerOfGravityForY = 0;
    protected int maxYl = 0;

    protected ContainerBase(
            int width,
            int length,
            int height,
            float maxWeight,
            int containerType,
            ContainerParameter parameter
    ) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.maxWeight = maxWeight;
        this.containerType = containerType;
        this.parameter = parameter;

        init();
    }

    public ContainerBase(Container containerPrototype) {
        this.width = containerPrototype.getWidth();
        this.length = containerPrototype.getLength();
        this.height = containerPrototype.getHeight();
        this.maxWeight = containerPrototype.getMaxWeight();
        this.containerType = containerPrototype.getContainerType();
        this.parameter = containerPrototype.getParameter();

        init();
    }

    private void init() {
        Position start = createPosition(0, 0, 0, PositionType.ROOT);
        activePosList.add(start);
    }

    public boolean isItemAllowed(Item item) {
        return
                // If item can be loaded on any container
                (item.allowedContainerSet.size() == 1 && item.allowedContainerSet.contains(ContainerData.DEFAULT_CONTAINER_TYPE))
                        // or only on specific ones
                        || item.allowedContainerSet.contains(containerType);
    }

    public long getLoadedVolume() {
        long sum = 0;
        for (Item item : this.itemList)
            if(item != null)
                sum += item.volume;

        return sum;
    }

    public float getLoadedWeight() {
        return weight;
    }

    protected void addItem(Item item, Position pos) {
        // Adjust height for immersive depth
        item.h = retrieveHeight(item, pos);

        item.setPosition(pos);
        itemList.add(item);
        item.containerIndex = this.index;

        itemPositionMap.put(item, pos);

        xMap.put(item.x(), item.index);
        xMap.put(item.xw, item.index);
        yMap.put(item.y(), item.index);
        yMap.put(item.yl, item.index);
        zMap.put(item.z(), item.index);
        zMap.put(item.zh, item.index);

        weight += item.weight;

        // Insert into Z-Graph
        zGraph.add(item, itemList, zMap);

        // Update immersive depth cache for positions that sit on top of the new item
        updateImmersiveDepthCacheForItem(item);
    }

    /**
     * Updates the immersive depth cache for all active positions that are affected
     * by the given item. A position is affected if its z equals item.zh (position sits
     * on the item's top face) and the position point (x, y) lies within the item's footprint.
     */
    protected void updateImmersiveDepthCacheForItem(Item item) {
        maxYl = Math.max(maxYl, item.yl);
        int itemZh = item.zh;
        int itemDepth = item.immersiveDepth;

        for (Position activePos : activePosList) {
            if (activePos.z() == itemZh &&
                    activePos.x() >= item.x() && activePos.x() < item.xw &&
                    activePos.y() >= item.y() && activePos.y() < item.yl) {
                var cachedImmersiveDepth = immersiveDepthCache.get(activePos);
                if (cachedImmersiveDepth == null || itemDepth < cachedImmersiveDepth) {
                    immersiveDepthCache.put(activePos, itemDepth);
                }
            }
        }
    }

    protected List<Position> findInsertPositions(Item item) {
        List<Position> posList = new ArrayList<>();

        // 3 basic positions
        Position verticalPosition = null, horizontalPosition = null;
        if(item.yl < this.length) {
            verticalPosition = createPosition(item.x(), item.yl, item.z(), PositionType.BASIC);
            posList.add(verticalPosition);
        }
        if(item.xw < this.width) {
            horizontalPosition = createPosition(item.xw, item.y(), item.z(), PositionType.BASIC);
            posList.add(horizontalPosition);
        }
        if(item.z() + item.h < this.height) {
            posList.add(createPosition(item.x(), item.y(), item.z() + item.h, PositionType.BASIC));
        }

        // 2 projected positions
        if(item.z() == 0) {
            if(item.x() > 0 && verticalPosition != null) {
                Item leftElement = findNextLeftElement(verticalPosition);
                int leftPos = (leftElement != null) ? leftElement.xw : 0;

                if(leftPos < item.x()) {
                    posList.add(createPosition(leftPos, item.yl, item.z(), PositionType.EXTENDED_H));
                }
            }

            if(item.y() > 0 && horizontalPosition != null) {
                Item lowerElement = findNextDeeperElement(horizontalPosition);
                int lowerPos = (lowerElement != null) ? lowerElement.yl : 0;

                if(lowerPos < item.y()) {
                    posList.add(createPosition(item.xw, lowerPos, item.z(), PositionType.EXTENDED_V));
                }
            }
        }

        return posList;
    }

    /**
     * The given position will be normed to an unrotated position.
     */
    protected Position normPosition(Item item, Position pos, boolean isRotated) {
        // Rotate if necessary
        if(isRotated) {
            item.rotate();
        }
        return pos;
    }

    protected Item findNextLeftElement(Position pos) {
        Item leftItem = null;

        for (Item item : itemList) {
            if(item == null || item.y() > pos.y() || item.yl < pos.y() || item.x() > pos.x() || item.xw > pos.x() || pos.y() == item.yl)
                continue;

            if(leftItem == null || item.xw > leftItem.xw)
                leftItem = item;
        }

        return leftItem;
    }

    protected Item findNextDeeperElement(Position pos) {
        Item lowerItem = null;

        for (Item item : itemList) {
            if(item == null || item.x() > pos.x() || item.xw < pos.x() || item.y() > pos.y() || item.yl > pos.y() || pos.x() == item.xw)
                continue;

            if(lowerItem == null || item.yl > lowerItem.yl)
                lowerItem = item;
        }

        return lowerItem;
    }

    protected List<Position> findCoveredPositions(Item item) {
        List<Position> coveredPositionList = new ArrayList<>();

        for (Position pos : activePosList) {
            // Liegt eine Position auf der unteren Kante des Objekts, ist sie �berdeckt.
            if(pos.z() == item.z() && pos.x() >= item.x() && pos.x() < item.xw && pos.y() == item.y())
                coveredPositionList.add(pos);
                // Liegt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
            else if(pos.z() == item.z() && pos.y() >= item.y() && pos.y() < item.yl && pos.x() == item.x())
                coveredPositionList.add(pos);
        }

        return coveredPositionList;
    }

    protected Position createPosition(int x, int y, int z, PositionType type) {
        Position pos = Position.of(maxPosIdx++, x, y, z, type);
        immersiveDepthCache.put(pos, computeMinImmersiveDepthAtPosition(x, y, z));
        return pos;
    }

    protected Position createPosition(int idx, int x, int y, int z, PositionType type) {
        Position pos = Position.of(idx, x, y, z, type);
        immersiveDepthCache.put(pos, computeMinImmersiveDepthAtPosition(x, y, z));
        return pos;
    }

    /**
     * Computes the minimum immersive depth of all items whose top face (zh) is at the given z,
     * and whose footprint contains the point (x, y).
     * This is a precomputation for the position, so that the PositionService can use it as a
     * fast lookup instead of iterating over the zMap each time.
     */
    protected int computeMinImmersiveDepthAtPosition(int x, int y, int z) {
        if (z == 0) {
            return 0;
        }

        List<Integer> zItems = zMap.get(z);
        if (zItems == null) {
            return 0;
        }

        int minDepth = Integer.MAX_VALUE;
        for (int i = zItems.size() - 1; i >= 0; i--) {
            Item lowerItem = itemList.get(zItems.get(i));
            if (lowerItem.zh == z &&
                    lowerItem.x() <= x &&
                    lowerItem.xw > x &&
                    lowerItem.y() <= y &&
                    lowerItem.yl > y) {
                int depth = lowerItem.immersiveDepth;
                if (depth == 0) {
                    return 0;
                }
                if (depth < minDepth) {
                    minDepth = depth;
                }
            }
        }

        return (minDepth == Integer.MAX_VALUE) ? 0 : minDepth;
    }

    protected void updateBearingCapacity(List<Item> items) {
        loadBearingChecker.update(this, items);
    }

    /**
     * If it is a stacking position, then the immersive depth of lower items
     * must be checked. If this is the case, then the height of given item is reduced.
     */
    protected int retrieveHeight(Item item, Position pos) {
        if(pos.z() == 0) {
            return item.h;
        }

        List<Item> lowerItems = findItemsBelow(this, pos, item);
        if(lowerItems.isEmpty())
            return item.h;

        int minImmersiveDepth = Integer.MAX_VALUE;
        for (int i = lowerItems.size() - 1; i >= 0; i--) {
            minImmersiveDepth = Math.min(minImmersiveDepth, lowerItems.get(i).getImmersiveDepth());
        }

        int newHeight = item.h - minImmersiveDepth;
        return (newHeight <= 0) ? 1 : newHeight;
    }

    private List<Item> findItemsBelow(Container container, Position pos, Item newItem) {
        if(!container.getBaseData().getZMap().containsKey(pos.z())) {
            return Collections.emptyList();
        }

        List<Item> belowItems = new ArrayList<>();
        List<Integer> zItems = container.getBaseData().getZMap().get(pos.z());
        for (int i = zItems.size() - 1; i >= 0; i--) {
            Item lowerItem = container.getItems().get(zItems.get(i));
            if(lowerItem.zh == pos.z() &&
                    lowerItem.x() < pos.x() + newItem.w &&
                    lowerItem.xw > pos.x() &&
                    lowerItem.y() < pos.y() + newItem.l &&
                    lowerItem.yl > pos.y()) {
                belowItems.add(lowerItem);
            }
        }

        return belowItems;
    }

    protected void addToCenterOfGravity(Item item, Position pos) {
        centerOfGravityForY += (pos.y() + (item.l / 2f)) * item.getWeight();
    }

    protected void removeFromCenterOfGravity(Item item, Position pos) {
        centerOfGravityForY -= (pos.y() + (item.l / 2f)) * item.getWeight();
    }

    public List<Item> getItems() {
        return itemList;
    }

    public List<Position> getActivePositions() {
        return activePosList;
    }

    public List<Item> getHistory() {
        return history;
    }

    public ContainerParameter getParameter() {
        return parameter;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public float getMaxWeight() {
        return maxWeight;
    }

    public int getContainerType() {
        return containerType;
    }

    public ContainerBaseData getBaseData() {
        return this;
    }

    public LPListMap<Integer, Integer> getXMap() {
        return xMap;
    }

    public LPListMap<Integer, Integer> getYMap() {
        return yMap;
    }

    public LPListMap<Integer, Integer> getZMap() {
        return zMap;
    }

    public ZItemGraph getZGraph() {
        return zGraph;
    }

    public Map<Integer, Float> getBearingCapacities() {
        return bearingCapacities;
    }

    public int getImmersiveDepthAtPosition(Position pos) {
        var immersiveDepth = immersiveDepthCache.get(pos);
        return (immersiveDepth != null) ? immersiveDepth : 0;
    }

    @Override
    public float getCenterOfGravityForY() {
        return centerOfGravityForY;
    }

    @Override
    public int getMaxYl() {
        return maxYl;
    }
}
