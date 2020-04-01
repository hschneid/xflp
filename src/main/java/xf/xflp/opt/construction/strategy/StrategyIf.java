package xf.xflp.opt.construction.strategy;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;

import java.util.List;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public interface StrategyIf {

	/**
	 * 
	 * @param item
	 * @param container
	 * @param posList
	 * @return
	 */
	public Position choose(Item item, Container container, List<Position> posList);
}
