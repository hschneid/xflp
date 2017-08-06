package xf.xflp.base.problem;

import java.util.Arrays;
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
public class LoadBearingCheck {

	private float[] freeLoadBearing;
	private float[] openWeight;

	/**
	 * 
	 * @return
	 */
	public boolean checkLoadBearing(List<Item> ceilingItems, ZItemGraph graph) {
		freeLoadBearing = new float[graph.size()];
		openWeight = new float[graph.size()];
		Arrays.fill(freeLoadBearing, -1);
		Arrays.fill(openWeight, -1);

		// F�r alle ceiling items
		for (Item ceilingItem : ceilingItems) {
			freeLoadBearing[ceilingItem.index] = ceilingItem.stackingWeightLimit;
			openWeight[ceilingItem.index] = ceilingItem.weight;

			if(!checkLoadBearingRecurive(ceilingItem, graph))
				return false;
		}

		return true;
	}

	/**
	 * Recursive function to check the bearing restriction for the current item.
	 * 
	 * In same step the weight of the item and the above items are shared to the
	 * items below of this item.
	 * 
	 * @param item Current item to check
	 * @param graph The stacking graph, to get the items below
	 * @return True if bearing restriction is hold, False if the stack is invalid for bearing restriction.
	 */
	@SuppressWarnings("unchecked")
	private boolean checkLoadBearingRecurive(Item item, ZItemGraph graph) {
		// Fetch unshared weight from items above
		float ow = openWeight[item.index];
		// If no weigth was shared, initialise
		if(ow == -1) {
			openWeight[item.index] = item.weight;
			ow = item.weight;
		}

		// For all lower items of current item
		List<?>[] lowerItemData = graph.getItemsBelowWithCutArea(item);
		List<Item> lowerItems = (List<Item>) lowerItemData[0];
		List<Float> cutArea = (List<Float>) lowerItemData[1];
		for (int i = 0; i < lowerItems.size(); i++) {
			Item lowerItem = lowerItems.get(i);

			// Initialise lazy
			if(openWeight[lowerItem.index] == -1)
				openWeight[lowerItem.index] = lowerItem.weight;
			if(freeLoadBearing[lowerItem.index] == -1)
				freeLoadBearing[lowerItem.index] = lowerItem.stackingWeightLimit;

			// If all weight was shared, terminate
			if(ow == 0)
				break;

			// The shared weight is relative to the common area space of touching plane
			float moveWeight = ow * cutArea.get(i);

			// Increase the share weight of underlying items for next iteration
			openWeight[item.index] -= moveWeight;
			openWeight[lowerItem.index] += moveWeight;
			freeLoadBearing[lowerItem.index] -= moveWeight;

			// Check the bearing restriction
			if(freeLoadBearing[lowerItem.index] < 0)
				return false;
		}

		// Call this method for all item below of current item
		for (int i = 0; i < lowerItems.size(); i++) {
			Item lowerItem = lowerItems.get(i);

			// Verfahre rekursiv mit darunterliegenden Items, wenn sie nicht auf dem Boden stehen 
			if(lowerItem.z > 0 && !checkLoadBearingRecurive(lowerItem, graph))
				return false;
		}

		return true;
	}
}
