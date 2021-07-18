package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.exception.XFLPException;
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

    private final OneContainerOneTypePacker packer = new OneContainerOneTypePacker();

    @Override
    public void execute(XFLPModel model) throws XFLPException {
        packer.setStrategy(model.getParameter().getPreferredPackingStrategy().getStrategy());
        packer.execute(model);
    }
}
