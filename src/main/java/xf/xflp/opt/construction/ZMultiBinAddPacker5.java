package xf.xflp.opt.construction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.XFLPBase;


/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * The packer ZMultiBinAddPacker5 plans a set of items into a set of containers. It uses only one
 * container type. All items will be packed into containers, because the number of containers is
 * unlimited. The ordering of items is predefined in the first step and will not changed during the
 * pack process.
 * 
 * The packer considers only items which will be added to a container. The adding and removing will
 * not be provided.
 * 
 * @author hschneid
 *
 */
public class ZMultiBinAddPacker5 extends XFLPBase {

	/* This comparator defines the ordering of items before planning process */
	private Comparator<Item> itemOrderingComp = new Comparator<Item>() {
		@Override
		public int compare(Item a, Item b) {
			// First the item with high bearing
			if(a.stackingWeightLimit > b.stackingWeightLimit)
				return -1;
			if(a.stackingWeightLimit < b.stackingWeightLimit)
				return 1;
			// Second the highest items
			if(a.h < b.h)
				return -1;
			if(a.h > b.h)
				return 1;
			// Third the widest items (can be oriented)
			if(a.w > b.w)
				return -1;
			if(a.w < b.w)
				return 1;
			// Forth the longest items (can be oriented)
			if(a.l > b.l)
				return -1;
			if(a.l < b.l)
				return 1;
			// Fifth the stacking group
			// Items with equal measures should be packed in a row
			// if they have the same stacking group (better stack building)
			if(a.stackingGroup > b.stackingGroup)
				return -1;
			if(a.stackingGroup < b.stackingGroup)
				return 1;
			// Rest is equal
			return 0;
		}
	};
	
	private final ZSingleBinAddPacker singlePacker = new ZSingleBinAddPacker();

	/*
	 * (non-Javadoc)
	 * @see de.fhg.iml.vlog.xflp.opt.XFLPBase#execute(de.fhg.iml.vlog.xflp.base.XFLPModel)
	 */
	@Override
	public void execute(XFLPModel model) {
		Container containerType = model.getContainerTypes()[0];
		
		List<Container> containerList = new ArrayList<>();
		Item[] unpackedItems = model.getItems();
		
		// Initialisation - Sort items in specific order.
		int containerIdx = 0;
		Arrays.sort(unpackedItems, itemOrderingComp);

		while(unpackedItems.length > 0) {
			// Create new container
			Container currentContainer = new Container(containerType, containerType.getLifoImportance());
			currentContainer.setIndex(containerIdx++);
		
			// Try to pack all unplanned items into the current empty container. The order
			// of items is untouched by this planning. Each unplanned item will be checked.
			unpackedItems = singlePacker.createLoadingPlan(unpackedItems, currentContainer);
			
			if(currentContainer.getNbrOfLoadedThings() > 0)
				containerList.add(currentContainer);
		}
		
		// Write created containers to model. There are no unplanned items.
		model.setContainers(containerList.toArray(new Container[0]));
	}
}
