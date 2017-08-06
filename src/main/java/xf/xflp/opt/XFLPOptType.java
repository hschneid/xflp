package xf.xflp.opt;

import xf.xflp.opt.construction.ZSingleBinPacker;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * This enumeration holds all for the user accessible optimization
 * methods.
 * 
 * @author hschneid
 *
 */
public enum XFLPOptType {
	
	SINGLE_PACKER_ADD_REMOVE(ZSingleBinPacker.class);
	
	private Class<? extends XFLPBase> clazz; 
	
	/**
	 * 
	 * @param clazz
	 */
	private XFLPOptType(Class<? extends XFLPBase> clazz) {
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
