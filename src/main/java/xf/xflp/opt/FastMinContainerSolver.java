package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.NContainerNTypeAddPacker;
import xf.xflp.opt.construction.onetype.NContainerOneTypeAddPacker;
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
 * Goal: Place all items into a some instances of container types. The number of containers
 *       shall be minimal.
 */
public class FastMinContainerSolver extends XFLPBase {

    private final NContainerOneTypeAddPacker oneTypeAddPacker = new NContainerOneTypeAddPacker();
    private final NContainerNTypeAddPacker nTypeAddPacker = new NContainerNTypeAddPacker();

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
                throw new UnsupportedOperationException("Currently add/removing and single container types is not supported");
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
