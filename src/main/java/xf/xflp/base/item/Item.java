package xf.xflp.base.item;


import util.collection.Indexable;
import xf.xflp.report.LoadType;

import java.util.Set;

/** 
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * An item is the entity, which is placed into a container. It contains
 * all master data, parameter and planning information (like the current position).
 *
 * @author hschneid
 *
 */
public class Item implements Indexable {

	// --- Master data (immutable after postInit) ---
	public int origW, origL, origH;
	public int size, volume;

	public boolean spinable, stackable;
	public int loadingLoc, unLoadingLoc;

	// Binary representation, where only one bit can be active
	public long stackingGroup;
	// Allowed container types (cooled, dangerous goods, etc.)
	public Set<Integer> allowedContainerSet;
	// Allowed items that can be stacked on top (binary representation)
	public long allowedStackingGroups;
	// How many different items can be below this item, if it is stacked.
	public int nbrOfAllowedStackedItems;
	// height, which reduces height of upper item, when something is stacked upon.
	public int immersiveDepth;

	public float weight;
	public float stackingWeightLimit;

	/* Unique index of this item object*/
	public int externalIndex;
	/* Type of item: loading or unloading */
	public LoadType loadingType;
	/* External externalIndex of this order. There can be two items
	 * with the same order externalIndex (up- and unloading) */
	public int orderIndex = -1;

	// Defines if this item is loaded (true) or unloaded (false)
	public boolean isLoading = false;

	// --- Mutable placement data (delegated to ItemPlacement) ---
	private ItemPlacement placement;

	// Backward-compatible public field aliases – kept in sync with placement
	public int xw, yl, zh, w, l, h;
	public boolean isRotated = false;
	/* Idx in data structure of its holding container (-1 if unpacked) */
	public int index = -1;
	/* Idx of the container, where the item is packed in (-1 if unpacked) */
	public int containerIndex = -1;

	public Item() {
		this.xw = this.yl = this.zh = -1;
		this.stackable = true;
	}

	public void postInit() {
		this.origW = w;
		this.origL = l;
		this.origH = h;
		this.size = w * l;
		this.volume = h * w * l;
		this.loadingType = (isLoading) ? LoadType.LOAD : LoadType.UNLOAD;
		this.placement = new ItemPlacement(w, l, h);
	}

	public void rotate() {
		int tmp = w;
		w = l;
		l = tmp;

		isRotated = !isRotated;
		syncToPlacement();
	}

	public void setPosition(Position pos) {
		placement.x = pos.x();
		placement.y = pos.y();
		placement.z = pos.z();
		xw = x() + w;
		yl = y() + l;
		zh = z() + h;
		syncToPlacement();
	}

	public void clearPosition() {
		this.xw = this.yl = this.zh = -1;
		syncToPlacement();
	}

	@Override
	public String toString() {
		return "Item "+this.externalIndex+" "+loadingLoc+" "+unLoadingLoc+" ("+w+","+l+","+h+") ["+x()+", "+y()+", "+z()+" "+(this.isRotated?"R":"")+"]"+ " "+stackingGroup;
	}

	/**
	 * Resets the item to its initial (unpacked) state.
	 * All placement data is cleared and dimensions are restored to original values.
	 */
	public void reset() {
		// Restore original dimensions
		this.w = origW;
		this.l = origL;
		this.h = origH;
		clearPosition();
		this.index = -1;
		this.containerIndex = -1;
		this.isRotated = false;
		this.isLoading = false;
		if (placement != null) {
			placement.reset(origW, origL, origH);
		}
	}

	/**
	 * Returns the current placement (lazy-synced from public fields).
	 */
	public ItemPlacement getPlacement() {
		syncToPlacement();
		return placement;
	}

	/**
	 * Returns a snapshot copy of the current placement.
	 */
	public ItemPlacement snapshotPlacement() {
		syncToPlacement();
		return new ItemPlacement(placement);
	}

	/**
	 * Restores the item's mutable fields from a previously saved placement snapshot.
	 */
	public void restorePlacement(ItemPlacement snapshot) {
		this.placement = new ItemPlacement(snapshot);
		syncFromPlacement();
	}

	/** Pushes current public fields into the placement object */
	private void syncToPlacement() {
		if (placement == null) return;
		placement.xw = this.xw;
		placement.yl = this.yl;
		placement.zh = this.zh;
		placement.w = this.w;
		placement.l = this.l;
		placement.h = this.h;
		placement.isRotated = this.isRotated;
		placement.index = this.index;
		placement.containerIndex = this.containerIndex;
	}

