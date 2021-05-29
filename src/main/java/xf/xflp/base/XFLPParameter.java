package xf.xflp.base;

import xf.xflp.opt.construction.strategy.Strategy;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class XFLPParameter {

	private float lifoImportance = 0f;
	private int maxNbrOfContainer = 1;
	private Strategy preferredPackingStrategy = Strategy.HIGH_LOW_LEFT;

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public float getLifoImportance() {
		return lifoImportance;
	}

	public void setLifoImportance(float lifoImportance) {
		this.lifoImportance = lifoImportance;
	}

	public Strategy getPreferredPackingStrategy() {
		return preferredPackingStrategy;
	}

	public void setPreferredPackingStrategy(Strategy preferredPackingStrategy) {
		this.preferredPackingStrategy = preferredPackingStrategy;
	}

	public int getMaxNbrOfContainer() {
		return maxNbrOfContainer;
	}

	public void setMaxNbrOfContainer(int maxNbrOfContainer) {
		this.maxNbrOfContainer = maxNbrOfContainer;
	}
}
