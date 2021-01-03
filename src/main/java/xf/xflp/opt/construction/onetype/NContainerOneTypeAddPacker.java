package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.XFLPBase;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * The packer NContainerOneTypeAddPacker plans a set of items into a set of containers. It uses only one
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
public class NContainerOneTypeAddPacker extends XFLPBase {

	private boolean isInit = false;
	private ZSingleBinAddPacker packer;

	/*
	 * (non-Javadoc)
	 * @see de.fhg.iml.vlog.xflp.opt.XFLPBase#execute(de.fhg.iml.vlog.xflp.base.XFLPModel)
	 */
	@Override
	public void execute(XFLPModel model) {
		init(model);

		List<Container> containerList = new ArrayList<>();
		List<Item> unpackedItems = Arrays.asList(model.getItems());
		
		int containerIdx = 0;
		while(unpackedItems.size() > 0 && hasMoreContainer(model, containerIdx)) {
			// Create new container
			Container currentContainer = createContainer(model);
			currentContainer.setIndex(containerIdx++);
		
			// Try to pack all unplanned items into the current empty container. The order
			// of items is untouched by this planning. Each unplanned item will be checked.
			unpackedItems = packer.createLoadingPlan(unpackedItems, currentContainer);
			
			containerList.add(currentContainer);
		}
		
		// Write created containers to model. There are no unplanned items.
		model.setContainers(containerList.toArray(new Container[0]));
	}

	private boolean hasMoreContainer(XFLPModel model, int containerIdx) {
		return containerIdx < model.getParameter().getMaxNbrOfContainer();
	}

	private Container createContainer(XFLPModel model) {
		Container containerType = model.getContainerTypes()[0];
		return new Container(containerType, containerType.getLifoImportance());
	}

	private void init(XFLPModel model) {
		if(!isInit) {
			isInit = true;
			Strategy strategy = model.getParameter().getPreferredPackingStrategy();
			packer = new ZSingleBinAddPacker(strategy);
		}
	}

}
