package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypeAddPacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;
import xf.xflp.opt.grasp.ItemOrderRandomSearch;
import xf.xflp.report.LoadType;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 * Goal: Create a solution with minimal number of unplanned items.
 *
 * Heuristic: Shuffling of items order. Neighborhood search is very heavy even for small problems.
 *
 */
public class BestFixedContainerSolver extends XFLPBase {

    private final OneContainerOneTypePacker oneTypePacker = new OneContainerOneTypePacker();
    private final OneContainerOneTypeAddPacker oneTypeAddPacker = new OneContainerOneTypeAddPacker();
    private final OneContainerNTypeAddPacker nTypeAddPacker = new OneContainerNTypeAddPacker();

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        if(isOnlyAddingItems(model)) {
            if(model.getContainerTypes().length > 1) {
                new ItemOrderRandomSearch(nTypeAddPacker).execute(model);
            } else {
                new ItemOrderRandomSearch(oneTypeAddPacker).execute(model);
            }
        } else {
            if(model.getContainerTypes().length > 1) {
                throw new UnsupportedOperationException("Currently add/removing and multiple container types is not supported");
            } else {
                new ItemOrderRandomSearch(oneTypePacker).execute(model);
            }
        }
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
