package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.opt.construction.onetype.OneContainerOneTypePacker;

/**
 * Copyright (c) 2012-present Holger Schneider
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

    private OneContainerOneTypePacker packer = new OneContainerOneTypePacker();

    @Override
    public void execute(XFLPModel model) {
        packer.execute(model);
    }
}
