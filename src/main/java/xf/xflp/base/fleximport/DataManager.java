package xf.xflp.base.fleximport;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class DataManager implements Serializable {
	private static final long serialVersionUID = 3987431565021981666L;

	private int maxItemID = 0;
	private int maxShipmentID = 1;
	private int maxLocationID = 0;
	private int maxStackingGroupID = 1;
	private int maxContainerTypeID = 1;

	private final Map<String, Integer> itemMap = new HashMap<>();
	private final Map<Integer, String> itemIdMap = new HashMap<>();
	private final Map<String, Integer> shipmentMap = new HashMap<>();
	private final Map<String, Integer> locationMap = new HashMap<>();
	private final Map<String, Integer> stackingGroupMap = new HashMap<>();
	private final Map<String, Integer> containerTypeMap = new HashMap<>();

	public DataManager() {
		containerTypeMap.put("default_container_type", 0);
		stackingGroupMap.put("default_stacking_group", 0);
		shipmentMap.put("default_shipment", 0);
		locationMap.put("", -1);
	}

	public void add(ItemData itemData) {
		addItem(itemData.getExternID());
		addShipment(itemData.getShipmentID());
		addLocation(itemData.getLoadingLocation());
		addLocation(itemData.getUnloadingLocation());
		addStackingGroup(itemData.getStackingGroup(), itemData.getAllowedStackingGroups());
		addContainerTypes(itemData.getAllowedContainerTypes());
	}

	public void add(ContainerData conData) {
		addContainerType(conData.getContainerType());
	}

	public void addItem(String itemID) {
		if(!itemMap.containsKey(itemID)) {
			itemMap.put(itemID, maxItemID);
			itemIdMap.put(maxItemID, itemID);
			maxItemID++;
		}
	}

	public void addShipment(String shipmentID) {
		if(!shipmentMap.containsKey(shipmentID))
			shipmentMap.put(shipmentID, maxShipmentID++);
	}

	public void addLocation(String locationID) {
		if(!locationMap.containsKey(locationID))
			locationMap.put(locationID, maxLocationID++);
	}

	public void addContainerType(String containerType) {
		if(!containerTypeMap.containsKey(containerType))
			containerTypeMap.put(containerType, maxContainerTypeID++);
	}

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

	public void addContainerTypes(String containerTypes) {
		String[] arr = containerTypes.split(",");

		for (String s : arr) {
			if(!containerTypeMap.containsKey(s))
				containerTypeMap.put(s, maxContainerTypeID++);
		}
	}

	public int getItemIdx(String itemID) {
		return itemMap.get(itemID);
	}

	public String getItemId(int itemIdx) {
		return itemIdMap.get(itemIdx);
	}

	public int getShipmentIdx(String shipmentID) {
		return shipmentMap.get(shipmentID);
	}

	public int getLocationIdx(String locationID) {
		return locationMap.get(locationID.trim().toLowerCase());
	}

	public int getStackingGroupIdx(String stackingGroup) {
		return 1 << stackingGroupMap.get(stackingGroup.trim().toLowerCase());
	}

	public int getContainerTypeIdx(String containerType) {
		return containerTypeMap.get(containerType);
	}

	public String getContainerTypeName(int index) {
		for (Map.Entry<String, Integer> entry : containerTypeMap.entrySet()) {
			if(entry.getValue() == index)
				return entry.getKey();
		}

		return "not found";
	}

	public Set<Integer> getContainerTypes(String allowedContainerSet) {
		String[] arr = allowedContainerSet.split(",");

		Set<Integer> res = new HashSet<>(arr.length);
		for (String s : arr)
			res.add(containerTypeMap.get(s));

		return res;
	}

	public int getStackingGroups(String allowedStackingGroups) {
		int res = 0;

		String[] arr = allowedStackingGroups.split(",");
		for (String s : arr) {
			s = s.trim().toLowerCase();
			if(stackingGroupMap.containsKey(s)) {
				res += 1 << stackingGroupMap.get(s);
			}
		}

		return res;
	}

	/**
	 *
	 */
	public void clear() {
		maxItemID = 0;
		maxShipmentID = 1;
		maxLocationID = 0;
		maxStackingGroupID = 1;
		maxContainerTypeID = 1;

		itemMap.clear();
		shipmentMap.clear();
		locationMap.clear();
		stackingGroupMap.clear();
		containerTypeMap.clear();
	}

	public void clearItems() {
		maxItemID = 0;
		maxShipmentID = 1;
		itemIdMap.clear();
		itemMap.clear();
		shipmentMap.clear();
		shipmentMap.put("default_shipment", 0);
	}
}
