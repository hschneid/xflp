package xf.xflp.opt.grasp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import xf.xflp.base.problem.BigItem;
import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.construction.ZMultiBinAddPackerNoSort;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class BigItemBuilder {

	private Random r = new Random(1234);
	private ZMultiBinAddPackerNoSort p = new ZMultiBinAddPackerNoSort();
	private final Comparator<Object[]> comp = new Comparator<Object[]>() {
		@Override
		public int compare(Object[] o1, Object[] o2) {
			int v13 = (Integer)o1[0];
			int v14 = (Integer)o1[1];
			int v23 = (Integer)o2[0];
			int v24 = (Integer)o2[1];
			int v15 = (Integer)o1[2];
			int v25 = (Integer)o2[2];

			// Prim�r H�he
			int diff = v25 - v15;

			// Sekund�r H�llquader-Auslastung
			if(diff == 0) {
				float f1 = ((v14 / (float)v13)) * v14;
				float f2 = ((v24 / (float)v23)) * v24;

				diff = (int)((f2 - f1) * 1000f);
			}

			return diff;
		}
	};

	/**
	 * 
	 * @param items
	 * @param container
	 * @return
	 */
	public BigItemBuilderResult findBigItems(Item[] items, Container container) {
		int maxIdx = 0;
		for (int i = 0; i < items.length; i++)
			maxIdx = Math.max(maxIdx, items[i].externalIndex);
		maxIdx++;
		boolean[] valid = new boolean[maxIdx];
		List<BigItem> bigItemList = new ArrayList<>();

		int nbrSolutions = 20;
		for (int i = 0; i < 100; i++) {
			// Rein zuf�llige Sortierung der Items
			Item[][] pool = new Item[nbrSolutions][];
			for (int j = 0; j < nbrSolutions; j++)
				pool[j] = copy(shuffle(items));

			// Bewertung
			Container[][] cPool = new Container[nbrSolutions][];
			evaluate(container, pool, cPool);

			extractBigItems(items, cPool, bigItemList, valid);

			// W�hle Items ohne BigItem
			List<Item> restItems = new ArrayList<>();
			for (int k = 0; k < items.length; k++)
				if(!valid[items[k].externalIndex]) {
					items[k].reset();
					restItems.add(items[k]);
				}

			items = restItems.toArray(new Item[0]);
			System.out.println(i+" "+items.length);
		}

		// Erzeuge Result-Objekt
		return new BigItemBuilderResult(
				bigItemList.toArray(new BigItem[0]),
				items
				);
	}

	/**
	 * 
	 * @param items
	 * @param cPool
	 * @param bigItemList
	 * @param valid
	 */
	private void extractBigItems(Item[] items, Container[][] cPool, List<BigItem> bigItemList, boolean[] valid) {
		for (int tupelSize = 5; tupelSize >= 2; tupelSize--) {
			// Sequenziere Ergebnisse und priorisiere sie
			List<Object[]> tupel = sequenceTriplets(cPool, tupelSize, valid);
			Collections.sort(tupel, comp);

			// W�hle beste Tupel
			selectBigItems(valid, tupel, bigItemList);
		}
	}

	/**
	 * 
	 * @param container
	 * @param triplets
	 */
	private List<Object[]> sequenceTriplets(Container[][] cPool, int tupelSize, boolean[] valid) {
		List<Object[]> tupelList = new ArrayList<>();

		// F�r alle L�sungen
		for (int l = 0; l < cPool.length; l++) {
			Container[] container = cPool[l];

			// F�r alle Container
			for (int i = 0; i < container.length; i++) {
				List<Item> itemList = new ArrayList<>();
				for (Item cIt : container[i]) 
					if(cIt != null)
						itemList.add(cIt);

				// F�r alle Items im Container
				Item[] items = itemList.toArray(new Item[0]);
				for (int j = 0; j < itemList.size() - (tupelSize - 1); j++) {
					if(!checkItems(items, j, j + (tupelSize - 1), valid))
						continue;
					int hullVolume = getHullVolume(items, j, j + (tupelSize - 1));
					int itemVolume = getItemVolume(items, j, j + (tupelSize - 1));
					int bigHeight = getHeight(items, j, j + (tupelSize - 1));

//					ck(items, j, j + (tupelSize - 1));

					// Restrictions (100% utilization, max heigth)
					if(itemVolume < hullVolume)
						continue;
					if(bigHeight != 3)
						continue;

					Object[] o = new Object[tupelSize + 3];
					o[0] = hullVolume;
					o[1] = itemVolume;
					o[2] = bigHeight;
					for (int k = 0; k < tupelSize; k++)
						o[3+k] = items[j+k];

					tupelList.add(o);
				}
			}
		}

		return tupelList;
	}
	
	@SuppressWarnings("unused")
	private void ck(Item[] items, int a, int b) {
		if(b-a == 2 &&
				items[a].externalIndex == 8
//				items[a].w == 2 && 
//				items[a].l == 3 && 
//				items[a].h == 1 &&
//				items[a].weight == 3 &&
//				items[a+1].w == 2 && 
//				items[a+1].l == 3 && 
//				items[a+1].h == 1 &&
//				items[a+1].weight == 2 &&
//				items[a+2].w == 2 && 
//				items[a+2].l == 3 && 
//				items[a+2].h == 1 &&
//				items[a+2].weight == 1
			)
			System.out.println(
				items[a].externalIndex+"-"+items[a].w+","+items[a].l+","+items[a].h+","+items[a].weight+"\n"+
				items[a+1].externalIndex+"-"+items[a+1].w+","+items[a+1].l+","+items[a+1].h+","+items[a+1].weight+"\n"+
				items[a+2].externalIndex+"-"+items[a+2].w+","+items[a+2].l+","+items[a+2].h+","+items[a+2].weight+"\n"+
				"-----"
			);
	}
	
	/**
	 * 
	 * @param valid
	 * @param triplets
	 * @param bigItems
	 */
	private void selectBigItems(boolean[] valid, List<Object[]> triplets, List<BigItem> bigItems) {
		OUTER:
			for (Object[] v : triplets) {
				// If only one of items in tupel is already in big item, then continue
				for (int i = 0; i < v.length - 3; i++)
					if(valid[((Item)v[3+i]).externalIndex])
						continue OUTER;

				// Convert Object to Item Array and tag as chosen
				Item[] smallItems = new Item[v.length - 3];
				for (int i = 0; i < v.length - 3; i++) {
					final Item item = (Item) v[3+i];

					smallItems[i] = item;
					valid[item.externalIndex] = true;
				}

				// Create Big Item
				bigItems.add(BigItem.create(smallItems));
			}
	}

	/**
	 * @param container
	 * @param pool
	 * @param cPool
	 */
	private void evaluate(Container container, Item[][] pool, Container[][] cPool) {
		for (int i = 0; i < pool.length; i++) {
			cPool[i] = p.createLoadingPlan(pool[i], container);
		}
	}

	/**
	 * 
	 * @param items
	 * @param a
	 * @param b
	 * @return
	 */
	private int getHullVolume(Item[] items, int a, int b) {
		int minX = Integer.MAX_VALUE, maxX = 0, minY = Integer.MAX_VALUE, maxY = 0, minZ = Integer.MAX_VALUE, maxZ = 0;
		for (int i = b; i >= a; i--) {
			minX = Math.min(minX, items[i].x);
			maxX = Math.max(maxX, items[i].xw);
			minY = Math.min(minY, items[i].y);
			maxY = Math.max(maxY, items[i].yl);
			minZ = Math.min(minZ, items[i].z);
			maxZ = Math.max(maxZ, items[i].zh);
		}
		return (maxX - minX) * (maxY - minY) * (maxZ - minZ); 
	}

	/**
	 * 
	 * @param items
	 * @param a
	 * @param b
	 * @return
	 */
	private int getHeight(Item[] items, int a, int b) {
		int minZ = Integer.MAX_VALUE, maxZ = 0;
		for (int i = b; i >= a; i--) {
			minZ = Math.min(minZ, items[i].z);
			maxZ = Math.max(maxZ, items[i].zh);
		}
		return (maxZ - minZ); 
	}

	/**
	 * 
	 * @param items
	 * @param a
	 * @param b
	 * @return
	 */
	private int getItemVolume(Item[] items, int a, int b) {
		int sum = 0;
		for (int i = b; i >= a; i--)
			sum += items[i].volume;

		return sum;
	}

	/**
	 * 
	 * @param items
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean checkItems(Item[] items, int a, int b, boolean[] valid) {
		for (int i = b; i >= a; i--)
			if(valid[items[i].externalIndex])
				return false;

		return true;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	private Item[] shuffle(Item[] i) {
		List<Item> l = new ArrayList<>(Arrays.asList(i));
		Collections.shuffle(l, r);
		return l.toArray(new Item[i.length]);
	}

	/**
	 * 
	 * @param items
	 * @return
	 */
	private Item[] copy(Item[] items) {
		Item[] copy = new Item[items.length];
		for (int i = 0; i < copy.length; i++)
			copy[i] = items[i].copy();
		return copy;
	}


	public class BigItemBuilderResult {
		public final BigItem[] bigItems;
		public final Item[] items;

		public BigItemBuilderResult(BigItem[] bigItems, Item[] items) {
			this.bigItems = bigItems;
			this.items = items;
		}
	}
}
