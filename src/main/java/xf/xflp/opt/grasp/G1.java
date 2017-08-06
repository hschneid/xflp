package xf.xflp.opt.grasp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.opt.construction.ZMultiBinAddPackerNoSort;
import xf.xflp.opt.grasp.BigItemBuilder.BigItemBuilderResult;

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
public class G1 {

	private Random r = new Random(1234);
	private ZMultiBinAddPackerNoSort p = new ZMultiBinAddPackerNoSort();

	/**
	 * 
	 * @param items
	 * @param container
	 * @return
	 */
	public Container[] createLoadingPlan(Item[] items, Container container) {
		BigItemBuilderResult bibr = new BigItemBuilder().findBigItems(items, container);

		// Wieviele Container jetzt mit den BigItems?
		List<Item> allItem = new ArrayList<>();
		for (Item item : bibr.items) {
			item.reset();
			allItem.add(item);
		}
		for (Item item : bibr.bigItems) {
			item.reset();
			allItem.add(item);
		}
		//ZMultiBinAddPacker5 pp = new ZMultiBinAddPacker5();
		//Container[] sol = pp.createLoadingPlan(allItem.toArray(new Item[0]), container);
		
		Item[][] pool = new Item[50][];
		// Zuf�llige Initialisierung
		for (int i = 0; i < pool.length; i++)
			pool[i] = copy(shuffle(items));

		for (int i = 0; i < 5; i++) {
			System.out.println("Gen "+i);
			// Bewertung
			Container[][] cPool = new Container[50][];
			evaluate(container, pool, cPool);

			pool = createNewIndividuals(items, cPool);
			System.out.println("---");
		}
		//return sol;
		return null;
	}

	private Item[][] createNewIndividuals(Item[] items, Container[][] containers) {
		Item[][] newPool = new Item[20][];
		int cnt = 0;

		for (int i = 0; i < 20; i++) {
			// W�hle Ergebnisse f�r Rekombination
			Container[][] cPool = new Container[8][];
			for (int j = 0; j < 8; j++)
				cPool[j] = containers[r.nextInt(containers.length)];
			cPool = containers;

			// Sequenziere Ergebnisse und priorisiere sie
			List<Object[]> triplets = new ArrayList<>();
			for (int k = 0; k < cPool.length; k++)
				sequenceTriplets(cPool[k], triplets);

			Collections.sort(triplets, new Comparator<Object[]>() {
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
			});

			// W�hle beste Triplets
			newPool[cnt++] = selectNewItemSequence(items, triplets);
		}

		return newPool;
	}

	/**
	 * @param container
	 * @param pool
	 * @param cPool
	 */
	private void evaluate(Container container, Item[][] pool, Container[][] cPool) {
		for (int i = 0; i < pool.length; i++) {
			cPool[i] = p.createLoadingPlan(pool[i], container);
			System.out.println("Bewerte "+i+" "+cPool[i].length);
		}
	}

	/**
	 * 
	 * @param items
	 * @param triplets
	 * @return
	 */
	private Item[] selectNewItemSequence(Item[] items, List<Object[]> triplets) {
		int maxIdx = 0;
		for (int i = 0; i < items.length; i++)
			maxIdx = Math.max(maxIdx, items[i].externalIndex);
		maxIdx++;
		Item[] ii = new Item[maxIdx];
		for (int i = 0; i < items.length; i++)
			ii[items[i].externalIndex] = items[i];
		boolean[] valid = new boolean[maxIdx];

		List<Item> newItemList = new ArrayList<>();
		for (Object[] v : triplets) {
//			if((Integer)v[1]/(float)((Integer)v[2]) < 0.5f)
//				continue;
			
			Item i1 = (Item) v[3+0];
			Item i2 = (Item) v[3+1];
			Item i3 = (Item) v[3+2];

			if(!valid[i1.externalIndex] && !valid[i2.externalIndex] && !valid[i3.externalIndex]) {
				newItemList.add(i1);
				newItemList.add(i2);
				newItemList.add(i3);

				valid[i1.externalIndex] = true;
				valid[i2.externalIndex] = true;
				valid[i3.externalIndex] = true;
			}
		}

//		for (int i = 0; i < valid.length; i++)
//			if(!valid[i] && ii[i] != null)
//				newItemList.add(ii[i]);
		System.out.println(newItemList.size());
		for (Item item : newItemList)
			item.resetWithoutRotate();

		return copy(newItemList.toArray(new Item[0]));
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

	/**
	 * 
	 * @param container
	 * @param triplets
	 */
	private void sequenceTriplets(Container[] container, List<Object[]> triplets) {
		int nbr = 3;

		for (int i = 0; i < container.length; i++) {
			List<Item> itemList = new ArrayList<>();
			for (int j = 0; j < container[i].getNbrOfLoadedThings(); j++)
				itemList.add(container[i].get(j));
			Item[] items = itemList.toArray(new Item[0]);

			for (int j = 0; j < itemList.size() - (nbr - 1); j++) {
				int hullVolume = getHullVolume(items, j, j + (nbr - 1));
				int itemVolume = getItemVolume(items, j, j + (nbr - 1));
				int bigHeight = getHeight(items, j, j + (nbr - 1));
				if(itemVolume < hullVolume)
					continue;
				if(bigHeight != 3)
					continue;
				
				Object[] o = new Object[nbr + 3];
				o[0] = hullVolume;
				o[1] = itemVolume;
				o[2] = bigHeight;
				for (int k = 0; k < nbr; k++)
					o[3+k] = items[j+k];

				triplets.add(o);
			}
		}
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

	private int getItemVolume(Item[] items, int a, int b) {
		int sum = 0;
		for (int i = b; i >= a; i--)
			sum += items[i].volume;

		return sum;
	}
}
