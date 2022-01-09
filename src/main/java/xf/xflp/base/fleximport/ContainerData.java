package xf.xflp.base.fleximport;

import xf.xflp.base.container.AddRemoveContainer;
import xf.xflp.base.container.AddSpaceContainer;
import xf.xflp.base.container.Container;
import xf.xflp.base.container.GroundContactRule;
import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Copyright (c) 2012-2022 Holger Schneider
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

	Container create(DataManager manager, boolean isAddingAndRemovingItems) throws XFLPException {
		Class<? extends Container> correctContainerClass = (isAddingAndRemovingItems)
				? AddRemoveContainer.class
				: AddSpaceContainer.class;

		for (Constructor<?> constructor : correctContainerClass.getConstructors()) {
			if(constructor.getParameterCount() == 7) {
				try {
					return (Container) constructor.newInstance(
							width,
							length,
							height,
							maxWeight,
							manager.getContainerTypeIdx(containerType),
							GroundContactRule.FREE,
							1
					);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
					throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, e.getMessage(), e);
				}
			}
		}

		throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "Could not find correct constructor for container");
	}
}
