package xf.xflp.opt.extract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
public class Extract {
	
	private Random r = new Random(1234);
	private ZMultiBinAddPackerNoSort p = new ZMultiBinAddPackerNoSort();

	/**
	 * 
	 * @param items
	 * @param container
	 * @return
	 */
	public Container[] createLoadingPlan(Item[] items, Container container) {
		Container[] conArr = null;
		List<Container> readyContainerList = new ArrayList<>();
		
		System.out.println("0 "+items.length+" "+readyContainerList.size());
		
		items = opt(items, container, readyContainerList, 1000, 0.95f);
		items = opt(items, container, readyContainerList, 5000, 0.9f);
		items = opt(items, container, readyContainerList, 4000, 0.85f);
		
		// Rest in result
		conArr = p.createLoadingPlan(items, container);
		for (Container c : conArr)
			readyContainerList.add(c);
		
		// Reindex the items to their new container position
		conArr = reindex(readyContainerList);
		
		System.out.println("X "+conArr.length);
		
		return conArr;
	}

	/**
	 * @param items
	 * @param container
	 * @param readyContainerList
	 * @return
	 */
	private Item[] opt(Item[] items, Container container, List<Container> outerReadyContainerList, int nbrLoops, float threshold) {
		Container[] conArr;
		List<Container> readyContainerList = new ArrayList<>();
		for (int i = 0; i < nbrLoops; i++) {
			// Perturb
			perturb(items);
			
			// Evaluate
			conArr = p.createLoadingPlan(items, container);
			
			// Select
			items = extract(conArr, readyContainerList, threshold);
						
			// Aspiration
			String asp = "";
			if(r.nextDouble() < 0.001) {
				items = aspirate(items, readyContainerList);
				asp = "aspirate";
			}
			
			// Output
			System.out.println(i+" "+items.length+" "+readyContainerList.size()+" "+asp);
		}
		
		outerReadyContainerList.addAll(readyContainerList);
		
		return items;
	}
	
	private Item[] aspirate(Item[] items, List<Container> readyContainerList) {
		List<Item> itemList = new ArrayList<>();
		
		int cIdx = r.nextInt(readyContainerList.size());
		Container c = readyContainerList.remove(cIdx);
		for (Item item : c) {
			if(item != null) {
				itemList.add(item);
				item.reset();
			}
		}
		
		for (Item item : items)
			itemList.add(item);

		Collections.shuffle(itemList, r);
		
		return itemList.toArray(new Item[0]);
	}

	/**
	 * 
	 * @param items
	 */
	private void perturb(Item[] items) {
		for (int i = 0; i < 5; i++) {
			int a = r.nextInt(items.length);
			int b = -1;
			do {
				b = r.nextInt(items.length);
			} while(a == b);
			
			Item tmp = items[a];
			items[a] = items[b];
			items[b] = tmp;
		}
	}

	/**
	 * 
	 * @param readyContainerList
	 * @return
	 */
	private Container[] reindex(List<Container> readyContainerList) {
		Container[] conArr = new Container[readyContainerList.size()];
		for (int i = 0; i < readyContainerList.size(); i++) {
			Container c = readyContainerList.get(i);
			
			c.setIndex(i);
			
			for (Item item : c)
				if(item != null)
					item.containerIndex = i;
			
			conArr[i] = c;
		}
		return conArr;
	}

	/**
	 * 
	 * @param conArr
	 * @param readyContainerList
	 * @param thresholdVolume
	 * @return
	 */
	public Item[] extract(Container[] conArr, List<Container> readyContainerList, float thresholdVolume) {
		List<Item> itemList = new ArrayList<>();
		for (Container c : conArr) {
			if((c.getLoadedVolume() / c.getMaxVolume()) >= thresholdVolume)
				readyContainerList.add(c);
			else 
				for (Item item : c) {
					if(item != null) {
						itemList.add(item);
						item.reset();
					}
				}
		}
		
		return itemList.toArray(new Item[0]);
	}
}
