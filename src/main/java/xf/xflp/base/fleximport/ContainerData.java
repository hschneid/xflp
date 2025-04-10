package xf.xflp.base.fleximport;

import util.collection.IndexedArrayList;
import xf.xflp.base.XFLPParameter;
import xf.xflp.base.container.*;
import xf.xflp.base.container.constraints.AxleLoadParameter;
import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Copyright (c) 2012-2025 Holger Schneider
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

	private static final long serialVersionUID = ObjectStreamClass.lookup(ContainerData.class).getSerialVersionUID();

	public static final int DEFAULT_CONTAINER_TYPE = 0;

	private int width = Integer.MAX_VALUE;
	private int length = Integer.MAX_VALUE;
	private int height = Integer.MAX_VALUE;
	private float maxWeight = Float.MAX_VALUE;
	private String containerType = "default_container_type";

	// Configuration of permissible axle load
	private float firstPermissibleAxleLoad = Float.MAX_VALUE;
	private float secondPermissibleAxleLoad = Float.MAX_VALUE;
	private float axleDistance = 0;

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

	public void setFirstPermissibleAxleLoad(float firstPermissibleAxleLoad) {
		this.firstPermissibleAxleLoad = firstPermissibleAxleLoad;
	}

	public void setSecondPermissibleAxleLoad(float secondPermissibleAxleLoad) {
		this.secondPermissibleAxleLoad = secondPermissibleAxleLoad;
	}

	public void setAxleDistance(float axleDistance) {
		this.axleDistance = axleDistance;
	}

	////////////////////////////////////////

	/**
	 * @return the containerType
	 */
	String getContainerType() {
		return containerType;
	}

	Container create(DataManager manager, XFLPParameter parameter, boolean isAddingAndRemovingItems) throws XFLPException {
		Class<? extends Container> correctContainerClass = (isAddingAndRemovingItems)
				? AddRemoveContainer.class
				: AddContainer.class;

		for (Constructor<?> constructor : correctContainerClass.getConstructors()) {
			if(constructor.getParameterCount() == 6) {
				try {

					ContainerParameter containerParameter = new DirectContainerParameter();
					containerParameter.add(ParameterType.GROUND_CONTACT_RULE, parameter.getGroundContactRule());
					containerParameter.add(ParameterType.LIFO_IMPORTANCE, 1f);
					containerParameter.add(ParameterType.AXLE_LOAD, new AxleLoadParameter(
							firstPermissibleAxleLoad, secondPermissibleAxleLoad, axleDistance
					));

					return (Container) constructor.newInstance(
							width,
							length,
							height,
							maxWeight,
							manager.getContainerTypeIdx(containerType),
							containerParameter
					);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
					throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, e.getMessage(), e);
				}
			}
		}

		throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "Could not find correct constructor for container");
	}
}
