package xf.xflp.base.container;

import com.google.common.collect.HashBiMap;
import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.container.constraints.LoadBearingChecker;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;
import xf.xflp.base.item.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ContainerBase implements Container, ContainerBaseData {

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

    protected int maxPosIdx = 0;
    protected ContainerParameter parameter = new DirectContainerParameter();

    protected ContainerBase(
            int width,
            int length,
            int height,
            float maxWeight,
            int containerType,
            GroundContactRule groundContactRule,
            float lifoImportance
    ) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.maxWeight = maxWeight;
        this.containerType = containerType;
        parameter.add(ParameterType.GROUND_CONTACT_RULE, groundContactRule);
        parameter.add(ParameterType.LIFO_IMPORTANCE, lifoImportance);

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

    @Override
    public boolean isItemAllowed(Item item) {
        return
                // If item can be loaded on any container
                (item.allowedContainerSet.size() == 1 && item.allowedContainerSet.contains(ContainerData.DEFAULT_CONTAINER_TYPE))
                        // or only on specific ones
                        || item.allowedContainerSet.contains(containerType);
    }

    @Override
    public long getLoadedVolume() {
        long sum = 0;
        for (Item item : this.itemList)
            if(item != null)
                sum += item.volume;

        return sum;
    }

    @Override
    public float getLoadedWeight() {
        float sum = 0;
        List<Item> list = this.itemList;
        for (int i = list.size() - 1; i >= 0; i--) {
            Item item = list.get(i);
            sum += (item != null) ? item.weight : 0;
        }

        return sum;
    }

    protected void addItem(Item item, Position pos) {
        // Adjust height for immersive depth
        item.h = retrieveHeight(item, pos);

        item.setPosition(pos);
        itemList.add(item);
        item.containerIndex = this.index;

        itemPositionMap.put(item, pos);

        xMap.put(item.x, item.index);
        xMap.put(item.xw, item.index);
        yMap.put(item.y, item.index);
        yMap.put(item.yl, item.index);
        zMap.put(item.z, item.index);
        zMap.put(item.zh, item.index);

        weight += item.weight;

        // Insert into Z-Graph
        zGraph.add(item, itemList, zMap);
    }

    protected List<Position> findInsertPositions(Item item) {
        List<Position> posList = new ArrayList<>();

        // 3 basic positions
        Position verticalPosition = null, horizontalPosition = null;
        if(item.yl < this.length) {
            verticalPosition = createPosition(item.x, item.yl, item.z, PositionType.BASIC);
            posList.add(verticalPosition);
        }
        if(item.xw < this.width) {
            horizontalPosition = createPosition(item.xw, item.y, item.z, PositionType.BASIC);
            posList.add(horizontalPosition);
        }
        if(item.z + item.h < this.height) {
            posList.add(createPosition(item.x, item.y, item.z + item.h, PositionType.BASIC));
        }

        // 2 projected positions
        if(item.z == 0) {
            if(item.x > 0 && verticalPosition != null) {
                Item leftElement = findNextLeftElement(verticalPosition);
                int leftPos = (leftElement != null) ? leftElement.xw : 0;

                if(leftPos < item.x) {
                    posList.add(createPosition(leftPos, item.yl, item.z, PositionType.EXTENDED_H));
                }
            }

            if(item.y > 0 && horizontalPosition != null) {
                Item lowerElement = findNextDeeperElement(horizontalPosition);
                int lowerPos = (lowerElement != null) ? lowerElement.yl : 0;

                if(lowerPos < item.y) {
                    posList.add(createPosition(item.xw, lowerPos, item.z, PositionType.EXTENDED_V));
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
            if(item == null || item.y > pos.y || item.yl < pos.y || item.x > pos.x || item.xw > pos.x || pos.y == item.yl)
                continue;

            if(leftItem == null || item.xw > leftItem.xw)
                leftItem = item;
        }

        return leftItem;
    }

    protected Item findNextDeeperElement(Position pos) {
        Item lowerItem = null;

        for (Item item : itemList) {
            if(item == null || item.x > pos.x || item.xw < pos.x || item.y > pos.y || item.yl > pos.y || pos.x == item.xw)
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
            if(pos.z == item.z && pos.x >= item.x && pos.x < item.xw && pos.y == item.y)
                coveredPositionList.add(pos);
                // Liegt eine Position auf der linken Kante des Objekts, ist sie �berdeckt.
            else if(pos.z == item.z && pos.y >= item.y && pos.y < item.yl && pos.x == item.x)
                coveredPositionList.add(pos);
        }

        return coveredPositionList;
    }

    /**
     *
     */
    protected Position createPosition(int x, int y, int z, PositionType type) {
        return Position.of(maxPosIdx++, x, y, z, type);
    }

    protected void updateBearingCapacity(List<Item> items) {
        loadBearingChecker.update(this, items);
    }

    /**
     * If it is a stacking position, then the immersive depth of lower items
     * must be checked. If this is the case, then the height of given item is reduced.
     */
    protected int retrieveHeight(Item item, Position pos) {
        if(pos.z == 0) {
            return item.h;
        }

        List<Item> lowerItems = Tools.findItemsBelow(this, pos, item);
        if(lowerItems.size() == 0)
            return item.h;

        int minImmersiveDepth = Integer.MAX_VALUE;
        for (int i = lowerItems.size() - 1; i >= 0; i--) {
            minImmersiveDepth = Math.min(minImmersiveDepth, lowerItems.get(i).getImmersiveDepth());
        }

        int newHeight = item.h - minImmersiveDepth;
        return (newHeight <= 0) ? 1 : newHeight;
    }

    @Override
    public List<Item> getItems() {
        return itemList;
    }

    @Override
    public List<Position> getActivePositions() {
        return activePosList;
    }

    @Override
    public List<Item> getHistory() {
        return history;
    }

    @Override
    public ContainerParameter getParameter() {
        return parameter;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public float getMaxWeight() {
        return maxWeight;
    }

    @Override
    public int getContainerType() {
        return containerType;
    }

    @Override
    public ContainerBaseData getBaseData() {
        return this;
    }

    @Override
    public LPListMap<Integer, Integer> getXMap() {
        return xMap;
    }

    @Override
    public LPListMap<Integer, Integer> getYMap() {
        return yMap;
    }

    @Override
    public LPListMap<Integer, Integer> getZMap() {
        return zMap;
    }

    @Override
    public ZItemGraph getZGraph() {
        return zGraph;
    }

    @Override
    public Map<Integer, Float> getBearingCapacities() {
        return bearingCapacities;
    }
}
