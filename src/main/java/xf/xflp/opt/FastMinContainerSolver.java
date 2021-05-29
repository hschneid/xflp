package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.opt.construction.onetype.NContainerOneTypeAddPacker;

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
public class FastMinContainerSolver extends XFLPBase {

    private final NContainerOneTypeAddPacker packer = new NContainerOneTypeAddPacker();

    @Override
    public void execute(XFLPModel model) {
        packer.execute(model);
    }
}
