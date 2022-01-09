package xf.xflp.opt.construction.multitype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.Packer;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * This packer puts the items in a sequence for each container type into single container.
 * Items will only be added to a container.
 *
 * @author hschneid
 *
 */
public class NContainerNTypeAddPacker implements Packer {

	@Override
	public void execute(XFLPModel model) throws XFLPException {

		Strategy strategy = model.getParameter().getPreferredPackingStrategy();
		MultiBinAddHeuristic heuristic = new MultiBinAddHeuristic(strategy, model.getStatusManager());

		List<Container> containers = new ArrayList<>();
		List<Item> unplannedItems = Arrays.asList(model.getItems());
		while(!unplannedItems.isEmpty()) {
			// Create one container per type
			List<Container> newContainers = getContainers(model);
			// Try to insert items in containers
			List<Item> restItems = heuristic.createLoadingPlan(unplannedItems, newContainers);

			containers.addAll(newContainers);
			// Rest containers will go into next round
			unplannedItems = restItems;
		}

		// Put result into model
		model.setContainers(containers.toArray(new Container[0]));
		model.setUnplannedItems(unplannedItems.toArray(new Item[0]));
	}

	private List<Container> getContainers(XFLPModel model) {
		return Arrays.stream(model.getContainerTypes())
				.map(Container::newInstance)
				.collect(Collectors.toList());
	}
}
