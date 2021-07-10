package test;

import org.junit.Test;
import xf.xflp.base.item.Container;
import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;
import xf.xflp.base.item.RotatedPosition;
import xf.xflp.opt.construction.ZSingleBinAddPacker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** 
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Test Suite for load planning
 * 
 * Central object of load planning is the representation and the restrictions of
 * a container. Hence here the general functions of the conainter are tested:
 * adding and removing of items, consideration of restrictions, etc...
 * 
 * @author hschneid
 *
 */
public class XFLPContainerTestSuite {

	private int itemIdx = 0;

	private Container previousTestContainer = null;

	// TODO: Teste eingesetztes Verfahren (Extrakt)
	// Teste LP Funktionen

	/*
	 * 
	 */
	@Test
	public void testRemovingReverseOrdering() {
		testSimpleAdding();

		Container con = previousTestContainer;

		Item i = getItem(1, 1, 1, 1, 10, 0);

		assertTrue(con.getPossibleInsertPositionList(i).size() == 0);

		List<Position> pList;
		Item lastItem = null;
		boolean found;
		// 1. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 1, 1, false) != null);
			found = true;
			for (Position p : pList) if(!(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1)) found = false;
			assertTrue(found);
		}

		// 2. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 1, 1, false) != null);
			assertTrue(findPos(pList, 1, 0, 1, false) != null);
		}

		// 3. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 1, 1, false) != null);
			assertTrue(findPos(pList, 1, 0, 1, false) != null);
			assertTrue(findPos(pList, 0, 0, 1, false) != null);
		}

		// 4. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 1, 0, false) != null);
			assertTrue(findPos(pList, 1, 1, 0, false) != null);
			assertTrue(findPos(pList, 0, 0, 1, false) != null);
			assertTrue(findPos(pList, 1, 0, 1, false) != null);
		}

		// 5. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 1, 0, false) != null);
			assertTrue(findPos(pList, 1, 0, 0, false) != null);
			assertTrue(findPos(pList, 0, 0, 1, false) != null);
		}

		// 6. Element
		{
			lastItem = getLastAddedItem(con);
			assertTrue(lastItem != null);
			con.remove(lastItem);

			pList = con.getPossibleInsertPositionList(i);
			assertTrue(findPos(pList, 0, 0, 0, false) != null);
			found = false;
			for (Position p : pList) if(p.getX() != 0 && p.getY() != 0 && p.getZ() != 0) found = true;
			assertFalse(found);
		}
	}

	/*
	 * 
	 */
	@Test
	public void testRemovingRandomOrder() {
		// L�sche beliebig Items raus
		// Auch wenn dadurch Items schweben, darf keine Exception fliegen.
		{
			Container con = getContainer(5,2,10);
			ZSingleBinAddPacker p = new ZSingleBinAddPacker();

			Item[] items = new Item[100];
			for (int i = 0; i < items.length; i++)
				items[i] = getItem(1, 1, 1, 1, 100, 0);

			Item[] unpackedItems = p.createLoadingPlan(items, con);
			assertTrue(unpackedItems.length == 0);

			Random r = new Random(1234);
			List<Item> itemList = Arrays.asList(items);
			Collections.shuffle(itemList, r);

			for (Item item : itemList) {
				try {
					con.remove(item);
				} catch(Exception e) {
					e.printStackTrace();
					assertTrue(false);
					break;
				}
			}
			assertTrue(con.getNbrOfLoadedThings() == 0);
			assertTrue(con.getActivePosList().size() == 1); //(Position (0,0,0))
		}
	}

	/*
	 * 
	 */
	@Test
	public void testRandomAction() {
		Container con = getContainer(100,100,100);
		ZSingleBinAddPacker p = new ZSingleBinAddPacker();

		Item[] items = new Item[100];
		for (int i = 0; i < items.length; i++)
			items[i] = getItem(1, 1, 1, 1, 100, 0);
		Item[] unpackedItems = p.createLoadingPlan(items, con);
		assertTrue(unpackedItems.length == 0);

		Random r = new Random(1234);

		try {
			for (int i = 0; i < 10000; i++) {
				if(i % 1000 == 0)
					System.out.println(i);
				float x = r.nextFloat();
				if(x < 0.333f) {
					int w = r.nextInt(2) + 1;
					int l = r.nextInt(2) + 1;
					int h = r.nextInt(2) + 1;
					Item it = getItem(w, l, h, 1, 1000, 0);
					List<Position> pList = con.getPossibleInsertPositionList(it);
					con.add(it, pList.get(r.nextInt(pList.size())));
				} else if(x < 0.666f) {
					List<Item> itemList = new ArrayList<>();
					for (Iterator<Item> ii = con.iterator(); ii.hasNext();) {
						Item it = ii.next();
						if(it != null)
							itemList.add(it);
					}
					con.remove(itemList.get(r.nextInt(itemList.size())));
				} else {
					int w = r.nextInt(2) + 1;
					int l = r.nextInt(2) + 1;
					int h = r.nextInt(2) + 1;
					Item it = getItem(w, l, h, 1, 1000, 0);
					List<Position> pList = con.getPossibleInsertPositionList(it);
					con.getTouchingPerimeter(it, pList.get(r.nextInt(pList.size())), 1, true, true);
				}
			}
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	/**
	 * 
	 * @param w
	 * @param l
	 * @param h
	 * @param ww
	 * @param wC
	 * @param sG
	 * @return
	 */
	private Item getItem(int w, int l, int h, int ww, long wC, int sG) {
		return getItem(w, l, h, ww, wC, sG, 1 << sG);
	}

	/**
	 * 
	 * @param w
	 * @param l
	 * @param h
	 * @param ww
	 * @param wC
	 * @param sG - noch unbin�r
	 * @param allowedSG - schon in bin�r
	 * @return
	 */
	private Item getItem(int w, int l, int h, int ww, long wC, int sG, int allowedSG) {
		Set<Integer> set = new HashSet<>();
		set.add(0);

		Item i = new Item(
				itemIdx,
				itemIdx,
				itemIdx,
				itemIdx + 1, 
				w,
				l,
				h,
				ww,
				wC,
				set, 
				1<<sG, 
				allowedSG, 
				true, 
				true
				);
		itemIdx++;

		return i;
	}

	/**
	 * 
	 * @param width
	 * @param length
	 * @param h
	 * @return
	 */
	private Container getContainer(int width, int length, int height) {
		return new Container(width, length, height, 999999999, 0, 0);
	}

	/**
	 * 
	 * @param width
	 * @param length
	 * @param h
	 * @param maxWeight
	 * @return
	 */
	private Container getContainer(int width, int length, int height, float maxWeight) {
		return new Container(width, length, height, maxWeight, 0, 0);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	private Item getLastAddedItem(Container con) {
		Item lastItem = null;
		int itemIdx = -1;
		for (Iterator<Item> it = con.iterator(); it.hasNext();) {
			Item j = it.next();
			if(j == null)
				continue;
			if(j.externalIndex > itemIdx) {
				itemIdx = j.externalIndex;
				lastItem = j;
			}
		}

		return lastItem;
	}

	/**
	 * 
	 * @param pList
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private Position findPos(List<Position> pList, int x, int y, int z, boolean rotated) {
		for (Position position : pList) {
			if(position.getX() == x && position.getY() == y && position.getZ() == z)
				if((rotated && position instanceof RotatedPosition)
						|| (!rotated && !(position instanceof RotatedPosition)))
					return position;
		}

		return null;
	}

	/**
	 * 
	 * @param pList
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private Position findPos(List<Position> pList, int x, int y, int z) {
		return findPos(pList, x, y, z, false);
	}

	/**
	 * 
	 * @param containers
	 * @param items
	 */
	protected void prepare(Container[] containers, Item[] items) {
		for (int i = 0; i < containers.length; i++)
			containers[i].clear();

		for (int i = 0; i < items.length; i++)
			items[i].reset();
	}
}
