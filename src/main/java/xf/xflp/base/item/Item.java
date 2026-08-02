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
public record Item(
	int origW,
	int origL,
	int origH,
	boolean spinable,
	boolean stackable,
	int loadingLoc,
	int unLoadingLoc,
	// Binary representation, where only one bit can be active
	long stackingGroup,
	// Allowed container types (cooled, dangerous goods, etc.)
	Set<Integer> allowedContainerSet,
	// Allowed items that can be stacked on top (binary representation)
	long allowedStackingGroups,
	// How many different items can be below this item, if it is stacked.
	int nbrOfAllowedStackedItems,
	// height, which reduces height of upper item, when something is stacked upon.
	int immersiveDepth,
	float weight,
	float stackingWeightLimit,
	/* Unique index of this item object */
	int externalIndex,
	/* Type of item: loading or unloading */
	LoadType loadingType,
	/* External index of this order. There can be two items
	 * with the same order index (up- and unloading) */
	int orderIndex,
	// Defines if this item is loaded (true) or unloaded (false)
	boolean isLoading
) {

	/**
	 * Convenience constructor that derives loadingType from isLoading.
	 */
	public Item(
			int origW, int origL, int origH,
			boolean spinable, boolean stackable,
			int loadingLoc, int unLoadingLoc,
			long stackingGroup, Set<Integer> allowedContainerSet,
			long allowedStackingGroups, int nbrOfAllowedStackedItems,
			int immersiveDepth,
			float weight, float stackingWeightLimit,
			int externalIndex, int orderIndex,
			boolean isLoading
	) {
		this(origW, origL, origH, spinable, stackable, loadingLoc, unLoadingLoc,
				stackingGroup, allowedContainerSet, allowedStackingGroups,
				nbrOfAllowedStackedItems, immersiveDepth, weight, stackingWeightLimit,
				externalIndex,
				isLoading ? LoadType.LOAD : LoadType.UNLOAD,
				orderIndex, isLoading);
	}

	@Override
	public String toString() {
		return "Item "+externalIndex+" "+loadingLoc+" "+unLoadingLoc+" ("+origW+","+origL+","+origH+")  "+stackingGroup;
	}

	public int getVolume() {
		return origW * origL * origH;
	}

	public int getOrigH() {
		return origH;
	}

	public int getUnLoadingLoc() {
		return unLoadingLoc;
	}

	public long getStackingGroup() {
		return stackingGroup;
	}

	public long getAllowedStackingGroups() {
		return allowedStackingGroups;
	}

	public float getWeight() {
		return weight;
	}

	public float getStackingWeightLimit() {
		return stackingWeightLimit;
	}

	public int getExternalIndex() {
		return externalIndex;
	}

	public LoadType getLoadingType() {
		return loadingType;
	}

	public int getNbrOfAllowedStackedItems() {
		return nbrOfAllowedStackedItems;
	}

	public int getImmersiveDepth() {
		return immersiveDepth;
	}

	public Item withImmersiveDepth(int immersiveDepth) {
		return new Item(origW, origL, origH, spinable, stackable, loadingLoc, unLoadingLoc,
				stackingGroup, allowedContainerSet, allowedStackingGroups,
				nbrOfAllowedStackedItems, immersiveDepth, weight, stackingWeightLimit,
				externalIndex, loadingType, orderIndex, isLoading);
	}

	public Item withSpinable(boolean spinable) {
		return new Item(origW, origL, origH, spinable, stackable, loadingLoc, unLoadingLoc,
				stackingGroup, allowedContainerSet, allowedStackingGroups,
				nbrOfAllowedStackedItems, immersiveDepth, weight, stackingWeightLimit,
				externalIndex, loadingType, orderIndex, isLoading);
	}

	public Item withLoadingLoc(int loadingLoc) {
		return new Item(origW, origL, origH, spinable, stackable, loadingLoc, unLoadingLoc,
				stackingGroup, allowedContainerSet, allowedStackingGroups,
				nbrOfAllowedStackedItems, immersiveDepth, weight, stackingWeightLimit,
				externalIndex, loadingType, orderIndex, isLoading);
	}

	public Item withUnLoadingLoc(int unLoadingLoc) {
		return new Item(origW, origL, origH, spinable, stackable, loadingLoc, unLoadingLoc,
				stackingGroup, allowedContainerSet, allowedStackingGroups,
				nbrOfAllowedStackedItems, immersiveDepth, weight, stackingWeightLimit,
				externalIndex, loadingType, orderIndex, isLoading);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item item)) return false;
		return externalIndex == item.externalIndex;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(externalIndex);
	}
}
