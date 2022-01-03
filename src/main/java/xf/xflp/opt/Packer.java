package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.exception.XFLPException;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 * Construction heuristics are packers, which create a solution out of an model.
 *
 * In some cases a planning strategy can be given, which influences the way of construction.
 */
public interface Packer {

    void execute(XFLPModel model) throws XFLPException;
}
