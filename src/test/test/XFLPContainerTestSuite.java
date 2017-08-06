package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import xf.xflp.base.problem.Container;
import xf.xflp.base.problem.Item;
import xf.xflp.base.problem.Position;
import xf.xflp.base.problem.RotatedPosition;
import xf.xflp.opt.construction.ZSingleBinAddPacker;

/** 
 * Copyright (c) 2012-present Holger Schneider
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
	public void testSimpleAdding() {
		Container con = getContainer(2,2,2);

		Item i = getItem(1, 1, 1, 1, 10, 0);
		List<Position> pList = con.getPossibleInsertPositionList(i);
		Position pp = findPos(pList, 0, 0, 0);
		// Erste Position suchen
		assertTrue(con.getNbrOfLoadedThings() == 0);
		assertTrue(pList.size() == 1);
		assertTrue(pp != null);

		// Erstes Element hinzuf�gen
		con.add(i, pList.get(0));
		assertTrue(con.getNbrOfLoadedThings() == 1);
		assertTrue(i.x == 0 && i.y == 0 && i.z == 0);

		// Zweite Position suchen
		Item i2 = getItem(1, 1, 1, 1, 10, 0);
		pList = con.getPossibleInsertPositionList(i2);
		assertTrue(pList.size() == 3);
		assertTrue(findPos(pList, 0, 0, 0) == null);
		assertTrue(findPos(pList, 1, 0, 0) != null);
		assertTrue(findPos(pList, 0, 1, 0) != null);
		assertTrue(findPos(pList, 0, 0, 1) != null);

		// Zweites Element einf�gen
		con.add(i2, findPos(pList, 1, 0, 0));
		assertTrue(con.getNbrOfLoadedThings() == 2);
		assertTrue(i2.x == 1 && i2.y == 0 && i2.z == 0);

		// 3. Element
		boolean found = false; Position foundPos = null;
		{
			Item i3 = getItem(2, 1, 1, 1, 10, 0);
			pList = con.getPossibleInsertPositionList(i3);
			found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 1 && p.getZ() == 0) { found = true; foundPos = p;}
			assertTrue(found);
			found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertTrue(found);
			found = false;
			con.add(i3, foundPos);
		}

		// 4. Element
		{
			Item i4 = getItem(1, 1, 1, 1, 10, 0);
			pList = con.getPossibleInsertPositionList(i4);
			found = false; foundPos = null;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) { found = true; foundPos = p;}
			assertTrue(found);
			found = false;
			for (Position p : pList) if(p.getX() == 1 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertTrue(found);
			found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1) found = true;
			assertTrue(found);
			con.add(i4, foundPos);
		}

		// 5. Element
		{
			Item i5 = getItem(1, 2, 1, 1, 10, 0);
			pList = con.getPossibleInsertPositionList(i5);
			assertTrue(findPos(pList, 1, 0, 1) != null);
			con.add(i5, findPos(pList, 1, 0, 1));
		}

		// 6. Element
		{
			Item i6 = getItem(1, 1, 1, 1, 10, 0);
			pList = con.getPossibleInsertPositionList(i6);
			found = false; foundPos = null;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1) { found = true; foundPos = p;}
			assertTrue(found);
			found = true;
			for (Position p : pList) if(!(p.getX() == 0 && p.getY() == 1 && p.getZ() == 1)) found = false;
			assertTrue(found);
			con.add(i6, foundPos);
		}

		previousTestContainer = con;
	}

	/*
	 * 
	 */
	@Test
	public void testAddingWithOrientation() {
		Container con = getContainer(2,2,2);

		Item i = getItem(2, 1, 1, 1, 10, 0);
		List<Position> pList = con.getPossibleInsertPositionList(i);

		// Erste Position suchen
		assertTrue(pList.size() == 2);
		boolean found = false;
		for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && p instanceof RotatedPosition) found = true;
		assertTrue(found);
		found = false;
		for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 0 && !(p instanceof RotatedPosition)) found = true;
		assertTrue(found);
	}

	/*
	 * 
	 */
	@Test
	public void testAddingWithErrors() {
		Container con = getContainer(2,2,2,10);

		Item i = getItem(3, 1, 1, 1, 10, 0);
		List<Position> pList = con.getPossibleInsertPositionList(i);
		assertTrue(pList.size() == 0);
		i = getItem(1, 3, 1, 1, 10, 0);
		pList = con.getPossibleInsertPositionList(i);
		assertTrue(pList.size() == 0);
		i = getItem(1, 1, 3, 1, 10, 0);
		pList = con.getPossibleInsertPositionList(i);
		assertTrue(pList.size() == 0);
		i = getItem(1, 1, 1, 11, 10, 0);
		pList = con.getPossibleInsertPositionList(i);
		assertTrue(pList.size() == 0);
		i = getItem(1, 1, 1, 9, 10, 0);
		pList = con.getPossibleInsertPositionList(i);
		assertTrue(pList.size() > 0);
	}

	/*
	 * 
	 */
	@Test
	public void testAddingWithStackingCapacity() {
		// Einfacher Stack (geht knapp)
		{
			Container con = getContainer(2,2,2);
			Item i = getItem(1, 1, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));
			i = getItem(1, 1, 1, 1, 10, 0);
			pList = con.getPossibleInsertPositionList(i);
			boolean found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertTrue(found);
		}

		// Einfacher Stack (geht nicht)
		{
			Container con = getContainer(2,2,2);
			Item i = getItem(1, 1, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));
			i = getItem(1, 1, 1, 2, 10, 0);
			pList = con.getPossibleInsertPositionList(i);
			boolean found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertFalse(found);
		}

		// Zwei-S�ulen-Stack (geht knapp)
		{
			Container con = getContainer(2,2,2);
			Item i = getItem(1, 1, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = null;
			for (Position p : pList) if(p.getX() == 1 && p.getY() == 0 && p.getZ() == 0) pos = p;
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(2, 1, 1, 2, 10, 0);
			pList = con.getPossibleInsertPositionList(i);
			boolean found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertTrue(found);
		}

		// Zwei-S�ulen-Stack (geht nicht)
		{
			Container con = getContainer(2,2,2);
			Item i = getItem(1, 1, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = null;
			for (Position p : pList) if(p.getX() == 1 && p.getY() == 0 && p.getZ() == 0) pos = p;
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(2, 1, 1, 3, 10, 0);
			pList = con.getPossibleInsertPositionList(i);
			boolean found = false;
			for (Position p : pList) if(p.getX() == 0 && p.getY() == 0 && p.getZ() == 1) found = true;
			assertFalse(found);
		}

		// Zwei-S�ulen-Stack++ (geht gar nicht)
		{
			Container con = getContainer(2,2,3);
			Item i = getItem(1, 1, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = findPos(pList, 1, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(2, 1, 1, 2, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 1);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 2);
			assertTrue(pos == null);
		}

		// Zwei-S�ulen-Stack++ (geht fast nicht)
		{
			Container con = getContainer(2,2,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 2, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = findPos(pList, 1, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(2, 1, 1, 2, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 1);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 2);
			assertTrue(pos == null);
		}

		// Zwei-S�ulen-Stack++ (geht ganz knapp)
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(2, 1, 1, 1, 2, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = findPos(pList, 2, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(3, 1, 1, 2, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 1);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 2);
			assertTrue(pos != null);
		}

		// Zwei-S�ulen-Stack++ (geht ganz knapp)
		{
			Container con = getContainer(6,3,3);
			// Traglast gesteigert
			Item i = getItem(4, 1, 1, 1, 2, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			con.add(i, pList.get(0));

			i = getItem(2, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			assertTrue(pList.size() > 0);
			Position pos = findPos(pList, 4, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(6, 1, 1, 2, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 1);
			assertTrue(pos != null);
			con.add(i, pos);

			i = getItem(1, 1, 1, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 0, 0, 2);
			assertTrue(pos != null);
		}
	}

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
	public void testRemovingAndPosition() {
		Container con = getContainer(2,2,5);

		Item i1, i2, i3;
		i1 = getItem(1, 1, 1, 1, 100, 0);
		con.add(i1, findPos(con.getPossibleInsertPositionList(i1), 0,0,0));
		i2 = getItem(1, 1, 1, 1, 100, 0);
		con.add(i2, findPos(con.getPossibleInsertPositionList(i2), 0,0,1));
		i3 = getItem(1, 1, 1, 1, 100, 0);
		con.add(i3, findPos(con.getPossibleInsertPositionList(i3), 0,0,2));

		List<Position> apList = con.getActivePosList();
		assertTrue(findPos(apList, 1, 0, 0) != null);
		assertTrue(findPos(apList, 0, 1, 0) != null);
		assertTrue(findPos(apList, 1, 0, 1) != null);
		assertTrue(findPos(apList, 0, 1, 1) != null);
		assertTrue(findPos(apList, 1, 0, 2) != null);
		assertTrue(findPos(apList, 0, 1, 2) != null);
		assertTrue(findPos(apList, 0, 0, 3) != null);

		con.remove(i2);
		apList = con.getActivePosList();
		assertTrue(findPos(apList, 0, 0, 1) != null);

		con.remove(i1);
		apList = con.getActivePosList();
		assertTrue(findPos(apList, 0, 0, 0) != null);

		con.remove(i3);
		apList = con.getActivePosList();
		assertTrue(con.getActivePosList().size() == 1);
		assertTrue(findPos(apList, 0, 0, 0) != null);
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

	/*
	 * 
	 */
	@Test
	public void testStackingGroup() {
		// Geht
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 1);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 1);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 1);
			assertTrue(findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false) != null);
		}
		// Geht gar nicht
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 2);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 2);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 1);
			assertTrue(findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false) == null);
		}
		// Geht nicht
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 1);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 2);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 1);
			assertTrue(findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false) == null);
		}

		// Geht mit 2 StackingGroups A
		{
			// Die beiden S�ulen k�nnen den Stapelgruppen 3 und 1 tragen
			Container con = getContainer(3, 3, 3);
			Item i = getItem(1, 1, 1, 1, 1, 1, 5);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 1, 5);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 0);
			assertTrue(findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false) != null);

			con = getContainer(3,3,3);
			// Traglast gesteigert
			i = getItem(1, 1, 1, 1, 1, 1, 5);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 1, 5);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 1);
			assertTrue(findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false) == null);
		}
	}

	/*
	 * 
	 */
	@Test
	public void testCoveredPositions() {
		// X-Achse
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 1, 0, false));
			// Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
			Position pos = findPos(con.getActivePosList(), 1, 1, 0);
			assertTrue(pos == null);
			pos = findPos(con.getInActivePosList(), 1, 1, 0);
			assertTrue(pos == null);
			pos = findPos(con.getCoveredPosList(), 1, 1, 0);
			assertTrue(pos != null);
		}
		// Y-Achse
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 1, 0));
			i = getItem(1, 2, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0, false));
			// Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
			Position pos = findPos(con.getActivePosList(), 1, 1, 0);
			assertTrue(pos == null);
			pos = findPos(con.getInActivePosList(), 1, 1, 0);
			assertTrue(pos == null);
			pos = findPos(con.getCoveredPosList(), 1, 1, 0);
			assertTrue(pos != null);
		}
		// Z-X-Achse
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
			i = getItem(2, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false));
			// Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
			Position pos = findPos(con.getActivePosList(), 1, 0, 1);
			assertTrue(pos == null);
			pos = findPos(con.getInActivePosList(), 1, 0, 1);
			assertTrue(pos == null);
			pos = findPos(con.getCoveredPosList(), 1, 0, 1);
			assertTrue(pos != null);
		}
		// Z-Y-Achse
		{
			Container con = getContainer(3,3,3);
			// Traglast gesteigert
			Item i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
			i = getItem(1, 1, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 1, 0));
			i = getItem(1, 2, 1, 1, 1, 0);
			con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 1, false));
			// Pr�fe, ob es die Position (1,1,0) in der activePosList gibt. -> das w�re illegal
			Position pos = findPos(con.getActivePosList(), 0, 1, 1);
			assertTrue(pos == null);
			pos = findPos(con.getInActivePosList(), 0, 1, 1);
			assertTrue(pos == null);
			pos = findPos(con.getCoveredPosList(), 0, 1, 1);
			assertTrue(pos != null);

		}
	}

	/*
	 * 
	 */
	@Test
	public void testTouchingPerimeter() {
		// Nur Wand (einseitig)
		{
			Container con = getContainer(10,10,10);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 48);
		}

		// Nur Wand (zweiseitig) Breite
		{
			Container con = getContainer(4,10,10);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 64);
		}

		// Nur Wand (zweiseitig) L�nge
		{
			Container con = getContainer(10,4,10);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 64);
		}

		// Nur Wand (zweiseitig) H�he
		{
			Container con = getContainer(10,10,4);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 64);
		}

		// Nur Wand (Dreiseitig) Breite, L�nge
		{
			Container con = getContainer(4,4,10);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 80);
		}

		// Nur Wand (Dreiseitig) Breite, H�he
		{
			Container con = getContainer(4,10,4);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 80);
		}

		// Nur Wand (Dreiseitig) L�nge, H�he
		{
			Container con = getContainer(10,4,4);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 80);
		}

		// Nur Wand (Allseitig) 
		{
			Container con = getContainer(4,4,4);
			// Traglast gesteigert
			Item i = getItem(4, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 96);
		}

		// Gemischt Wand und Box (1) 
		{
			Container con = getContainer(4,4,4);
			// Traglast gesteigert
			Item i = getItem(1, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);
			i = getItem(2, 2, 2, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 1, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 12);
		}

		// Gemischt Wand und Box halbhoch (1) 
		{
			Container con = getContainer(4,4,4);
			// Traglast gesteigert
			Item i = getItem(1, 4, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);
			i = getItem(2, 2, 2, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 1, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 10);
		}

		// Gemischt Wand und Box (1) 
		{
			Container con = getContainer(4,4,4);
			// Traglast gesteigert
			Item i = getItem(1, 4, 1, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);
			i = getItem(2, 2, 2, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 1, 0, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 10);
		}

		// Nur Box (1) 
		{
			Container con = getContainer(4,4,4);
			// Traglast gesteigert
			Item i = getItem(1, 4, 4, 1, 1, 0);
			List<Position> pList = con.getPossibleInsertPositionList(i);
			Position pos = findPos(pList, 0, 0, 0);
			assertTrue(pos != null);
			con.add(i, pos);
			i = getItem(3, 1, 4, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 1, 0, 0, false);
			assertTrue(pos != null);
			con.add(i, pos);
			i = getItem(2, 2, 2, 1, 1, 0);
			pList = con.getPossibleInsertPositionList(i);
			pos = findPos(pList, 1, 1, 0);
			assertTrue(pos != null);
			float f = con.getTouchingPerimeter(i, pos, 1, true, true);
			assertTrue(f == 12);
		}
	}

	/*
	 * 
	 */
	@Test
	public void testSeatOn() {
		Container con = getContainer(5,1,3);
		Item i = getItem(1, 1, 2, 1, 100, 0);
		con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
		i = getItem(2, 1, 1, 1, 100, 0);
		con.add(i, findPos(con.getPossibleInsertPositionList(i), 1, 0, 0));
		i = getItem(2, 1, 2, 1, 100, 0);
		con.add(i, findPos(con.getPossibleInsertPositionList(i), 3, 0, 0, false));

		// Geht nicht
		{
			i = getItem(2, 1, 1, 1, 100, 0);
			Position pos = findPos(con.getPossibleInsertPositionList(i), 0, 0, 2, false);
			assertTrue(pos == null);
		}
		// Geht nur knapp nicht
		{
			i = getItem(3, 1, 1, 1, 100, 0);
			Position pos = findPos(con.getPossibleInsertPositionList(i), 0, 0, 2, false);
			assertTrue(pos == null);
		}
		// Geht 
		{
			i = getItem(4, 1, 1, 1, 100, 0);
			Position pos = findPos(con.getPossibleInsertPositionList(i), 0, 0, 2, false);
			assertTrue(pos != null);
		}
	}

	@Test
	public void testRotation() {
		Container con = getContainer(2,2,1);
		Item i = getItem(1, 1, 1, 1, 100, 0);
		con.add(i, findPos(con.getPossibleInsertPositionList(i), 0, 0, 0));
		i = getItem(2, 1, 1, 1, 100, 0);
		List<Position> pList = con.getPossibleInsertPositionList(i);
		assertTrue(findPos(pList, 1, 0, 0, true) != null);
		assertTrue(findPos(pList, 0, 1, 0, false) != null);
		i.spinable = false;
		pList = con.getPossibleInsertPositionList(i);
		assertTrue(findPos(pList, 1, 0, 0, true) == null);
		assertTrue(findPos(pList, 0, 1, 0, false) != null);
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
