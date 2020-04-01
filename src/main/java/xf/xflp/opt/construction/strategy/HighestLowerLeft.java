package xf.xflp.opt.construction.strategy;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;

import java.util.ArrayList;
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
 * The strategy is used in construction heuristic to choose best possible insert position.
 *
 * This type of strategy chooses the highest, most left (width) and most decent (length).
 *
 */
public class HighestLowerLeft implements StrategyIf {

	@Override
	public Position choose(Item item, Container container, List<Position> posList) {
		if(posList == null || posList.size() == 0) {
			throw new IllegalStateException("List of positions must be not empty or null.");
		}

		float[] distances = new float[posList.size()];
		for (int i = distances.length - 1; i >= 0; i--) {
			distances[i] = getDistance(posList.get(i));
		}

		float minValue = Float.MAX_VALUE;
		for (int i = posList.size() - 1; i >= 0; i--) {
			minValue = Math.min(minValue, distances[i]);
		}

		// Search all positions with max value
		List<Position> filteredPositions = new ArrayList<>();
		for (int i = distances.length - 1; i >= 0; i--) {
			if(distances[i] == minValue) {
				filteredPositions.add(posList.get(i));
			}
		}

		if(filteredPositions.size() == 0) {
			throw new IllegalStateException("There must be at least one position.");
		}

		return filteredPositions.get(0);
	}

	private float getDistance(Position p) {
		if(p == null) {
			return Float.MAX_VALUE;
		}
		return (float)Math.pow((p.getX()*p.getX()) + (p.getY()*p.getY()) + (p.getZ()*p.getZ()), 0.5);
	}
}
