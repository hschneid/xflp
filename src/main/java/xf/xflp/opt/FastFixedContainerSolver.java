package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.OneContainerNTypeAddPacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypeAddPacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;
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
 * This solver tries to create very fastly a reasonable solution.
 *
 * It uses only construction heuristics.
 *
 * Goal: All items should be packed into a single set of container types.
 *       If items are not fitting, then they will be placed in separate list. (unplanned)
 *
 */
public class FastFixedContainerSolver extends XFLPBase {

    private final OneContainerOneTypePacker oneTypePacker = new OneContainerOneTypePacker();
    private final OneContainerOneTypeAddPacker oneTypeAddPacker = new OneContainerOneTypeAddPacker();
    private final OneContainerNTypeAddPacker nTypeAddPacker = new OneContainerNTypeAddPacker();

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        if(isOnlyAddingItems(model)) {
            if(model.getContainerTypes().length > 1) {
                nTypeAddPacker.execute(model);
            } else {
                oneTypeAddPacker.execute(model);
            }
        } else {
            if(model.getContainerTypes().length > 1) {
                throw new UnsupportedOperationException("Currently add/removing and multiple container types is not supported");
            } else {
                oneTypePacker.execute(model);
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
