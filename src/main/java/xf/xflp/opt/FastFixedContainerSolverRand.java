package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.ItemPlacement;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPackerRand;
import xf.xflp.opt.construction.onetype.OneContainerOneTypeAddPackerRand;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;
import xf.xflp.report.LoadType;

import java.util.Map;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 * This solver creates a reasonable solution using a GRASP approach with a
 * Biased Randomized construction heuristic.
 *
 * The biased randomized packer is executed N_ITERATIONS times. Each iteration
 * uses a geometric distribution to probabilistically select the next item.
 * The best result (highest loaded volume across all containers) is kept.
 *
 * Goal: All items should be packed into a single set of container types.
 *       If items are not fitting, then they will be placed in separate list. (unplanned)
 */
public class FastFixedContainerSolverRand extends XFLPBase {

    private static final int N_ITERATIONS = 10;

    private final OneContainerOneTypePacker oneTypePacker = new OneContainerOneTypePacker();

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        if(isOnlyAddingItems(model)) {
            executeGrasp(model);
        } else {
            if(model.getContainerTypes().length > 1) {
                throw new UnsupportedOperationException("Currently add/removing and multiple container types is not supported");
            } else {
                oneTypePacker.execute(model);
            }
        }
    }

    private void executeGrasp(XFLPModel model) throws XFLPException {
        boolean multiType = model.getContainerTypes().length > 1;

        Container[] bestContainers = null;
        Item[] bestUnplannedItems = null;
        Map<Item, ItemPlacement> bestPlacements = null;
        long bestVolume = -1;

        for (int i = 0; i < N_ITERATIONS; i++) {
            // Execute one randomized iteration (packer resets items and creates fresh containers)
            if (multiType) {
                new OneContainerNTypeAddPackerRand().execute(model);
            } else {
                new OneContainerOneTypeAddPackerRand().execute(model);
            }

            // Calculate total loaded volume of this iteration
            long loadedVolume = getLoadedVolume(model);

            // Keep best result
            if (loadedVolume > bestVolume) {
                bestVolume = loadedVolume;
                bestContainers = model.getContainers();
                bestUnplannedItems = model.getUnplannedItems();
            }
        }

        // Set the best result into model
        model.setContainers(bestContainers);
        model.setUnplannedItems(bestUnplannedItems);
    }

    private static long getLoadedVolume(XFLPModel model) {
        long loadedVolume = 0;
        for (Container c : model.getContainers()) {
            loadedVolume += c.getLoadedVolume();
        }
        return loadedVolume;
    }

    private boolean isOnlyAddingItems(XFLPModel model) {
        for (Item item : model.getItems()) {
            if(item.loadingType == LoadType.UNLOAD) {
                return false;
            }
        }

        return true;
    }
}
