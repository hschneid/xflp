package xf.xflp.opt.construction.strategy;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.position.PositionCandidate;
import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;

import java.util.List;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
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
 * This type of strategy chooses with maximal priority the highest and secondary the
 * most left (width) and most decent (length) position.
 *
 */
public class HighestLowerLeft extends BaseStrategy {

	@Override
	public PositionCandidate choose(Item item, Container container, List<PositionCandidate> candidates) throws XFLPException {
		if(candidates == null || candidates.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "List of positions must be not empty or null.");
		}

		List<PositionCandidate> filteredPositions = getPositionWithMinValue(
				candidates,
				this::getDistanceZ
		);

		if(filteredPositions.size() > 1) {
			filteredPositions = getPositionWithMinValue(
					filteredPositions,
					this::getDistance
			);
		}

		if(filteredPositions.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "There must be at least one position.");
		}

		return filteredPositions.get(0);
	}

	float getDistance(PositionCandidate candidate) {
		if(candidate == null) {
			return Float.MAX_VALUE;
		}

		Position p = candidate.position;
		return (float)Math.pow(
				(p.getX() * p.getX()) +
						(p.getY() * p.getY()) +
						(p.getZ() * p.getZ()), 0.5
		);
	}

	float getDistanceZ(PositionCandidate p) {
		return p == null ? Float.MAX_VALUE : (float) p.position.z * -1;
	}
}
