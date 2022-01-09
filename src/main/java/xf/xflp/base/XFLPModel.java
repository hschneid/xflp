package xf.xflp.base;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.monitor.StatusManager;


/** 
 * Copyright (c) 2012-2022 Holger Schneider
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
 * @author hschneid
 */
public class XFLPModel {

	protected Item[] items;

	/* Only one object per container type, solution will copy these objects */
	protected final Container[] containerTypes;

	protected final XFLPParameter parameter;
	private StatusManager statusManager;

	/* Result objects */
	private Container[] containers = new Container[0];
	private Item[] unplannedItems = new Item[0];

	/**
	 * Initialize an optimization model object with the given input data. It contains the general
	 * parameter for all optimization procedures. It holds no solution information.
	 */
	public XFLPModel(Item[] items, Container[] containerTypes, XFLPParameter parameter, StatusManager statusManager) {
		this.items = items;
		this.containerTypes = containerTypes;
		this.parameter = parameter;
		this.statusManager = statusManager;
	}

	public Container[] getContainerTypes() {
		return containerTypes;
	}

	public XFLPParameter getParameter() {
		return parameter;
	}

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

	public void setItems(Item[] items) {
		this.items = items;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}
}
