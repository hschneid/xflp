package xf.xflp.opt.construction.strategy;

import xf.xflp.base.problem.ComplexContainer;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;

import java.util.List;

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
 * The strategy is used in construction heuristic to choose best possible insert position.
 *
 * This type of strategy chooses the highest, most left (width) and most decent (length).
 *
 */
public class HighestLowerLeft extends BaseStrategy {

	@Override
	public Position choose(Item item, ComplexContainer container, List<Position> posList) throws XFLPException {
		if(posList == null || posList.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "List of positions must be not empty or null.");
		}

		List<Position> filteredPositions = getPositionWithMinValue(
				posList,
				this::getDistance
		);

		if(filteredPositions.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "There must be at least one position.");
		}

		return filteredPositions.get(0);
	}

	float getDistance(Position p) {
		if(p == null) {
			return Float.MAX_VALUE;
		}
		return (float)Math.pow((p.getX() * p.getX()) + (p.getY() * p.getY()) + (p.getZ() * p.getZ()), 0.5);
	}
}
