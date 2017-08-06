package xf.xflp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.XFLPParameter;
import xf.xflp.base.XFLPSolution;
import xf.xflp.base.fleximport.ContainerData;
import xf.xflp.base.fleximport.FlexiImporter;
import xf.xflp.base.fleximport.InternalItemData;
import xf.xflp.base.fleximport.ItemData;
import xf.xflp.base.monitor.StatusCode;
import xf.xflp.base.monitor.StatusManager;
import xf.xflp.base.monitor.StatusMonitor;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.XFLPOptType;
import xf.xflp.report.LPReport;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * XFLP is central user interface for this suite. 
 * It combines all methods for data import, optimization execution, parameters and
 * retrieval of solutions.
 * 
 * The modeling of this class represents a state machine, where iteratively several 
 * methods must be called. The execution method take all inserted data and parameters
 * and start the optimizers.
 * 
 * 
 * @author hschneid
 *
 */
public class XFLP {

	/* Importer and data warehouse */
	protected FlexiImporter importer = new FlexiImporter();

	/* Optimization type - Chosen by user */
	private XFLPOptType optType;
	
	/* Last created solution */
	private XFLPSolution lastSolution;

	/* Shall be the metric transformed into a faster access metric */
	private XFLPParameter parameter = new XFLPParameter();

	/* Manages internal status messages to external observer */
	private StatusManager statusManager = new StatusManager();

	/**
	 * Calculates the VRP with the before inserted data
	 * by addDepot(), addCustomer(), addMetric() and 
	 * addVehicle() or the parameters setCapacity() and setMaxRouteDuration()
	 */
	public void executeLoadPlanning() {
		statusManager.fireMessage(StatusCode.RUNNING, "XFLP started");

		// Flush import buffer
		importer.finishImport();

		// Init a planning model
		XFLPModel model = init();
		
		// Optimize the model
		optType.createInstance().execute(model);
		
//		new XFLPDraw(optContainers);
		
		// Build solution object
		lastSolution = new XFLPSolution(model);

		statusManager.fireMessage(StatusCode.FINISHED, "XFLP finished sucessfully.");
	}

	/**
	 * Transforms the read data into a model, which can be used
	 * for optimization.
	 * 
	 * @param depotList
	 * @param customerList 
	 * @param veh Contains parameters for capacity, max route duration and others.
	 * @return Returns a model, which can be used for optimization procedures.
	 * @throws IllegalArgumentException
	 */
	private XFLPModel init() throws IllegalArgumentException {
		statusManager.fireMessage(StatusCode.RUNNING, "Initialisation");

		// Pr�fung auf Eingabeparameter
		if(importer.getItemList().size() == 0) {
			statusManager.fireMessage(StatusCode.ABORT, "No items are given.");
			throw new IllegalArgumentException("No items are given.");
		}
		if(importer.getContainerList().size() == 0) {
			statusManager.fireMessage(StatusCode.ABORT, "No container information were set.");
			throw new IllegalArgumentException("No container information were set.");
		}

		// Copy imported data to internal data structure
		// Container
		List<Container> containerTypeList = importer.getConvertedContainerList();
		// Items
		List<Item> itemList = new ArrayList<>();
		{
			// Transform imported items into loading and unloading items
			List<InternalItemData> itemDataList = importer.getItemList();
			
			for (InternalItemData iD : itemDataList) {
				itemList.add(iD.createLoadingItem(importer.getDataManager()));
				if(iD.getUnloadingLocation().length() > 0)
					itemList.add(iD.createUnLoadingItem(importer.getDataManager()));
			}
		}
		
		// Pre-Sort items for logical order (ascending order location index)
		Collections.sort(itemList, new Comparator<Item>() {
			@Override
			public int compare(Item arg0, Item arg1) {
				return arg0.loadingLoc - arg1.loadingLoc;
			}
		});
		
		return new XFLPModel(itemList.toArray(new Item[0]), containerTypeList.toArray(new Container[0]), parameter);
	}

	/**
	 * Uses the last planned solution and turns it
	 * into a report representation.
	 * 
	 * All route plan informations can be akquired by this report.
	 * 
	 * @return A report data structure with detailed information about the route plan or null if no solution was calculated.
	 */
	public LPReport getReport() {
		if(lastSolution != null)
			return lastSolution.getReport();
		
		return null;
	}

	/**
	 * Akquire a depot data object to insert data for
	 * a new depot. The next call of this method will
	 * finalize the before akquired depot data object. 
	 * 
	 * @return Depot data object
	 */
	public ItemData addItem() {
		return importer.getItemData();
	}

	/**
	 * Akquire a vehicle data object to insert data for
	 * a new vehicle. The next call of this method will
	 * finalize the before akquired vehicle data object.
	 * 
	 * The call of this method means, that default vehicle
	 * parameters are not valid any longer. In this case they 
	 * have to be inserted in such a vehicle data object.
	 * 
	 * @return Container data object
	 */
	public ContainerData addContainer() {
		return importer.getContainerData();
	}

	/**
	 * Clears all added depots.
	 */
	public void clearItems() {
		importer.clearItems();
	}

	/**
	 * Removes all inserted vehicles and reset the planning parameters to default.
	 * (See setCapacity() and setMaxRouteDuration() )
	 */
	public void clearContainers() {
		importer.clearContainers();
	}

	/**
	 * All parameters are reset to default values (in most cases to false)
	 */
	public void clearParameters() {
		parameter.clear();
	}

	/**
	 * With this method a user can place a specifed status monitor object, where
	 * news from the optimization are communicated.
	 * 
	 * A full transparent information flow is not given, because the loss of speed is huge.
	 * 
	 * @param monitor User defined monitor object
	 */
	public void setStatusMonitor(StatusMonitor monitor) {
		statusManager.addObserver(monitor);
	}
}