	/** Pulls placement state back into the public fields */
	private void syncFromPlacement() {
		this.xw = placement.xw;
		this.yl = placement.yl;
		this.zh = placement.zh;
		this.w = placement.w;
		this.l = placement.l;
		this.h = placement.h;
		this.isRotated = placement.isRotated;
		this.index = placement.index;
		this.containerIndex = placement.containerIndex;
	}

	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#getIdx()
	 */
	@Override
	public int getIdx() {
		return index;
	}
	
	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#setIdx(int)
	 */
	@Override
	public void setIdx(int idx) {
		this.index = idx;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getVolume() {
		return w * l * h;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
		this.origH = h;
	}

	public int getOrigH() {
		return origH;
	}

	public int x() {
		return placement.x;
	}

	public int getX() {
		return placement.x;
	}

	public void setX(int x) {
		this.placement.x = x;
	}

	public int y() {
		return placement.y;
	}

	public int getY() {
		return placement.y;
	}

	public void setY(int y) {
		this.placement.y = y;
	}

	public int z() {
		return placement.z;
	}

	public int getZ() {
		return placement.z;
	}

	public void setZ(int z) {
		this.placement.z = z;
	}

	public int getXw() {
		return xw;
	}

	public void setXw(int xw) {
		this.xw = xw;
	}

	public int getYl() {
		return yl;
	}

	public void setYl(int yl) {
		this.yl = yl;
	}

	public int getZh() {
		return zh;
	}

	public void setZh(int zh) {
		this.zh = zh;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}

	public boolean isSpinable() {
		return spinable;
	}

	public void setSpinable(boolean spinable) {
		this.spinable = spinable;
	}

	public boolean isStackable() {
		return stackable;
	}

	public void setStackable(boolean stackable) {
		this.stackable = stackable;
	}

	public int getLoadingLoc() {
		return loadingLoc;
	}

	public void setLoadingLoc(int loadingLoc) {
		this.loadingLoc = loadingLoc;
	}

	public int getUnLoadingLoc() {
		return unLoadingLoc;
	}

	public void setUnLoadingLoc(int unLoadingLoc) {
		this.unLoadingLoc = unLoadingLoc;
	}

	public long getStackingGroup() {
		return stackingGroup;
	}

	public void setStackingGroup(long stackingGroup) {
		this.stackingGroup = stackingGroup;
	}

	public Set<Integer> getAllowedContainerSet() {
		return allowedContainerSet;
	}

	public void setAllowedContainerSet(Set<Integer> allowedContainerSet) {
		this.allowedContainerSet = allowedContainerSet;
	}

	public long getAllowedStackingGroups() {
		return allowedStackingGroups;
	}

	public void setAllowedStackingGroups(long allowedStackingGroups) {
		this.allowedStackingGroups = allowedStackingGroups;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getStackingWeightLimit() {
		return stackingWeightLimit;
	}

	public void setStackingWeightLimit(float stackingWeightLimit) {
		this.stackingWeightLimit = stackingWeightLimit;
	}

	public int getExternalIndex() {
		return externalIndex;
	}

	public void setExternalIndex(int externalIndex) {
		this.externalIndex = externalIndex;
	}

	public LoadType getLoadingType() {
		return loadingType;
	}

	public void setLoadingType(LoadType loadingType) {
		this.loadingType = loadingType;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getContainerIndex() {
		return containerIndex;
	}

	public void setContainerIndex(int containerIndex) {
		this.containerIndex = containerIndex;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean loading) {
		isLoading = loading;
	}

	public boolean isRotated() {
		return isRotated;
	}

	public void setRotated(boolean rotated) {
		isRotated = rotated;
	}

	public int getNbrOfAllowedStackedItems() {
		return nbrOfAllowedStackedItems;
	}

	public void setNbrOfAllowedStackedItems(int nbrOfAllowedStackedItems) {
		this.nbrOfAllowedStackedItems = nbrOfAllowedStackedItems;
	}

	public int getImmersiveDepth() {
		return immersiveDepth;
	}

	public void setImmersiveDepth(int immersiveDepth) {
		this.immersiveDepth = immersiveDepth;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item item)) return false;
		return index == item.index;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
