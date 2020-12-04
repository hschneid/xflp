package xf.xflp.opt.construction.strategy;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.base.problem.RotatedPosition;

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
 * This type of strategy chooses position, which width is a good proportion of container width.
 * As alternative strategy it uses the TouchingPerimeter
 *
 */
public class WidthProportionFactor implements StrategyIf {

	@Override
	public Position choose(Item item, Container container, List<Position> posList) {
		if(posList == null || posList.size() == 0) {
			throw new IllegalStateException("List of positions must be not empty or null.");
		}

		if(posList.size() == 1) {
			return posList.get(0);
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

	private int getDeviationOfProportion(Item i, Position pos, Container container) {
		int conWidth = container.getWidth();
		int itemWidth =  (pos instanceof RotatedPosition) ? i.l : i.w;

		float proportion = itemWidth / conWidth;
		int bestProportion = (int) proportion;
		float deviation = Math.abs(proportion - bestProportion);
		int roundDeviation = Math.round(deviation * 10);

		return roundDeviation;
	}
}
