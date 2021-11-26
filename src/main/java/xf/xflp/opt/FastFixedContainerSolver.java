package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.exception.XFLPException;
import xf.xflp.opt.construction.multitype.OneContainerNTypePacker;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public class FastFixedContainerSolver extends XFLPBase {

    private final OneContainerOneTypePacker singlePacker = new OneContainerOneTypePacker();
    private final OneContainerNTypePacker multiPacker = new OneContainerNTypePacker();

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        if(model.getContainerTypes().length == 1) {
            singlePacker.setStrategy(model.getParameter().getPreferredPackingStrategy().getStrategy());
            singlePacker.execute(model);
        } else {
            multiPacker.setStrategy(model.getParameter().getPreferredPackingStrategy().getStrategy());
            multiPacker.execute(model);
        }
    }
}
