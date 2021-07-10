package xf.xflp.base.fleximport;

import xf.xflp.base.problem.ComplexContainer;
import xf.xflp.base.problem.GroundContactRule;

import java.io.Serializable;

/** 
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class ContainerData implements Serializable {

	private static final long serialVersionUID = -5241212560474818458L;

	public static final int DEFAULT_CONTAINER_TYPE = 0;
	
	protected int width = Integer.MAX_VALUE;
	protected int length = Integer.MAX_VALUE;
	protected int height = Integer.MAX_VALUE;
	protected float maxWeight = Float.MAX_VALUE;
	protected String containerType = "default_container_type";
	
	/**
	 * @param width the width to set
	 */
	public final ContainerData setWidth(int width) {
		this.width = width;
		return this;
	}
	/**
	 * @param length the length to set
	 */
	public final ContainerData setLength(int length) {
		this.length = length;
		return this;
	}
	/**
	 * @param height the height to set
	 */
	public final ContainerData setHeight(int height) {
		this.height = height;
		return this;
	}
	/**
	 * @param maxWeight the maxWeight to set
	 */
	public final ContainerData setMaxWeight(float maxWeight) {
		this.maxWeight = maxWeight;
		return this;
	}
	/**
	 * @param containerType the containerType to set
	 */
	public final ContainerData setContainerType(String containerType) {
		this.containerType = containerType;
		return this;
	}

	////////////////////////////////////////

	/**
	 * @return the containerType
	 */
	String getContainerType() {
		return containerType;
	}

	ComplexContainer create(DataManager manager) {
		ComplexContainer c = new ComplexContainer();

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
