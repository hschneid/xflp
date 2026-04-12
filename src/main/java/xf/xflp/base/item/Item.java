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

	public boolean spinable, stackable = true;
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

	public Item() {}

	public void postInit() {
		this.placement = new ItemPlacement(this.origW, this.origL, this.origH);
		this.loadingType = (isLoading) ? LoadType.LOAD : LoadType.UNLOAD;
	}

	public void rotate() {
		int tmp = w();
		setW(l());
		setL(tmp);

		placement.isRotated = !placement.isRotated;
	}

	public void setPosition(Position pos) {
		placement.x = pos.x();
		placement.y = pos.y();
		placement.z = pos.z();
		placement.xw = x() + w();
		placement.yl = y() + l();
		placement.zh = z() + h();
	}

	public void clearPosition() {
		placement.x = placement.y = placement.z = placement.xw = placement.yl = placement.zh = -1;
	}

	@Override
	public String toString() {
		return "Item "+this.externalIndex+" "+loadingLoc+" "+unLoadingLoc+" ("+w()+","+l()+","+h()+") ["+x()+", "+y()+", "+z()+" "+(placement.isRotated?"R":"")+"]"+ " "+stackingGroup;
	}

	/**
	 * Resets the item to its initial (unpacked) state.
	 * All placement data is cleared and dimensions are restored to original values.
	 */
	public void reset() {
		// Restore original dimensions
		placement.w = origW;
		placement.l = origL;
		placement.h = origH;
		clearPosition();
		placement.index = -1;
		placement.containerIndex = -1;
		placement.isRotated = false;
		this.isLoading = false;
		placement.reset(origW, origL, origH);
	}

	/**
	 * Returns a snapshot copy of the current placement.
	 */
	public ItemPlacement snapshotPlacement() {
		return new ItemPlacement(placement);
	}

	/**
	 * Restores the item's mutable fields from a previously saved placement snapshot.
	 */
	public void restorePlacement(ItemPlacement snapshot) {
		this.placement = new ItemPlacement(snapshot);
	}

	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#getIdx()
	 */
	@Override
	public int getIdx() {
		return placement.index;
	}
	
	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#setIdx(int)
	 */
	@Override
	public void setIdx(int idx) {
		placement.index = idx;
	}

	public int getVolume() {
		return w() * l() * h();
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

	public int xw() {
		return placement.xw;
	}

	public int getXw() {
		return placement.xw;
	}

	public void setXw(int xw) {
		this.placement.xw = xw;
	}

	public int yl() {
		return placement.yl;
	}

	public int getYl() {
		return placement.yl;
	}

	public void setYl(int yl) {
		this.placement.yl = yl;
	}

	public int zh() {
		return placement.zh;
	}

	public int getZh() {
		return placement.zh;
	}

	public void setZh(int zh) {
		this.placement.zh = zh;
	}

	public int w() {
		return placement.w;
	}

	public int getW() {
		return placement.w;
	}

	public void setW(int w) {
		this.placement.w = w;
	}

	public int l() {
		return placement.l;
	}

	public int getL() {
		return placement.l;
	}

	public void setL(int l) {
		this.placement.l = l;
	}

	public int h() {
		return placement.h;
	}

	public int getH() {
		return placement.h;
	}

	public void setH(int h) {
		this.placement.h = h;
	}

	public void setOrigW(int width) {
		this.origW = width;
	}

	public void setOrigL(int length) {
		this.origL = length;
	}

	public void setOrigH(int h) {
		this.origH = h;
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

	public int index() {
		return placement.index;
	}

	public int getIndex() {
		return placement.index;
	}

	public void setIndex(int index) {
		placement.index = index;
	}

	public int getContainerIndex() {
		return placement.containerIndex;
	}

	public void setContainerIndex(int containerIndex) {
		placement.containerIndex = containerIndex;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean loading) {
		isLoading = loading;
	}

	public boolean isRotated() {
		return placement.isRotated;
	}

	public void setRotated(boolean rotated) {
		placement.isRotated = rotated;
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
		return placement.index == item.placement.index;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
