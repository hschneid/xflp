package xf.xflp.opt;

import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;
import xf.xflp.opt.grasp.SingleBinOptimizedPacker;

import java.lang.reflect.InvocationTargetException;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This enumeration holds all for the user available optimization
 * methods.
 * @author hschneid
 */
public enum XFLPOptType {
	
	SINGLE_CONTAINER_OPTIMIZER(SingleBinOptimizedPacker.class),
	FAST_FIXED_CONTAINER_PACKER(FastFixedContainerSolver.class),
	BEST_FIXED_CONTAINER_PACKER(BestFixedContainerSolver.class),
	FAST_MIN_CONTAINER_PACKER(FastMinContainerSolver.class),
	BEST_MIN_CONTAINER_PACKER(BestMinContainerSolver.class)
	;
	
	private final Class<? extends XFLPBase> clazz;

	XFLPOptType(Class<? extends XFLPBase> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Creates an instance of the chosen opt type class in clazz.
	 * 
	 * @return An object instance
	 */
	public XFLPBase createInstance() throws XFLPException {
		try {
			return (XFLPBase) Class.forName(clazz.getName()).getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "no copy of optimization procedure possible", e);
		}
	}
}
