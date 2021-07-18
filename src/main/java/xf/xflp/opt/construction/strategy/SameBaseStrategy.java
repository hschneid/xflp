package xf.xflp.opt.construction.strategy;

import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.exception.XFLPException;
import xf.xflp.exception.XFLPExceptionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This strategy is created by transport business case, where mostly rectangular boxes
 * are stacked. Main focus is on identifying open stacks with same or smaller base. So
 * the height is used most properly.
 *
 * Prerequisites:
 *  - 1 item can be placed only on 1 item. In reality this happens if items have special shoulder-feet-groups.
 *  - The items are sorted that similar items are packed in stream. So, open stacks are filled with same base items directly,
 *    without polluting a stack with smaller items.
 *
 * @author hschneid
 *
 */
public class SameBaseStrategy extends BaseStrategy {

	private final HighestLowerLeft highLow = new HighestLowerLeft();
	private final WidthProportionFactor widthProportion = new WidthProportionFactor();

	@Override
	public Position choose(Item item, Container container, List<Position> posList) throws XFLPException {
		if(posList == null || posList.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "List of positions must be not empty or null.");
		}

		// Is there a stack, where this item has same base
		Position sameBasePosition = checkSameBaseStack(item, container, posList);
		if(sameBasePosition != null) {
			return sameBasePosition;
		}

		return findPosition(item, container, posList);
	}

	private Position checkSameBaseStack(Item item, Container container, List<Position> posList) {
		List<Position> sameBasePositions = new ArrayList<>(posList.size());
		List<Position> smallerBasePositions = new ArrayList<>(posList.size());

		findBasePositions(item, container, posList, sameBasePositions, smallerBasePositions);

		return chooseBasePosition(sameBasePositions, smallerBasePositions);
	}

	private void findBasePositions(Item item, Container container, List<Position> posList, List<Position> sameBasePositions, List<Position> smallerBasePositions) {
		int itemLength = Math.max(item.l, item.w);
		int itemWidth = Math.min(item.l, item.w);
		for (Position pos : posList) {
			if (pos.getZ() == 0)
				continue;

			// Search items below the position
			List<Integer> itemIdx = container.getBaseData().getZMap().get(pos.getZ());
			if (itemIdx == null)
				continue;


			for (Integer idx : itemIdx) {
				Item belowItem = container.getItems().get(idx);

				// Check, if this item is directly below the position
				if (belowItem.x == pos.getX() && belowItem.y == pos.getY() && belowItem.zh == pos.getZ()) {
					// Check, if this item has same base
					if (itemLength == Math.max(belowItem.l, belowItem.w) &&
							itemWidth == Math.min(belowItem.l, belowItem.w)) {
						sameBasePositions.add(pos);
					} else if (itemLength <= Math.max(belowItem.l, belowItem.w) &&
							itemWidth <= Math.min(belowItem.l, belowItem.w)) {
						smallerBasePositions.add(pos);
					}
				}
			}
		}
	}

	private Position chooseBasePosition(List<Position> sameBasePositions, List<Position> smallerBasePositions) {
		List<Position> foundPositions = (sameBasePositions.size() > 0) ? sameBasePositions : smallerBasePositions;
		if (foundPositions.size() == 0) {
			return null;
		}

		List<Position> minHighLowPositions = getPositionWithMinValue(
				foundPositions,
				highLow::getDistance
		);

		if (minHighLowPositions.size() == 0) {
			return null;
		}

		return minHighLowPositions.get(0);
	}

	private Position findPosition(Item item, Container container, List<Position> posList) throws XFLPException {
		/*List<Position> minTpPositions = getPositionWithMinValue(
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
		);*/

		List<Position> minHighLowPositions = getPositionWithMinValue(
				posList,
				highLow::getDistance
		);

		List<Position> filteredPositions = getPositionWithMinValue(
				minHighLowPositions,
				(Position p) -> widthProportion.getDeviationOfProportion(item, p, container)
		);

		if(filteredPositions.isEmpty()) {
			throw new XFLPException(XFLPExceptionType.ILLEGAL_STATE, "There must be at least one position.");
		}

		return filteredPositions.get(0);
	}
}
