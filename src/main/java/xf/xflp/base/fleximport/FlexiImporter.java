package xf.xflp.base.fleximport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xf.xflp.base.problem.Container;

/** 
 * Copyright (c) 2012-present Holger Schneider
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
 * 
 * @author hschneid
 *
 */
public class FlexiImporter implements Serializable {
	private static final long serialVersionUID = -6460880073124361069L;

	private DataManager dataManager = new DataManager();
	
	private List<InternalItemData> itemList = new ArrayList<>();
	private List<InternalContainerData> containerList = new ArrayList<>();

	private InternalItemData lastItemData = null;
	private InternalContainerData lastContainerData = null;

	/**
	 * If XFLP suite begins execution, the import process is finished
	 * and all achieved data objects are finalized and inserted into the
	 * data lists.
	 */
	public void finishImport() {
		if(lastItemData != null) {
			dataManager.add(lastItemData);
			itemList.add(lastItemData);
		}
		if(lastContainerData != null) {
			dataManager.add(lastContainerData);
			containerList.add(lastContainerData);
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

		lastItemData = new InternalItemData();

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

		lastContainerData = new InternalContainerData();

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
	 * Clears all imported depots.
	 */
	public void clearItems() {
		itemList.clear();
	}

	/**
	 * Removes all inserted vehicles and reset the planning parameters to default.
	 * (See setCapacity() and setMaxRouteDuration() )
	 */
	public void clearContainers() {
		containerList.clear();
	}

	/**
	 * 
	 * @return the collected depots
	 */
	public List<InternalItemData> getItemList() {
		return itemList;
	}

	/**
	 * 
	 * @return the collected vehicles
	 */
	public List<InternalContainerData> getContainerList() {
		return containerList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Container> getConvertedContainerList() {
		List<Container> list = new ArrayList<>();
		
		for (InternalContainerData con : containerList)
			list.add(con.create(dataManager));
		
		return list;
	}

	/**
	 * 
	 * @return
	 */
	public DataManager getDataManager() {
		return dataManager;
	}
}
