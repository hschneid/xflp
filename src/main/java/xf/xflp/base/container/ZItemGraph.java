package xf.xflp.base.container;

import util.collection.LPListMap;
import util.collection.SetIndexArrayList;
import xf.xflp.base.item.PlacedItem;

import java.util.ArrayList;
import java.util.List;


/** 
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class ZItemGraph {

	private final SetIndexArrayList<ZItemGraphEntry> lowerList = new SetIndexArrayList<>();
	private final SetIndexArrayList<List<PlacedItem>> upperList = new SetIndexArrayList<>();

	/**
	 * Adds a new item into Z-Graph. Due to linkage to internal data structure,
	 * the effort of adding is O(n+m)
	 *
	 */
	public void add(PlacedItem newItem, List<PlacedItem> itemList, LPListMap<Integer, Integer> zMap) {
		// Lower
		{
			List<PlacedItem> lowerItems = searchItemsBelow(newItem, itemList, zMap.get(newItem.z));
			ZItemGraphEntry e = new ZItemGraphEntry(newItem, lowerItems);
			lowerList.set(newItem.index, e);

			// Update of lower items with new upper item
			for (PlacedItem lowerItem : lowerItems) {
				if(upperList.get(lowerItem.index) == null)
					upperList.set(lowerItem.index, new ArrayList<>());
				upperList.get(lowerItem.index).add(newItem);
			}
		}

		// Upper
		{
			List<PlacedItem> upperItems = searchItemsAbove(newItem, itemList, zMap.get(newItem.zh));
			upperList.set(newItem.index, upperItems);

			// Update upper items with new lower item, which means new cut area ratio
			for (PlacedItem upperItem : upperItems) {
				ZItemGraphEntry e = lowerList.get(upperItem.index);
				e.lowerItemList.add(newItem);
				e.update();
			}
		}
	}

	/*
	 * Remove an item from Z graph
	 */
	public void remove(PlacedItem item) {
		// Remove Item from lower items
		if(lowerList.get(item.index) != null) {
			List<PlacedItem> get = lowerList.get(item.index).lowerItemList;
			for (int i = get.size() - 1; i >= 0; i--) {
				PlacedItem lowerItem = get.get(i);
				upperList.get(lowerItem.index).remove(item);
			}
		}

		// Entferne Item aus upper items
		if(upperList.get(item.index) != null) {
			List<PlacedItem> get = upperList.get(item.index);
			for (int i = get.size() - 1; i >= 0; i--) {
				PlacedItem upperItem = get.get(i);
				lowerList.get(upperItem.index).lowerItemList.remove(item);
			}
		}

		// Entferne lowerList-Eintrag
		lowerList.remove(item.index);
		// Entferne upperList-Eintrag
		upperList.remove(item.index);
	}

	public int size() {
		return upperList.size();
	}

	public List<PlacedItem> getItemsBelow(PlacedItem item) {
		return lowerList.get(item.index).lowerItemList;
	}

	public List<PlacedItem> getItemsAbove(PlacedItem item) {
		return upperList.get(item.index);
	}

	/**
	 * 
	 * @param item New item
	 * @param itemList List of all packed items
	 * @param zList List of all item indices where zh = item.z
	 * @return List of all items below new item
	 */
	private List<PlacedItem> searchItemsBelow(PlacedItem item, List<PlacedItem> itemList, List<Integer> zList) {
		List<PlacedItem> list = new ArrayList<>();

		if(item.z == 0)
			return list;

		for (int i = zList.size() - 1; i >= 0; i--) {
			int zItemIdx = zList.get(i);
			PlacedItem it = itemList.get(zItemIdx);

			if(it.zh == item.z &&
					it.xw > item.x && it.x < item.xw &&
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	private List<PlacedItem> searchItemsAbove(PlacedItem item, List<PlacedItem> itemList, List<Integer> zList) {
		List<PlacedItem> list = new ArrayList<>();

		if(zList == null)
			return list;

		for (int i = zList.size() - 1; i >= 0; i--) {
			int zItemIdx = zList.get(i);
			PlacedItem it = itemList.get(zItemIdx);

			if(it.z == item.zh &&
					it.xw > item.x && it.x < item.xw &&
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}
}
