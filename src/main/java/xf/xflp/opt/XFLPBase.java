package xf.xflp.opt;

import xf.xflp.base.XFLPModel;
import xf.xflp.base.item.Item;
import xf.xflp.exception.XFLPException;
import xf.xflp.report.LoadType;

/** 
 * Copyright (c) 2012-2026 Holger Schneider
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

	protected boolean isOnlyAddingItems(XFLPModel model) {
		for (Item item : model.getItems()) {
			if(item.loadingType() == LoadType.UNLOAD) {
				return false;
			}
		}

		return true;
	}
}
