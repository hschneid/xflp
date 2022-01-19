package xf.xflp.base.fleximport;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * FlexiImporter is the class which holds the concept for flexible and adaptive
 * import of data into the XFLP suite. The general points are
 * - abstraction of data handling between chaotic user and stable algorithms
 * - abstraction of import sequence (which data is first?)
 * - Easy-to-fill data objects by eliminating import methods (set/add) with static parameters
 * - Handling of vehicles (default or fleet)
 * - Assiging the vehicle priority by sorting the vehicles with their capacities
 * 
 * After import collected data can be accessed XFLP suite. 
 * @author hschneid
 */
public class FlexiImporter implements Serializable {
	private static final long serialVersionUID = -6460880073124361069L;

	private final DataManager dataManager = new DataManager();
	
	private final List<ItemData> itemList = new ArrayList<>();
	private final List<ContainerData> containerList = new ArrayList<>();

	private ItemData lastItemData = null;
	private ContainerData lastContainerData = null;

	/**
	 * If XFLP suite begins execution, the import process is finished
	 * and all achieved data objects are finalized and inserted into the
	 * data lists.
	 */
	public void finishImport() {
		if(lastItemData != null) {
			dataManager.add(lastItemData);
			itemList.add(lastItemData);
			lastItemData = null;
		}
		if(lastContainerData != null) {
			dataManager.add(lastContainerData);
			containerList.add(lastContainerData);
			lastContainerData = null;
		}
	}

	/**
	 * Achieve a depot data object, where user can import data in any
	 * sequence. The call of this method means, that the last achieved
	 * depot data object is finalized and added to the internal depot list.
	 * 
	 * @return Depot data, where user can import data in any sequence
	 */
	public ItemData getItemData() {
		if(lastItemData != null) {
			dataManager.add(lastItemData);
			itemList.add(lastItemData);
		}

		lastItemData = new ItemData();

		return lastItemData;
	}

	/**
	 * Achieve a vehicle data object, where user can import data in any
	 * sequence. The call of this method means, that the last achieved
	 * vehicle data object is finalized and added to the internal vehicle list.
	 * 
	 * By achieving a vehicle data object, the default vehicle is put out of
	 * vehicle list. So the default vehicle parameter have to be announced in
	 * specific own vehicle data.
	 * 
	 * @return Container data, where user can import data in any sequence
	 */
	public ContainerData getContainerData() {
		if(lastContainerData != null) {
			dataManager.add(lastContainerData);
			containerList.add(lastContainerData);
		}

		lastContainerData = new ContainerData();

		return lastContainerData;
	}

	/**
	 * Clears all internal data lists and reset the internal fields.
	 */
	public void clear() {
		itemList.clear();
		containerList.clear();

		lastItemData = null;
		lastContainerData = null;
		
		dataManager.clear();
	}

	/**
	 * Clears all imported items.
	 */
	public void clearItems() {
		itemList.clear();
		lastItemData = null;
		dataManager.clearItems();
	}

	/**
	 * Removes all inserted container and reset the planning parameters to default.
	 */
	public void clearContainers() {
		containerList.clear();
		lastContainerData = null;
	}

	/**
	 * 
	 * @return the collected depots
	 */
	public List<ItemData> getItemList() {
		return itemList;
	}

	/**
	 * Transform imported items into loading and unloading items
	 */
	public List<Item> getConvertedItemList() {
		return itemList.stream()
				.flatMap(itemData -> {
					Item item = itemData.createLoadingItem(dataManager);
					if(itemData.getUnloadingLocation().length() > 0)
						return Stream.of(item, itemData.createUnLoadingItem(dataManager));
					return Stream.of(item);
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * 
	 * @return the collected vehicles
	 */
	public List<ContainerData> getContainerList() {
		return containerList;
	}
	
	public List<Container> getConvertedContainerList(List<Item> items) {
		boolean isAddingAndRemovingItems = checkForAddRemove(items);

		return containerList.stream()
				.map(con -> con.create(dataManager, isAddingAndRemovingItems))
				.collect(Collectors.toList());
	}

	private boolean checkForAddRemove(List<Item> items) {
		for (Item item : items) {
			if(item.getUnLoadingLoc() != -1) {
				return true;
			}
		}
		return false;
	}

	public DataManager getDataManager() {
		return dataManager;
	}
}
