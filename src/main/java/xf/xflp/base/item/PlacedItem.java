package xf.xflp.base.item;

import util.collection.Indexable;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * ItemPlacement holds all mutable planning data for an item.
 * The master data (original dimensions, weight, stacking rules, etc.)
 * remain on the Item object and are never changed by the algorithms.
 *
 * This separation allows the solver to snapshot / compare / restore
 * placement states without cloning items or containers.
 *
 * @author hschneid
 */
public class PlacedItem implements Indexable {

    private final Item item;

    /** Placed position coordinates */
    public int x = -1;
    public int y = -1;
    public int z = -1;

    /** Placed end coordinates (position + placed dimensions) */
    public int xw = -1;
    public int yl = -1;
    public int zh = -1;

    /** Placed dimensions (may differ from master data due to rotation / immersive depth) */
    public int w;
    public int l;
    public int h;

    /** Whether this item is rotated in its current placement */
    public boolean isRotated = false;

    /** Index in the data structure of its holding container (-1 if unpacked) */
    public int index = -1;

    /** Index of the container where the item is packed (-1 if unpacked) */
    public int containerIndex = -1;

    /**
     * Regular way of creating a new placement, when heuristic wants to place an item for the first time.
     */
    public PlacedItem(Item item) {
        this.item = item;
        this.w = item.origW;
        this.l = item.origL;
        this.h = item.origH;
    }

    /**
     * Creates a new empty placement initialised with the item's original dimensions.
     */
    public PlacedItem(int w, int l, int h) {
        this.w = w;
        this.l = l;
        this.h = h;
        this.item = null;
    }

    /**
     * Creates a deep copy of an existing placement.
     */
    public PlacedItem(PlacedItem other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.xw = other.xw;
        this.yl = other.yl;
        this.zh = other.zh;
        this.w = other.w;
        this.l = other.l;
        this.h = other.h;
        this.isRotated = other.isRotated;
        this.index = other.index;
        this.containerIndex = other.containerIndex;
        this.item = other.item;
    }

    public void setPosition(Position pos) {
        x = pos.x();
        y = pos.y();
        z = pos.z();
        xw = x + w;
        yl = y + l;
        zh = z + h;
    }

    public void rotate() {
        int tmp = w;
        w = l;
        l = tmp;

        isRotated = !isRotated;
    }

    public int getVolume() {
        return w * l * h;
    }

    /**
     * Resets placement to its initial (unpacked) state.
     *
     * @param origW original width from item master data
     * @param origL original length from item master data
     * @param origH original height from item master data
     */
    public void reset(int origW, int origL, int origH) {
        this.x = this.y = this.z = this.xw = this.yl = this.zh = -1;
        this.w = origW;
        this.l = origL;
        this.h = origH;
        this.isRotated = false;
        this.index = -1;
        this.containerIndex = -1;
    }

    @Override
    public int getIdx() {
        return index;
    }

    @Override
    public void setIdx(int idx) {
        this.index = idx;
    }

    public Item getItem() {
        return item;
    }
}

