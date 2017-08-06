package xf.xflp.base.fleximport;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class DataManager implements Serializable {
	private static final long serialVersionUID = 3987431565021981666L;
	
	private int maxItemID = 0;
	private int maxShipmentID = 0;
	private int maxLocationID = 0;
	private int maxStackingGroupID = 0;
	private int maxContainerTypeID = 0;
	
	private Map<String, Integer> itemMap = new HashMap<>();
	private Map<String, Integer> shipmentMap = new HashMap<>();
	private Map<String, Integer> locationMap = new HashMap<>();
	private Map<String, Integer> stackingGroupMap = new HashMap<>();
	private Map<String, Integer> containerTypeMap = new HashMap<>();
	
	/**
	 * 
	 * @param itemData
	 */
	public void add(InternalItemData itemData) {
		addItem(itemData.getExternID());
		addShipment(itemData.getShipmentID());
		addLocation(itemData.getLoadingLocation());
		addLocation(itemData.getUnloadingLocation());
		addStackingGroup(itemData.getStackingGroup(), itemData.getAllowedStackingGroups());
		addContainerTypes(itemData.getAllowedContainerTypes());
	}
	
	/**
	 * 
	 * @param conData
	 */
	public void add(InternalContainerData conData) {
		addContainerType(conData.getContainerType());
	}
	
	/**
	 * 
	 * @param itemID
	 */
	public void addItem(String itemID) {
		if(!itemMap.containsKey(itemID))
			itemMap.put(itemID, maxItemID++);
	}

	/**
	 * 
	 * @param shipmentID
	 */
	public void addShipment(String shipmentID) {
		if(!shipmentMap.containsKey(shipmentID))
			shipmentMap.put(shipmentID, maxShipmentID++);
	}
	
	/**
	 * 
	 * @param locationID
	 */
	public void addLocation(String locationID) {
		locationID = locationID.trim().toLowerCase();
		if(!locationMap.containsKey(locationID))
			locationMap.put(locationID, maxLocationID++);
	}
	
	/**
	 * 
	 * @param containerType
	 */
	public void addContainerType(String containerType) {
		containerType = containerType.trim().toLowerCase();
		if(!containerTypeMap.containsKey(containerType))
			containerTypeMap.put(containerType, maxContainerTypeID++);
	}
	
	/**
	 * 
	 * @param stackingGroupID
	 * @param stackingGroups
	 */
	public void addStackingGroup(String stackingGroupID, String stackingGroups) {
		stackingGroupID = stackingGroupID.trim().toLowerCase();
		if(!stackingGroupMap.containsKey(stackingGroupID))
			stackingGroupMap.put(stackingGroupID, maxStackingGroupID++);
		
		String[] arr = stackingGroups.split(",");
		for (String s : arr) {
			s = s.trim().toLowerCase();
			if(!stackingGroupMap.containsKey(s))
				stackingGroupMap.put(s, maxStackingGroupID++);
		}
	}
	
	/**
	 * 
	 * @param containerTypes
	 */
	public void addContainerTypes(String containerTypes) {
		String[] arr = containerTypes.split(",");
		
		for (String s : arr) {
			s = s.trim().toLowerCase();
			if(!containerTypeMap.containsKey(s))
				containerTypeMap.put(s, maxContainerTypeID++);
		}
	}
	
	/**
	 * 
	 * @param itemID
	 * @return
	 */
	public int getItemIdx(String itemID) {
		return itemMap.get(itemID);
	}

	/**
	 * 
	 * @param shipmentID
	 * @return
	 */
	public int getShipmentIdx(String shipmentID) {
		return shipmentMap.get(shipmentID);
	}

	/**
	 * 
	 * @param locationID
	 * @return
	 */
	public int getLocationIdx(String locationID) {
		return locationMap.get(locationID.trim().toLowerCase());
	}

	/**
	 * 
	 * @param stackingGroup
	 * @return
	 */
	public int getStackingGroupIdx(String stackingGroup) {
		return 1 << stackingGroupMap.get(stackingGroup.trim().toLowerCase());
	}

	/**
	 * 
	 * @param containerType
	 * @return
	 */
	public int getContainerTypeIdx(String containerType) {
		return containerTypeMap.get(containerType.trim().toLowerCase());
	}
	
	/**
	 * 
	 * @param allowedContainerSet
	 * @return
	 */
	public Set<Integer> getContainerTypes(String allowedContainerSet) {
		String[] arr = allowedContainerSet.split(",");
		
		Set<Integer> res = new HashSet<>();
		for (String s : arr)
			res.add(containerTypeMap.get(s.trim().toLowerCase()));
		
		return res;
	}

	/**
	 * 
	 * @param allowedStackingGroups
	 * @return
	 */
	public int getStackingGroups(String allowedStackingGroups) {
		int res = 0;
		
		String[] arr = allowedStackingGroups.split(",");
		for (String s : arr)
			res += 1 << stackingGroupMap.get(s.trim().toLowerCase());
		
		return res;
	}

	/**
	 * 
	 */
	public void clear() {
		maxItemID = 0;
		maxShipmentID = 0;
		maxLocationID = 0;
		maxStackingGroupID = 0;
		maxContainerTypeID = 0;
		
		itemMap.clear();
		shipmentMap.clear();
		locationMap.clear();
		stackingGroupMap.clear();
		containerTypeMap.clear();
	}

}
