package xf.xflp.base.problem;

import util.collection.LPListMap;
import util.collection.SetIndexArrayList;

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
 */
public class ZItemGraph {

	private final SetIndexArrayList<ZItemGraphEntry> lowerList = new SetIndexArrayList<>();
	private final SetIndexArrayList<List<Item>> upperList = new SetIndexArrayList<>();

	/**
	 * Adds a new item into Z-Graph. Due to linkage to internal data structure,
	 * the effort of adding is O(n+m)
	 *
	 */
	public void add(Item newItem, List<Item> itemList, LPListMap<Integer, Integer> zMap) {
		// Lower
		{
			List<Item> lowerItems = searchItemsBelow(newItem, itemList, zMap.get(newItem.z));
			ZItemGraphEntry e = new ZItemGraphEntry(newItem, lowerItems);
			lowerList.set(newItem.index, e);

			// Update of lower items with new upper item
			for (Item lowerItem : lowerItems) {
				if(upperList.get(lowerItem.index) == null)
					upperList.set(lowerItem.index, new ArrayList<>());
				upperList.get(lowerItem.index).add(newItem);
			}
		}

		// Upper
		{
			List<Item> upperItems = searchItemsAbove(newItem, itemList, zMap.get(newItem.zh));
			upperList.set(newItem.index, upperItems);

			// Update upper items with new lower item, which means new cut area ratio
			for (Item upperItem : upperItems) {
				ZItemGraphEntry e = lowerList.get(upperItem.index);
				e.lowerItemList.add(newItem);
				e.update();
			}
		}
	}

	/*
	 * Remove an item from Z graph
	 */
	public void remove(Item item) {
		// Entferne Item aus lower items
		if(lowerList.get(item.index) != null) {
			List<Item> get = lowerList.get(item.index).lowerItemList;
			for (int i = get.size() - 1; i >= 0; i--) {
				Item lowerItem = get.get(i);
				upperList.get(lowerItem.index).remove(item);
			}
		}

		// Entferne Item aus upper items
		if(upperList.get(item.index) != null) {
			List<Item> get = upperList.get(item.index);
			for (int i = get.size() - 1; i >= 0; i--) {
				Item upperItem = get.get(i);
				lowerList.get(upperItem.index).lowerItemList.remove(item);
			}
		}

		// Entferne lowerList-Eintrag
		lowerList.remove(item.index);
		// Entferne upperList-Eintrag
		upperList.remove(item.index);
	}

	public void clear() {
		lowerList.clear();
		upperList.clear();
	}

	public int size() {
		return upperList.size();
	}

	public List<Item> getItemsBelow(Item item) {
		return lowerList.get(item.index).lowerItemList;
	}

	public List<Item> getItemsAbove(Item item) {
		return upperList.get(item.index);
	}

	/**
	 * Searches for any item in Z-Graph the base items with z = 0
	 * 
	 * Pre-Condition: All items, even the given one, are added into the Z-Graph
	 * 
	 * @param item
	 * @param itemList
	 * @return
	 */
	public List<Item> getBaseItems(Item item, List<Item> itemList) {
		boolean[] found = new boolean[lowerList.size()];
		searchBaseRecursive(item, found);

		List<Item> baseItems = new ArrayList<>();
		for (int i = 0; i < found.length; i++)
			if(found[i])
				baseItems.add(itemList.get(i));

		return baseItems;
	}

	/**
	 * Searches for any item in Z-Graph the ceiling items with no further upper items
	 * with regard to the base items of the given item
	 * 
	 * Pre-Condition: All items, even the given one, are added into the Z-Graph
	 * 
	 * @param item
	 * @param itemList
	 * @return
	 */
	public List<Item> getCeilItems(Item item, List<Item> itemList) {
		boolean[] foundBase = new boolean[lowerList.size()];
		boolean[] foundCeil = new boolean[lowerList.size()];
		// Search base items
		searchBaseRecursive(item, foundBase);

		// Search ceil items with base items
		for (int i = 0; i < foundBase.length; i++)
			if(foundBase[i]) 
				searchCeilRecursive(itemList.get(i), foundCeil);

		// Copy ceil items into list
		List<Item> ceilItems = new ArrayList<>();
		for (int i = 0; i < foundCeil.length; i++)
			if(foundCeil[i])
				ceilItems.add(itemList.get(i));

		return ceilItems;
	}

	public List<?>[] getItemsBelowWithCutArea(Item item) {
		return new List[] {
				lowerList.get(item.index).lowerItemList,
				lowerList.get(item.index).cutRatioList,
		};
	}

	/**
	 * 
	 * @param item New item
	 * @param itemList List of all packed items
	 * @param zList List of all item indices where zh = item.z
	 * @return List of all items below new item
	 */
	private List<Item> searchItemsBelow(Item item, List<Item> itemList, List<Integer> zList) {
		List<Item> list = new ArrayList<>();

		if(item.z == 0)
			return list;

		for (int i = zList.size()- 1; i >= 0; i--) {
			int zItemIdx = zList.get(i);
			Item it = itemList.get(zItemIdx);

			if(it.zh == item.z && 
					it.xw > item.x && it.x < item.xw && 
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	private List<Item> searchItemsAbove(Item item, List<Item> itemList, List<Integer> zList) {
		List<Item> list = new ArrayList<>();

		if(zList == null)
			return list;

		for (int i = zList.size() - 1; i >= 0; i--) {
			int zItemIdx = zList.get(i);
			Item it = itemList.get(zItemIdx);

			if(it.z == item.zh && 
					it.xw > item.x && it.x < item.xw && 
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	private void searchBaseRecursive(Item item, boolean[] found) {
		if(lowerList.get(item.index) == null)
			return;

		List<Item> lowerItemList = lowerList.get(item.index).lowerItemList;
		for (int j = 0, lowerItemListSize = lowerItemList.size(); j < lowerItemListSize; j++) {
			Item i = lowerItemList.get(j);
			if (i.z == 0)
				found[i.index] = true;
			else
				searchBaseRecursive(i, found);
		}
	}

	private void searchCeilRecursive(Item item, boolean[] found) {
		if(upperList.get(item.index) == null || upperList.get(item.index).isEmpty())
			found[item.index] = true;
		else {
			List<Item> get = upperList.get(item.index);
			for (int j = 0, getSize = get.size(); j < getSize; j++) {
				Item i = get.get(j);
				searchCeilRecursive(i, found);
			}
		}
	}

}
