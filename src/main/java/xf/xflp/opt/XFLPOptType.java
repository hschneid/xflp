package xf.xflp.opt;

import xf.xflp.opt.construction.DoubleContainerAddPacker;
import xf.xflp.opt.construction.SingleContainerAddPacker;
import xf.xflp.opt.construction.SingleContainerPacker;
import xf.xflp.opt.grasp.SingleBinOptimizedPacker;
import xf.xflp.opt.grasp.SingleBinRandomSearchPacker;

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
public enum XFLPOptType {
	
	SINGLE_CONTAINER_ADD_REMOVE_PACKER(SingleContainerPacker.class),
	SINGLE_CONTAINER_ADD_PACKER(SingleContainerAddPacker.class),
	DOUBLE_CONTAINER_ADD_PACKER(DoubleContainerAddPacker.class),
	SINGLE_CONTAINER_OPTIMIZER(SingleBinOptimizedPacker.class),
	SINGLE_CONTAINER_RANDOM_SEARCH(SingleBinRandomSearchPacker.class);
	
	private Class<? extends XFLPBase> clazz; 

	XFLPOptType(Class<? extends XFLPBase> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Creates an instance of the chosen opt type class in clazz.
	 * 
	 * @return An object instance
	 */
	public XFLPBase createInstance() {
		try {
			return (XFLPBase) Class.forName(clazz.getName()).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("no copy of optimization procedure possible");
		}
	}
}
