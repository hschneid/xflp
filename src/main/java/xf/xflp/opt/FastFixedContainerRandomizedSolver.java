package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPacker;
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPackerRand;
import xf.xflp.opt.construction.onetype.OneContainerOneTypeAddPacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;
import xf.xflp.opt.construction.onetype.SingleBinAddHeuristic;
import xf.xflp.opt.construction.onetype.SingleBinAddHeuristicBiasRandom;

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
public class FastFixedContainerRandomizedSolver extends XFLPBase {

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

        Container[] bestContainers;
        Item[] bestUnplannedItems;

        executeLoadPlanningStatic(model, multiType);
        long bestVolume = getLoadedVolume(model);
        bestContainers = model.getContainers();
        bestUnplannedItems = model.getUnplannedItems();

        // Early exit, when everything fits
        if(model.getUnplannedItems().length == 0) {
            return;
        }

        //System.out.println("INIT  - " + bestVolume);

        for (int i = 0; i < N_ITERATIONS; i++) {
            // Execute one randomized iteration (packer resets items and creates fresh containers)
            executeLoadPlanningRandomized(model, multiType);

            // Calculate total loaded volume of this iteration
            long loadedVolume = getLoadedVolume(model);
            //System.out.println("ITER " + i + " - " + loadedVolume + " - " + bestVolume);

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

    private static void executeLoadPlanningStatic(XFLPModel model, boolean multiType) {
        if (multiType) {
            new OneContainerNTypeAddPacker().execute(model);
        } else {
            new OneContainerOneTypeAddPacker(new SingleBinAddHeuristic(model)).execute(model);
        }
    }

    private static void executeLoadPlanningRandomized(XFLPModel model, boolean multiType) {
        if (multiType) {
            new OneContainerNTypeAddPackerRand().execute(model);
        } else {
            new OneContainerOneTypeAddPacker(new SingleBinAddHeuristicBiasRandom(model)).execute(model);
        }
    }

    private static long getLoadedVolume(XFLPModel model) {
        long loadedVolume = 0;
        for (Container c : model.getContainers()) {
            loadedVolume += c.getLoadedVolume();
        }
        return loadedVolume;
    }
}
