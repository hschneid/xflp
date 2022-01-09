package xf.xflp.base;

import xf.xflp.opt.construction.strategy.Strategy;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class XFLPParameter {

	private float lifoImportance = 0f;
	private int maxNbrOfContainer = 1;
	private Strategy preferredPackingStrategy = Strategy.HIGH_LOW_LEFT;
	private int nbrOfAllowedStackedItems = Integer.MAX_VALUE;

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public float getLifoImportance() {
		return lifoImportance;
	}

	public void setLifoImportance(float lifoImportance) {
		this.lifoImportance = lifoImportance;
	}

	public Strategy getPreferredPackingStrategy() {
		return preferredPackingStrategy;
	}

	/**
	 * The packing strategy is used by the placing algorithm to choose the best
	 * next insert position.
	 *
	 * This value must be choosen from enum:
	 *  - HIGH_LOW_LEFT : Choose highest, deepest and most left position
	 *  - TOUCHING_PERIMETER : Choose the position, where new item will touch as most as possible already placed items. If there are multiple best positions, choose with HIGH_LOW_LEFT.
	 *  - WIDTH_PROPORTION : Choose the position, where the width of the new item is nearest to a full proportion of the container width. If there are multiple best positions, choose with TOUCHING_PERIMETER.
	 *
	 * Default: HIGH_LOW_LEFT
	 */
	public void setPreferredPackingStrategy(Strategy preferredPackingStrategy) {
		this.preferredPackingStrategy = preferredPackingStrategy;
	}

	public int getMaxNbrOfContainer() {
		return maxNbrOfContainer;
	}

	public void setMaxNbrOfContainer(int maxNbrOfContainer) {
		this.maxNbrOfContainer = maxNbrOfContainer;
	}

	public int getNbrOfAllowedStackedItems() {
		return nbrOfAllowedStackedItems;
	}

	/**
	 * This value defines the maximal number of items, which are allowed to be
	 * placed/stacked on top of an item.
	 *
	 * If value is set to 1, then the algorithm can place/stack only one other item on top of any item.
	 *
	 * Default: No limitation
	 */
	public void setNbrOfAllowedStackedItems(int nbrOfAllowedStackedItems) {
		this.nbrOfAllowedStackedItems = nbrOfAllowedStackedItems;
	}
}
