package xf.xflp.base.fleximport;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.GroundContactRule;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class InternalContainerData extends ContainerData {

	private static final long serialVersionUID = -3429254830973943702L;

	/**
	 * @return the width
	 */
	public final int getWidth() {
		return width;
	}
	/**
	 * @return the length
	 */
	public final int getLength() {
		return length;
	}
	/**
	 * @return the height
	 */
	public final int getHeight() {
		return height;
	}
	/**
	 * @return the maxWeight
	 */
	public final float getMaxWeight() {
		return maxWeight;
	}
	/**
	 * @return the containerType
	 */
	public final String getContainerType() {
		return containerType;
	}

	public Container create(DataManager manager) {
		Container c = new Container();

		c.setWidth(width);
		c.setLength(length);
		c.setHeight(height);
		c.setMaxWeight(maxWeight);
		c.setContainerType(manager.getContainerTypeIdx(containerType));
		c.setLifoImportance(1);
		c.setGroundContactRule(GroundContactRule.FREE);

		c.init();

		return c;
	}
}
