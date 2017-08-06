package xf.xflp.base;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;


/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * The XFLPModel holds all necessary input data. The
 * nodeArr contains all read nodes with their attitudes.
 * The metric can only be reached through this model for
 * getting distances or times between nodes. 
 * 
 * @author hschneid
 *
 */
public class XFLPModel {

	protected final Item[] items;

	/* Only one object per container type, solution will copy these objects */
	protected final Container[] containerTypes;

	protected final XFLPParameter parameter;
	
	/* Result objects */
	private Container[] containers = new Container[0];
	private Item[] unplannedItems = new Item[0];

	/**
	 * Initialize an optimization model object with the given input data. It contains the general
	 * parameter for all optimization procedures. It holds no solution information.
	 *
	 * @param nodeArr
	 * @param metric Metric that will be used to evaluate solutions
	 * @param optMetric Metric that will be used for optimization processes
	 * @param vehicle
	 * @param parameter
	 */
	public XFLPModel(Item[] items, Container[] containerTypes, XFLPParameter parameter) {
		this.items = items;
		this.containerTypes = containerTypes;
		this.parameter = parameter;
	}

	/**
	 * 
	 * @return
	 */
	public Container[] getContainerTypes() {
		return containerTypes;
	}

	/**
	 * 
	 * @return
	 */
	public XFLPParameter getParameter() {
		return parameter;
	}

	/**
	 * 
	 * @return
	 */
	public Item[] getItems() {
		return items;
	}

	/**
	 * @return the containers
	 */
	public final Container[] getContainers() {
		return containers;
	}

	/**
	 * @param containers the containers to set
	 */
	public final void setContainers(Container[] containers) {
		this.containers = containers;
	}

	/**
	 * @return the unplannedItems
	 */
	public final Item[] getUnplannedItems() {
		return unplannedItems;
	}

	/**
	 * @param unplannedItems the unplannedItems to set
	 */
	public final void setUnplannedItems(Item[] unplannedItems) {
		this.unplannedItems = unplannedItems;
	}
}
