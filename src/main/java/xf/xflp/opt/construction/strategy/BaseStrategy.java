package xf.xflp.opt.construction.strategy;

import xf.xflp.base.container.ComplexContainer;
import xf.xflp.base.container.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.exception.XFLPException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** 
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public abstract class BaseStrategy {

	public abstract Position choose(Item item, Container container, List<Position> posList) throws XFLPException;

	protected List<Position> getPositionWithMinValue(List<Position> posList, Function<Position, Float> positionValue) {
		if(posList == null) {
			return new ArrayList<>();
		}

		if(posList.size() <= 1) {
			return posList;
		}

		float[] distances = new float[posList.size()];
		for (int i = distances.length - 1; i >= 0; i--) {
			distances[i] = positionValue.apply(posList.get(i));
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
		return filteredPositions;
	}
}
