package xf.xflp.base.container;

import com.google.common.collect.HashBiMap;
import util.collection.IndexedArrayList;
import util.collection.LPListMap;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.PositionType;
import xf.xflp.base.item.Tools;

import java.util.ArrayList;
import java.util.List;

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
    }

    public ContainerBase(Container containerPrototype) {
        this.width = containerPrototype.getWidth();
        this.length = containerPrototype.getLength();
        this.height = containerPrototype.getHeight();
        this.maxWeight = containerPrototype.getMaxWeight();
        this.containerType = containerPrototype.getContainerType();
        this.parameter = containerPrototype.getParameter();
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
        for (Item item : this.itemList)
            sum += (item != null) ? item.weight : 0;

        return sum;
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

    /**
     *
     */
    protected Position createPosition(int x, int y, int z, PositionType type) {
        return Position.of(maxPosIdx++, x, y, z, type);
    }

    /**
     * If it is a stacking position (z > 0), then the immersive depth of lower items
     * must be checked. If this is the case, then the height of given item is reduced.
     */
    protected int retrieveHeight(Item item, Position pos) {
        if(pos.z == 0) {
            return item.h;
        }

        List<Item> lowerItems = Tools.findItemsBelow(this, pos, item);
        int minImmersiveDepth = lowerItems.stream().mapToInt(Item::getImmersiveDepth).min().orElse(0);

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

}
