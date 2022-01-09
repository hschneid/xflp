package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2022 Holger Schneider
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
		float x = Math.min(rootItem.xw, cutItem.xw) - Math.max(rootItem.x, cutItem.x);
		float y = Math.min(rootItem.yl, cutItem.yl) - Math.max(rootItem.y, cutItem.y);

		return (x * y) / rootItem.size;
	}

}
