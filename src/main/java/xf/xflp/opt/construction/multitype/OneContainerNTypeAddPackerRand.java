package xf.xflp.opt.construction.multitype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.Packer;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This packer puts the items for each container type into single container
 * using a Biased Randomized heuristic (geometric distribution for item selection).
 * Items will only be added to a container.
 *
 * @author hschneid
 */
public class OneContainerNTypeAddPackerRand implements Packer {

	@Override
	public void execute(XFLPModel model) throws XFLPException {
		Strategy strategy = model.getParameter().getPreferredPackingStrategy();

		// Create one container per type
		List<Container> containers = getContainers(model);
		// Try to insert items in containers using biased randomized heuristic
		List<Item> unplannedItems = new MultiBinAddHeuristicBiasRandom(strategy, model.getStatusManager(), model.getParameter())
				.createLoadingPlan(Arrays.asList(model.getItems()), containers);

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

