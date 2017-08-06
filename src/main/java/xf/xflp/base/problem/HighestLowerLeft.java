package xf.xflp.base.problem;

import java.util.List;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class HighestLowerLeft implements StrategyIf {

	/*
	 * (non-Javadoc)
	 * @see de.fhg.iml.packer.strategy.StrategyIf#choose(de.fhg.iml.packer.ds.container.Stack, de.fhg.iml.packer.ds.container.Container, java.util.List)
	 */
	@Override
	public Position choose(Item item, Container container, List<Position> posList) {
		Position bestPos = null;
		float minY = Float.MAX_VALUE;
		float minX = Float.MAX_VALUE;
		float maxZ = 0;
		for (Position pos : posList) {
			if(pos.getZ() > maxZ) {
				maxZ = pos.getZ();
				minY = pos.getY();
				minX = pos.getX();
				bestPos = pos;
			} else if(pos.getZ() == maxZ && pos.getY() < minY) {
				maxZ = pos.getZ();
				minY = pos.getY();
				minX = pos.getX();
				bestPos = pos;
			} else if(pos.getZ() == maxZ && pos.getY() == minY && pos.getX() < minX) {
				maxZ = pos.getZ();
				minY = pos.getY();
				minX = pos.getX();
				bestPos = pos;
			}
		}

		return bestPos;
	}
}
