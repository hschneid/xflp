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
 * 
 * @author hschneid
 *
 * The strategy is used in construction heuristic to choose best possible insert position.
 *
 * This type of strategy chooses the position with the highest touching perimeter value.
 * This value describes the contact with walls or other items.
 * High value means much contact.
 *
 * If multiple positions have the highest touching perimeter value, then the
 * strategy HighestLowerLeft is used to decide.
 *
 */
public class TouchingPerimeter extends BaseStrategy {

	private final HighestLowerLeft fallbackStrategy = new HighestLowerLeft();

	@Override
	public Position choose(Item item, Container container, List<Position> posList) {
		if(posList == null || posList.isEmpty()) {
			throw new IllegalStateException("List of positions must be not empty or null.");
		}

		List<Position> filteredPositions = getPositionWithMinValue(
				posList,
				(Position p) ->
						// Negative to find min value
						-container.getTouchingPerimeter(
								item,
								p,
								1,
								true,
								true
						)
		);

		// Return found position or check further
		if(filteredPositions.size() == 1) {
			return filteredPositions.get(0);
		} else if(filteredPositions.isEmpty()) {
			throw new IllegalStateException("There must be at least one position.");
		} else {
			return fallbackStrategy.choose(item, container, filteredPositions);
		}
	}
}
