package xf.xflp.base.fleximport;

import xf.xflp.base.problem.Item;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * The InternalItemData class is only for interal use. 
 * It holds the same data like ItemData.
 * 
 * It gives access to the inserted data for XFLP suite. 
 * So the user wont see internal variable names or data
 * structures.
 * 
 * @author hschneid
 *
 */
public class InternalItemData extends ItemData {
	private static final long serialVersionUID = 6628460956306861678L;

	/**
	 * @return the externID
	 */
	public final String getExternID() {
		return externID;
	}

	/**
	 * @return the shipmentID
	 */
	public final String getShipmentID() {
		return shipmentID;
	}

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
	 * @return the weight
	 */
	public final float getWeight() {
		return weight;
	}

	/**
	 * @return the stackingWeightLimit
	 */
	public final float getStackingWeightLimit() {
		return stackingWeightLimit;
	}

	/**
	 * @return the stackingGroup
	 */
	public final String getStackingGroup() {
		return stackingGroup;
	}

	/**
	 * @return the allowedStackingGroups
	 */
	public final String getAllowedStackingGroups() {
		return allowedStackingGroups;
	}

	/**
	 * @return the allowedContainerSet
	 */
	public final String getAllowedContainerTypes() {
		return allowedContainerSet;
	}

	/**
	 * @return the loadingLocation
	 */
	public final String getLoadingLocation() {
		return loadingLocation;
	}

	/**
	 * @return the unloadingLocation
	 */
	public final String getUnloadingLocation() {
		return unloadingLocation;
	}

	/**
	 * @return the spinable
	 */
	public final boolean isSpinable() {
		return spinable;
	}
	
	/**
	 * 
	 * @param manager
	 * @return
	 */
	public Item createLoadingItem(DataManager manager) {
		Item i = new Item(
				manager.getItemIdx(externID),
				manager.getShipmentIdx(shipmentID),
				manager.getLocationIdx(loadingLocation),
				-1,//manager.getLocationIdx(unloadingLocation),
				width,
				length,
				height,
				weight,
				stackingWeightLimit,
				manager.getContainerTypes(allowedContainerSet),
				manager.getStackingGroupIdx(stackingGroup),
				manager.getStackingGroups(allowedStackingGroups),
				spinable,
				true // isLoading
				);
		
		return i;
	}
	
	/**
	 * 
	 * @param manager
	 * @return
	 */
	public Item createUnLoadingItem(DataManager manager) {
		Item i = new Item(
				manager.getItemIdx(externID),
				manager.getShipmentIdx(shipmentID),
				manager.getLocationIdx(unloadingLocation),
				-1,//manager.getLocationIdx(loadingLocation),
				width,
				length,
				height,
				weight,
				stackingWeightLimit,
				manager.getContainerTypes(allowedContainerSet),
				manager.getStackingGroupIdx(stackingGroup),
				manager.getStackingGroups(allowedStackingGroups),
				spinable,
				false // isLoading
				);
		
		return i;
	}

}
