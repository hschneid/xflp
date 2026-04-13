package xf.xflp.opt.construction.onetype;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.Packer;
import xf.xflp.opt.construction.strategy.Strategy;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This packer puts the items into one container with one container type
 * using a Biased Randomized heuristic (geometric distribution for item selection).
 * Items will only be added to a container.
 *
 * @author hschneid
 */
public class OneContainerOneTypeAddPackerRand implements Packer {

	@Override
	public void execute(XFLPModel model) throws XFLPException {
		Container container = model.getContainerTypes()[0].newInstance();

		Strategy strategy = model.getParameter().getPreferredPackingStrategy();

		List<Item> unplannedItemList = new SingleBinAddHeuristicBiasRandom(strategy, model.getStatusManager(), model.getParameter(), model.getRandom())
				.createLoadingPlan(
						Arrays.asList(model.getItems()),
						container
				);

		// Put result into model
		model.setContainers(new Container[]{container});
		model.setUnplannedItems(unplannedItemList.toArray(new Item[0]));
	}
}

