package test;


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
public class ZPerfSuite {

//	private int itemIdx = 0;
//	private Random rand = new Random(1234);
//	
//	private Comparator<Item> c1 = new Comparator<Item>() {
//		@Override
//		public int compare(Item a, Item b) {
//			if(a.stackingWeightLimit > b.stackingWeightLimit)
//				return -1;
//			if(a.stackingWeightLimit < b.stackingWeightLimit)
//				return 1;
//			if(a.h < b.h)
//				return -1;
//			if(a.h > b.h)
//				return 1;
//			if(a.w > b.w)
//				return -1;
//			if(a.w < b.w)
//				return 1;
//			if(a.l > b.l)
//				return -1;
//			if(a.l < b.l)
//				return 1;
//			if(a.stackingGroup > b.stackingGroup)
//				return -1;
//			if(a.stackingGroup < b.stackingGroup)
//				return 1;
//			return 0;
//		}
//	};
//	
//	@Test
//	public void testPacking() {
//		ZMultiBinAddPacker p = new ZMultiBinAddPacker();
//		ZMultiBinAddPacker2 p2 = new ZMultiBinAddPacker2();
//
//		// Genau richtig
//		{
//			Container[] conArr = new Container3DImpl[1];
//			for (int i = 0; i < conArr.length; i++) {
//				conArr[i] = getContainer(5, 5, 5, 1000);
//				conArr[i].setIndex(i);
//			}
//
//			Item[] items = new Item[125];
//			for (int i = 0; i < items.length; i++)
//				items[i] = getItem(1, 1, 1, 1, 10, 1);
//
//			Item[] nonPackedItems = p.createLoadingPlan(items, conArr);
//
//			assertTrue(nonPackedItems.length == 0);
//		}
//
//		// einer zuviel
//		{
//			Container[] conArr = new Container3DImpl[1];
//			for (int i = 0; i < conArr.length; i++) {
//				conArr[i] = getContainer(5, 5, 5, 1000);
//				conArr[i].setIndex(i);
//			}
//
//			Item[] items = new Item[126];
//			for (int i = 0; i < items.length; i++)
//				items[i] = getItem(1, 1, 1, 1, 10, 1);
//
//			Item[] nonPackedItems = p.createLoadingPlan(items, conArr);
//
//			assertTrue(nonPackedItems.length == 1);
//		}
//
//		// Komplexeres Muster mit drehen
//		{
//			Container[] conArr = new Container3DImpl[1];
//			for (int i = 0; i < conArr.length; i++) {
//				conArr[i] = getContainer(3, 4, 3, 1000);
//				conArr[i].setIndex(i);
//			}
//
//			Item[] items = new Item[18];
//			for (int i = 0; i < items.length; i++)
//				items[i] = getItem(2, 1, 1, 1, 10, 1);
//
//			Item[] nonPackedItems = p.createLoadingPlan(items, conArr);
//
//			assertTrue(nonPackedItems.length == 0);
//		}
//
//		// Sehr Komplexes Muster
//		{
//			for (int k = 0; k < 5; k++) {
//				Container[] conArr = new Container3DImpl[1];
//				for (int i = 0; i < conArr.length; i++) {
//					conArr[i] = getContainer(3, 4, 3, 1000);
//					conArr[i].setIndex(i);
//				}
//
//				int j = 0;
//				Item[] items = new Item[12];
//				for (int i = 0; i < 7; i++)
//					items[j++] = getItem(2, 1, 1, 1, 10, 1);
//				for (int i = 0; i < 3; i++)
//					items[j++] = getItem(2, 2, 1, 1, 10, 1);
//				for (int i = 0; i < 1; i++)
//					items[j++] = getItem(1, 4, 1, 1, 10, 1);
//				for (int i = 0; i < 1; i++)
//					items[j++] = getItem(3, 2, 1, 1, 10, 1);
//				List<Item> l = Arrays.asList(items);
//				Collections.shuffle(l, rand);
//				Item[] nonPackedItems = p2.createLoadingPlan(l.toArray(new Item[0]), conArr);
//				assertTrue(nonPackedItems.length == 0);
//			}
//		}
//
//		// Sehr Komplexes Muster mit H�hen
//		{
//			for (int k = 0; k < 5; k++) {
//				Container[] conArr = new Container3DImpl[1];
//				for (int i = 0; i < conArr.length; i++) {
//					
//					conArr[i] = getContainer(3, 4, 3, 1000);
//					conArr[i].setIndex(i);
//				}
//
//				int j = 0;
//				Item[] items = new Item[12];
//				for (int i = 0; i < 2; i++)
//					items[j++] = getItem(1, 1, 1, 1, 10, 1);
//				for (int i = 0; i < 2; i++)
//					items[j++] = getItem(1, 2, 1, 1, 10, 1);
//				for (int i = 0; i < 2; i++)
//					items[j++] = getItem(1, 3, 1, 1, 10, 1);
//				for (int i = 0; i < 2; i++)
//					items[j++] = getItem(2, 2, 1, 1, 10, 1);
//				items[j++] = getItem(4, 1, 1, 1, 10, 1);
//				items[j++] = getItem(2, 1, 2, 1, 10, 1);
//				items[j++] = getItem(2, 1, 3, 1, 10, 1);
//				items[j++] = getItem(1, 1, 2, 1, 10, 1);
//				List<Item> l = Arrays.asList(items);
//				Collections.shuffle(l, rand);
//				Item[] nonPackedItems = p2.createLoadingPlan(l.toArray(new Item[0]), conArr);
//				assertTrue(nonPackedItems.length == 0);
//			}
//		}
//	}
//	
//	/*
//	 * 
//	 */
//	@Test
//	public void testPackingEndlessContainers() {
////		ZMultiBinAddPackerNoSort packer = new ZMultiBinAddPackerNoSort();
//		ZMultiBinAddPackerLookAhead packer = new ZMultiBinAddPackerLookAhead();
//		
//		Container container = getContainer(4, 9, 3, 1000);
//		container.setIndex(0);
//		
//		long time = System.currentTimeMillis();
//		List<Item> itemList = new ArrayList<Item>();
//		int nbr = 100;
//		for (int i = 0; i < nbr; i++)
//			itemList.addAll(Arrays.asList(getScenario4()));
//		Collections.sort(itemList, c1);
//		
//		Item[] items = itemList.toArray(new Item[0]);
//		Container[] containers = packer.createLoadingPlan(items, container);
//		
//		System.out.println("IST "+containers.length+" SOLL "+nbr+" in "+(System.currentTimeMillis() - time)+" ms");
//		
////		ContainerMergeILS cmILS = new ContainerMergeILS();
////		cmILS.execute(containers);
//	}
//
////	@Test
////	public void testPackingWithStacking() {
////		ContainerMergeILS p = new ContainerMergeILS();
////		ZMultiBinAddPacker4 pp = new ZMultiBinAddPacker4();
////
////		// Sehr Komplexes Muster mit einer Lage ohne Belastbarkeit
////		//		{
////		//			for (int k = 0; k < 5; k++) {
////		//				Container[] conArr = new Container[1];
////		//				for (int i = 0; i < conArr.length; i++) {
////		//					conArr[i] = new Container(3, 4, 3, 1000, 0);
////		//					conArr[i].setIndex(i);
////		//				}
////		//
////		//				int j = 0;
////		//				Item[] items = new Item[12];
////		//				for (int i = 0; i < 7; i++)
////		//					items[j++] = getItem(2, 1, 1, 1, 10, 1);
////		//				items[j++] = getItem(2, 2, 1, 1, 10, 1);
////		//				items[j++] = getItem(3, 2, 1, 1, 10, 1);
////		//				for (int i = 0; i < 2; i++)
////		//					items[j++] = getItem(2, 2, 1, 1, 0, 1);
////		//				items[j++] = getItem(1, 4, 1, 1, 0, 1);
////		//				List<Item> l = Arrays.asList(items);
////		//				Collections.shuffle(l, rand);
////		//				items = l.toArray(new Item[0]);
////		//				items = p.execute(conArr, items);
////		//				Item[] nonPackedItems = pp.createLoadingPlan(items, conArr);
////		//				assertTrue(nonPackedItems.length == 0);
////		//			}
////		//		}
////
////		// Gro�es komplexes Muster mit Belastbarkeiten und Stapelgruppen
////		{
////			for (int k = 0; k < 1; k++) {
////				Container[] conArr = new Container[1];
////				for (int i = 0; i < conArr.length; i++) {
////					conArr[i] = new Container(4, 9, 3, 1000, 0);
////					conArr[i].setIndex(i);
////				}
////
////				Item[] items = getScenario4();
////				List<Item> l = Arrays.asList(items);
////				Collections.shuffle(l, rand);
////				items = l.toArray(new Item[0]);
////				items = p.execute(conArr, items);
//////				Debug.debug = true;
////				Item[] nonPackedItems = pp.createLoadingPlan(items, conArr);
////				assertTrue(nonPackedItems.length == 0);
//////				assertTrue(conArr[0].getActivePosList().size() == 0);
////
////				for (int i = 0; i < items.length; i++) {
////					Item v = items[i];
////					System.out.println(v.externalIndex+" "+v.w+" "+v.l+" "+v.height+" "+v.x+" "+v.y+" "+v.z+" "+v.stackingGroup+" "+v.isRotated);
////				}
////				
////				for (int i = 0; i < conArr[0].getActivePosList().size(); i++)
////					System.out.println(conArr[0].getActivePosList().get(i));
////
////				//						prepare(conArr, items);
////				//		
////				//						int nbr = 2;
////				//						Container[] conArr2 = new Container[nbr];
////				//						for (int i = 0; i < nbr; i++) {
////				//							conArr2[i] = new Container(4, 10, 3, 1000, 0);
////				//							conArr2[i].setIndex(i);
////				//						}
////				//		
////				//						Item[] items2 = new Item[items.length * nbr];
////				//						int cnt = 0;
////				//						for (int i = 0; i < nbr; i++)
////				//							for (int j = 0; j < items.length; j++)
////				//								items2[cnt++] = getItem((int)items[j].w, (int)items[j].l, (int)items[j].height, (int)items[j].weight, items[j].stackingWeightLimit, (int)items[j].stackingGroup);
////				//						Debug.debug = true;
////				//						nonPackedItems = pp.createLoadingPlan(items2, conArr2);
////				//						assertTrue(nonPackedItems.length == 0);
////			}
////		}
////
////		//		// Gro�es komplexes Muster mit Belastbarkeiten und Stapelgruppen und Mehreren Ladungstr�gern
////		//		{
////		//			int nbrOfContainers = 2;
////		//			for (int k = 0; k < 5; k++) {
////		//				Container[] conArr = new Container[nbrOfContainers];
////		//				for (int i = 0; i < conArr.length; i++) {
////		//					conArr[i] = new Container(4, 10, 3, 1000, 0);
////		//					conArr[i].setIndex(i);
////		//				}
////		//
////		//				List<Item> itemList = new ArrayList<Item>();
////		//				for (int i = 0; i < nbrOfContainers; i++) 
////		//					itemList.addAll(Arrays.asList(getScenario4()));	
////		//				Collections.shuffle(itemList, rand);
////		//				Item[] items = itemList.toArray(new Item[0]);
////		//				items = p.execute(conArr, items);
////		//				Item[] nonPackedItems = pp.createLoadingPlan(items, conArr);
////		//				assertTrue(nonPackedItems.length == 0);
////		//			}
////		//		}
////	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	private Item[] getScenario4() {
//		Item[] items = new Item[19];
//		int cnt = 0;
//		items[cnt++] = getItem(2, 2, 1, 3, 3, 1);
//		items[cnt++] = getItem(2, 2, 1, 3, 3, 1);
//		items[cnt++] = getItem(2, 2, 1, 1, 1, 1);
//		items[cnt++] = getItem(2, 1, 1, 3, 3, 2);
//		items[cnt++] = getItem(2, 1, 1, 3, 3, 2);
//		items[cnt++] = getItem(2, 1, 1, 2, 2, 1);
//		items[cnt++] = getItem(2, 1, 1, 2, 2, 2);
//		items[cnt++] = getItem(2, 1, 1, 1, 1, 1);
//		items[cnt++] = getItem(2, 3, 1, 3, 3, 2);
//		items[cnt++] = getItem(2, 3, 1, 2, 2, 2);
//		items[cnt++] = getItem(2, 3, 1, 1, 1, 2);
//		items[cnt++] = getItem(2, 4, 1, 1, 1, 1);
//		items[cnt++] = getItem(1, 4, 1, 1, 1, 2);
//
//		items[cnt++] = getItem(2, 4, 2, 3, 3, 1);
//		items[cnt++] = getItem(1, 4, 2, 3, 3, 1);
//		items[cnt++] = getItem(2, 1, 2, 3, 3, 2);
//		items[cnt++] = getItem(2, 1, 2, 2, 2, 2);
//		items[cnt++] = getItem(2, 3, 2, 2, 2, 1);
//
//		items[cnt++] = getItem(1, 4, 3, 3, 3, 2);
//
//		return items;
//	}
//
//	/**
//	 * 
//	 * @param w
//	 * @param l
//	 * @param h
//	 * @param ww
//	 * @param wC
//	 * @param sG
//	 * @return
//	 */
//	private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
//		Set<Integer> set = new HashSet<Integer>();
//		set.add(0);
//		
//		int newStackingGroup = 1 << sG;
//		int allowedStackingGroups = newStackingGroup;
//
//		TestPackageSet pS = new TestPackageSet(
//				new TestPackageType(h, l, w, ww, wC, newStackingGroup),
//				1
//				);
//
//		Item i = new Item(itemIdx, itemIdx, itemIdx, itemIdx + 1, pS, true, set, allowedStackingGroups);
//		itemIdx++;
//
//		return i;
//	}
//
//	/**
//	 * 
//	 * @param width
//	 * @param length
//	 * @param h
//	 * @return
//	 */
//	private Container getContainer(int width, int length, int height) {
//		return new Container3DImpl(width, length, height, 999999999, 0, 0);
//	}
//
//	/**
//	 * 
//	 * @param width
//	 * @param length
//	 * @param h
//	 * @param maxWeight
//	 * @return
//	 */
//	private Container getContainer(int width, int length, int height, float maxWeight) {
//		return new Container3DImpl(width, length, height, maxWeight, 0, 0);
//	}
//	
//	public static void main(String[] args) {
//		new ZPerfSuite().testPackingEndlessContainers();
//	}
}
