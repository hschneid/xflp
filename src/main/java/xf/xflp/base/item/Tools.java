package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class Tools {

	/**
	 * Returns the area size ratio of the cut between root item and cut item to root items full area size
	 * 
	 * @return cut area size / root area size
	 */
	public static float getCutRatio(Item rootItem, Item cutItem) {
		return getCutRatio(
				rootItem.x(), rootItem.y(), rootItem.w(), rootItem.l(),
				cutItem
		);
	}

	/**
	 * Returns the area size ratio of the cut between root item and cut item to root items full area size
	 *
	 * @return cut area size / root area size
	 */
	public static float getCutRatio(int x, int y, int w, int l, Item cutItem) {
		float xx = Math.min(cutItem.xw(), x + w) - Math.max(cutItem.x(), x);
		float yy = Math.min(cutItem.yl(), y + l) - Math.max(cutItem.y(), y);

		return (xx * yy) / (w * l);
	}
}
