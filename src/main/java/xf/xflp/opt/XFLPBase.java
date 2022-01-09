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
 */
public abstract class XFLPBase {

	/**
	 * 
	 * @param model Model contains items, container types, the resulting containers and rejected items
	 */
	public abstract void execute(XFLPModel model) throws XFLPException;
}
