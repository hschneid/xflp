package xf.xflp.base.fleximport;

import java.io.Serializable;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Depot data class defines all fields which can be used
 * in XFLP suite. Users can call the set-Methods to insert
 * data into this object. There is no predefined sequence of 
 * doing that. All fields are initialized by default values 
 * in that way, that restrictions are always holds.
 * 
 * Each set-method returns the depot data object itself, so
 * that the set-methods can be called in one way.
 * 
 * There is no way to clear a value back to default value. The
 * user has to do that by himself. All values can be set multiple times, 
 * where last set value is overwritten with only one exception:
 * It is possible to set multiple time windows. It is not possible
 * to clear the list of inserted time windows.
 * 
 * @author hschneid
 *
 */
public abstract class ItemData implements Serializable {
	private static final long serialVersionUID = -8970875565639368202L;
	
	protected String externID = "";
	protected String shipmentID = "default_shipment";

	protected int width = -1;
	protected int length = -1;
	protected int height = -1;
	
	protected float weight = 0;
	protected float stackingWeightLimit = Float.MAX_VALUE;
	
	protected String stackingGroup = "default_stackinggroup";
	protected String allowedStackingGroups = new String();
	protected String allowedContainerSet = new String();
	
	protected String loadingLocation = "";
	protected String unloadingLocation = "";
	
	protected boolean spinable = true;

	/**
	 * @param externID the externID to set
	 * @return
	 */
	public final ItemData setExternID(String externID) {
		this.externID = externID;
		return this;
	}

	/**
	 * @param shipmentID the shipmentID to set
	 * @return
	 */
	public final ItemData setShipmentID(String orderID) {
		this.shipmentID = orderID;
		return this;
	}

	/**
	 * @param width the width to set
	 * @return
	 */
	public final ItemData setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * @param length the length to set
	 * @return
	 */
	public final ItemData setLength(int length) {
		this.length = length;
		return this;
	}

	/**
	 * @param height the height to set
	 * @return
	 */
	public final ItemData setHeight(int height) {
		this.height = height;
		return this;
	}

	/**
	 * @param weight the weight to set
	 * @return
	 */
	public final ItemData setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	/**
	 * @param stackingWeightLimit the stackingWeightLimit to set
	 * @return
	 */
	public final ItemData setStackingWeightLimit(float stackingWeightLimit) {
		this.stackingWeightLimit = stackingWeightLimit;
		return this;
	}

	/**
	 * @param stackingGroup the stackingGroup to set
	 * @return
	 */
	public final ItemData setStackingGroup(String stackingGroup) {
		this.stackingGroup = stackingGroup;
		return this;
	}

	/**
	 * @param allowedStackingGroups the allowedStackingGroups to set
	 * @return
	 */
	public final ItemData setAllowedStackingGroups(String allowedStackingGropus) {
		this.allowedStackingGroups = allowedStackingGropus;
		return this;
	}

	/**
	 * @param allowedContainerSet the allowedContainerSet to set
	 * @return
	 */
	public final ItemData setAllowedContainerSet(String allowedContainerSet) {
		this.allowedContainerSet = allowedContainerSet;
		return this;
	}

	/**
	 * @param loadingLocation the loadingLocation to set
	 * @return
	 */
	public final ItemData setLoadingLocation(String loadingLocation) {
		this.loadingLocation = loadingLocation;
		return this;
	}

	/**
	 * @param unloadingLocation the unloadingLocation to set
	 * @return
	 */
	public final ItemData setUnloadingLocation(String unloadingLocation) {
		this.unloadingLocation = unloadingLocation;
		return this;
	}

	/**
	 * @param spinable the spinable to set
	 * @return
	 */
	public final ItemData setSpinable(boolean spinable) {
		this.spinable = spinable;
		return this;
	}
}
