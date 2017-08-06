package xf.xflp.base.problem;

import java.util.ArrayList;
import java.util.List;

import util.Copyable;
import util.collection.LPListMap;
import util.collection.SetIndexArrayList;


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

	private final SetIndexArrayList<ZItemGraph.Entry> lowerList = new SetIndexArrayList<>();
	private final SetIndexArrayList<List<Item>> upperList = new SetIndexArrayList<>();

	/**
	 * Adds a new item into Z-Graph. Due to linkage to interal data structure, the effort of adding
	 * is O(n+m)
	 * 
	 * @param newItem
	 * @param itemList
	 * @param zMap
	 */
	public void add(Item newItem, List<Item> itemList, LPListMap<Integer, Integer> zMap) {
		// Lower
		{
			List<Item> lowerItems = searchItemsBelow(newItem, itemList, zMap.get(newItem.z));
			Entry e = new Entry(newItem, lowerItems);
			lowerList.set(newItem.index, e);

			// Update of lower items with new upper item
			for (Item lowerItem : lowerItems) {
				if(upperList.get(lowerItem.index) == null)
					upperList.set(lowerItem.index, new ArrayList<Item>());
				upperList.get(lowerItem.index).add(newItem);
			}
		}

		// Upper
		{
			List<Item> upperItems = searchItemsAbove(newItem, itemList, zMap.get(newItem.zh));
			upperList.set(newItem.index, upperItems);

			// Update upper items with new lower item, which means new cut area ratio
			for (Item upperItem : upperItems) {
				Entry e = lowerList.get(upperItem.index);
				e.lowerItemList.add(newItem);
				e.update();
			}
		}
	}

	/**
	 * 
	 * @param item
	 */
	public void remove(Item item) {
		// Entferne Item aus lower items
		if(lowerList.get(item.index) != null)
			for (Item lowerItem : lowerList.get(item.index).lowerItemList)
				upperList.get(lowerItem.index).remove(item);		
		// Entferne Item aus upper items
		if(upperList.get(item.index) != null)
			for (Item upperItem : upperList.get(item.index))
				lowerList.get(upperItem.index).lowerItemList.remove(item);		

		// Entferne lowerList-Eintrag
		lowerList.remove(item.index);
		// Entferne upperList-Eintrag
		upperList.remove(item.index);
	}

	/**
	 * 
	 */
	public void clear() {
		lowerList.clear();
		upperList.clear();
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return upperList.size();
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	public List<Item> getItemsBelow(Item item) {
		return lowerList.get(item.index).lowerItemList;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
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

	/**
	 * 
	 * @param item
	 * @return
	 */
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
	 * @param zList List of all item indeces where zh = item.z
	 * @return List of all items below new item
	 */
	private List<Item> searchItemsBelow(Item item, List<Item> itemList, List<Integer> zList) {
		List<Item> list = new ArrayList<>();

		if(item.z == 0)
			return list;

		for (int i = 0; i < zList.size(); i++) {
			int zItemIdx = zList.get(i);
			Item it = itemList.get(zItemIdx);

			if(it.zh == item.z && 
					it.xw > item.x && it.x < item.xw && 
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	/**
	 * 
	 * @param item
	 * @param itemList
	 * @param zList
	 * @return List of all items above new item
	 */
	private List<Item> searchItemsAbove(Item item, List<Item> itemList, List<Integer> zList) {
		List<Item> list = new ArrayList<>();

		if(zList == null)
			return list;

		for (int i = 0; i < zList.size(); i++) {
			int zItemIdx = zList.get(i);
			Item it = itemList.get(zItemIdx);

			if(it.z == item.zh && 
					it.xw > item.x && it.x < item.xw && 
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	/**
	 * 
	 * @param item
	 * @param found
	 */
	private void searchBaseRecursive(Item item, boolean[] found) {
		if(lowerList.get(item.index) == null)
			return;

		for (Item i : lowerList.get(item.index).lowerItemList) {
			if(i.z == 0)
				found[i.index] = true;
			else
				searchBaseRecursive(i, found);
		}
	}

	/**
	 * 
	 * @param item
	 * @param found
	 */
	private void searchCeilRecursive(Item item, boolean[] found) {
		if(upperList.get(item.index) == null || upperList.get(item.index).size() == 0)
			found[item.index] = true;
		else 
			for (Item i : upperList.get(item.index))
				searchCeilRecursive(i, found);
	}


	/**
	 * 
	 * @author hschneid
	 *
	 */
	class Entry implements Copyable<Entry>{
		public final Item item;
		public final List<Item> lowerItemList;
		public final List<Float> cutRatioList;
		public final Object[] itemRatioArr;

		/**
		 * 
		 * @param item
		 * @param lowerItemList
		 * @param cutRatioList
		 */
		public Entry(Item item, List<Item> lowerItemList) {
			this.item = item;
			this.cutRatioList = new ArrayList<>();
			this.lowerItemList = lowerItemList;
			this.itemRatioArr = new Object[]{this.lowerItemList, this.cutRatioList};

			update();
		}

		/**
		 * 
		 */
		public void update() {
			float[] bCuts = new float[lowerItemList.size()];
			float sum = 0;
			for (int i = 0; i < lowerItemList.size(); i++) {
				bCuts[i] = Tools.getCutRatio(item, lowerItemList.get(i));
				sum += bCuts[i];
			}
			// Anpassen der Anteile auf 100%
			for (int i = 0; i < lowerItemList.size(); i++)
				cutRatioList.add(bCuts[i] * (1f / sum));
		}

		@Override
		public Entry copy() {
			throw new IllegalStateException();
		}
	}

}
