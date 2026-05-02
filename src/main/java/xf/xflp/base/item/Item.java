package xf.xflp.base.item;


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
public class Item {

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

	public Item() {}

	public void postInit() {
		this.loadingType = (isLoading) ? LoadType.LOAD : LoadType.UNLOAD;
	}

	@Override
	public String toString() {
		return "Item "+this.externalIndex+" "+loadingLoc+" "+unLoadingLoc+" ("+origW+","+origL+","+origH+")  "+stackingGroup;
	}

	public int getVolume() {
		return origW * origL * origH;
	}

	public int getOrigH() {
		return origH;
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

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean loading) {
		isLoading = loading;
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
		return externalIndex == item.externalIndex;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void setSpinable(boolean spinable) {
		this.spinable = spinable;
	}
}
