package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.opt.construction.ZMultiBinAddPacker5;

/**
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This enumeration holds all for the user available optimization
 * methods.
 *
 * @author hschneid
 *
 */
public class FastMinContainerSolver extends XFLPBase {

    private final ZMultiBinAddPacker5 packer = new ZMultiBinAddPacker5();

    @Override
    public void execute(XFLPModel model) {
        packer.execute(model);
    }
}
