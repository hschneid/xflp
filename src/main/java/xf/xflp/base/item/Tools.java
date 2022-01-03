package xf.xflp.base.item;

import util.collection.IndexedArrayList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class Tools {

	/**
	 *
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
	 * @return cut area size / root area size
	 */
	public static float getCutRatio(Item rootItem, Item cutItem) {
		float x = Math.min(rootItem.xw, cutItem.xw) - Math.max(rootItem.x, cutItem.x);
		float y = Math.min(rootItem.yl, cutItem.yl) - Math.max(rootItem.y, cutItem.y);

		return (x * y) / rootItem.size;
	}

	/**
	 * Returns all items, which are below the given item (root)
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
	 */
	public static List<Item> getItemsBelow(Item item, IndexedArrayList<Item> itemList) {
		List<Item> list = new ArrayList<>();

		if(item.z == 0)
			return list;

		for (Item it : itemList) {
			if (it == null)
				continue;

			if (it.zh == item.z &&
					it.xw > item.x && it.x < item.xw &&
					it.yl > item.y && it.y < item.yl)
				list.add(it);
		}

		return list;
	}

	/**
	 * 
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

	/**
	 * Recursive method
	 */
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

	/**
	 * Recursive method
	 *
	 * Returns all items, which are above the item (root) and stores them into found set
	 */
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
		}
	}

	/**
	 * Returns all items which are placed at the ground of container and which
	 * are below the given item (root)
	 */
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
