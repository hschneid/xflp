package xf.xflp.base.problem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.collection.IndexedArrayList;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class Tools {

	/**
	 * 
	 * @param root
	 * @param itemList
	 * @return
	 */
	public static Set<Item> getAllCeilingItems(Item root, List<Item> itemList) {
		Set<Item> floorSet = getAllFloorItems(root, itemList);
		Set<Item> ceilSet = new HashSet<>();
		for (Item i : floorSet)
			getCeilingItems(i, itemList, ceilSet);

		return ceilSet;
	}

	/**
	 * Returns the area size ratio of the cut between root item and cut item to root items full area size
	 * 
	 * @param rootItem
	 * @param cutItem
	 * @return cut area size / root area size
	 */
	public static float getCutRatio(Item rootItem, Item cutItem) {
		float x = Math.min(rootItem.xw, cutItem.xw) - Math.max(rootItem.x, cutItem.x);
		float y = Math.min(rootItem.yl, cutItem.yl) - Math.max(rootItem.y, cutItem.y);

		return (x * y) / rootItem.size;
	}

	/**
	 * 
	 * @param root
	 * @param itemList
	 * @param foundSet
	 */
	public static void getItemsBelow(Item root, List<Item> itemList, Set<Item> foundSet) {
		if(root.z == 0)
			return;

		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			if(item == null)
				continue;

			if(foundSet.contains(item))
				continue;

			if(item.zh == root.z && 
					item.xw > root.x && item.x < (root.x + root.w) && 
					item.yl > root.y && item.y < (root.y + root.l) 
					) {
				foundSet.add(item);
			}
		}
	}

	/**
	 * 
	 * @param pos
	 * @param itemList
	 * @return
	 */
	public static List<Item> getItemsBelow(Position pos, Item upperItem, List<Item> itemList) {
		List<Item> list = new ArrayList<>();

		if(pos.getZ() == 0)
			return list;

		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			if(item == null)
				continue;

			if(item.zh == pos.getZ() && 
					item.xw > pos.getX() && item.x < (pos.getX() + upperItem.w) && 
					item.yl > pos.getY() && item.y < (pos.getY() + upperItem.l) 
					) {
				list.add(item);
			}
		}

		return list;
	}

	/**
	 * 
	 * @param item
	 * @param itemList
	 * @return
	 */
	public static List<Item> getItemsBelow(Item item, IndexedArrayList<Item> itemList) {
		List<Item> list = new ArrayList<>();

		if(item.z == 0)
			return list;

		for (int i = 0; i < itemList.size(); i++) {
			Item it = itemList.get(i);
			if(it == null)
				continue;

			if(it.zh == item.z && 
					it.xw > item.x && it.x < item.xw && 
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	/**
	 * 
	 * @param root
	 * @param itemList
	 * @param foundSet
	 */
	public static void getLowerItems(Item root, List<Item> itemList, Set<Item> foundSet) {
		if(root.z == 0)
			return;

		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			if(item == null)
				continue;

			if(foundSet.contains(item))
				continue;

			if(item.zh == root.z && 
					item.xw > root.x && item.x < (root.x + root.w) && 
					item.yl > root.y && item.y < (root.y + root.l) 
					) {
				foundSet.add(item);
				getLowerItems(item, itemList, foundSet);
			}
		}
	}

	public static void getFloorItems(Item root, List<Item> itemList, Set<Item> foundSet) {
		if(root.z == 0) {
			foundSet.add(root);
			return;
		}

		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			if(item == null)
				continue;

			if(item.zh == root.z && 
					item.xw > root.x && item.x < (root.x + root.w) && 
					item.yl > root.y && item.y < (root.y + root.l) 
					) {
				getFloorItems(item, itemList, foundSet);
			}
		}
	}

	public static void getCeilingItems(Item root, List<Item> itemList, Set<Item> foundSet) {
		boolean foundCeilingItem = false;
		for (int i = 0; i < itemList.size(); i++) {
			Item item = itemList.get(i);
			if(item == null)
				continue;

			if(item.z == root.zh && 
					item.xw > root.x && item.x < (root.x + root.w) && 
					item.yl > root.y && item.y < (root.y + root.l) 
					) {
				foundCeilingItem = true;
				getCeilingItems(item, itemList, foundSet);
			}
		}

		if(!foundCeilingItem) {
			foundSet.add(root);
			return;
		}
	}

	public static Set<Item> getAllFloorItems(Item root, List<Item> itemList) {
		Set<Item> floorSet = new HashSet<>();
		Set<Item> fSet = new HashSet<>();
		Set<Item> ceilSet = new HashSet<>();

		getFloorItems(root, itemList, fSet);

		do {
			floorSet.clear();
			floorSet.addAll(fSet);
			fSet.clear();
			ceilSet.clear();

			for (Item i : floorSet)
				getCeilingItems(i, itemList, ceilSet);

			for (Item i : ceilSet)
				getFloorItems(i, itemList, fSet);
		} while(floorSet.size() != fSet.size());

		return floorSet;
	}

}
