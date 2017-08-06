package xf.xflp.base.problem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.collection.IndexedArrayList;
import util.collection.LPListMap;

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
public abstract class AbstractContainer implements Iterable<Item> {

	protected static final Position rootPos = new Position(-1, -1, -1);
	protected static final int ROOT = 0;
	protected static final int BASIC = 1;
	protected static final int EXTENDED = 2;

	/* Idx of the container. There are no two containers, with
	 * the same externalIndex. */
	protected int index = -1;

	protected final int width, height, length;
	protected final float maxWeight;
	protected float weight = 0;
	protected final int containerType;

	protected IndexedArrayList<Item> itemList = new IndexedArrayList<>();
	protected List<Position> activePosList = new ArrayList<>();

	protected LPListMap<Integer, Integer> xMap = new LPListMap<>();
	protected LPListMap<Integer, Integer> yMap = new LPListMap<>();
	protected LPListMap<Integer, Integer> zMap = new LPListMap<>();

	protected boolean ignoreMaxLength = false;


	/**
	 * 
	 * @param h
	 * @param width
	 */
	public AbstractContainer(int height, int width, int length, float maxWeight, int containerType) {
		this.width = width;
		this.height = height;
		this.length = length;
		this.maxWeight = maxWeight;
		this.containerType = containerType;
	}

	public float getWeight() {
		return weight;
	}

	public int getWidth() {
		return width;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getHeight() {
		return height;
	}

	public int getIndex() {
		return index;
	}

	public int getLength() {
		return length;
	}

	public float getMaxWeight() {
		return maxWeight;
	}

	public Item get(int index) {
		return itemList.get(index);
	}

	public float getTouchingPerimeter(Item item, Position pos, int itemTouchValue, boolean withWall, boolean withFloor) {
		int value = 0;
		int w = item.w;
		int l = item.l;
		int h = item.h;
		if(pos instanceof RotatedPosition) {
			w = item.l;
			l = item.w;
		}

		int xw = pos.x + w;
		int yl = pos.y + l;
		int zh = pos.z + h;
		
		List<Integer> list = null;
		
		// x-Achse
		{
			if(pos.x == 0)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withWall)
					value += h * l;

			if(xw == width)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withWall)
					value += h * l;

			List<Integer> xItemList = new ArrayList<>();
			list = xMap.get(pos.x); if(list != null) xItemList.addAll(list);
			list = xMap.get(xw); if(list != null) xItemList.addAll(list);

			if(xItemList.size() > 0) {
				// Pr�fe alle Items, die pos.x ber�hren
				for (int j = xItemList.size() - 1; j >= 0; j--) {
					int index = xItemList.get(j);
					Item i = itemList.get(index);

					if(i.xw == pos.x || i.x == xw) {
						// Pr�fe L�nge und H�he
						if(i.y > yl || i.yl < pos.y)
							continue;
						if(i.z > zh || i.zh < pos.z)
							continue;

						// Es liegt eine Ber�hrung vor, also berechne die Schnittfl�che
						int yLength = Math.min(yl, i.yl) - Math.max(i.y, pos.y);
						int zLength = Math.min(zh, i.zh) - Math.max(i.z, pos.z);
						value += yLength * zLength * itemTouchValue;
					}
				}
			}
		}

		// Y-Achse
		{
			if(pos.y == 0)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withWall)
					value += h * w;
			if(yl == length)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withWall)
					value += h * w;
			
			List<Integer> yItemList = new ArrayList<>(); 
			list = yMap.get(pos.y); if(list != null) yItemList.addAll(list);
			list = yMap.get(yl); if(list != null) yItemList.addAll(list);

			if(yItemList.size() > 0) {
				// Pr�fe alle Items, die pos.x ber�hren
				for (int j = yItemList.size() - 1; j >= 0; j--) {
					int index = yItemList.get(j);
					Item i = itemList.get(index);

					if(i.yl == pos.y || i.y == yl) {
						// Pr�fe Breite und H�he
						if(i.x > xw || i.xw < pos.x)
							continue;
						if(i.z > zh || i.zh < pos.z)
							continue;

						// Es liegt eine Ber�hrung vor, also berechne die Schnittfl�che
						int xLength = Math.min(xw, i.xw) - Math.max(i.x, pos.x);
						int zLength = Math.min(zh, i.zh) - Math.max(i.z, pos.z);
						value += xLength * zLength * itemTouchValue;
					}
				}
			}
		}
		
		// Z-Achse
		{
			if(pos.z == 0)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withFloor)
					value += w * l;

			if(zh == height)
				// Wenn die Wand mitber�cksichtigt werden soll, dann geht die komplette
				// Seitenfl�che mit ein
				if(withWall)
					value += w * l;

			List<Integer> zItemList = new ArrayList<>(); 
			list = yMap.get(pos.z); if(list != null) zItemList.addAll(list);
			list = yMap.get(zh); if(list != null) zItemList.addAll(list);

			// Pr�fe alle Items, die pos.x ber�hren
			for (int j = zItemList.size() - 1; j >= 0; j--) {
				int index = zItemList.get(j);
				Item i = itemList.get(index);

				if(i.zh == pos.z || i.z == zh) {
					// Pr�fe L�nge und H�he
					if(i.y > yl || i.yl < pos.y)
						continue;
					if(i.x > xw || i.xw < pos.x)
						continue;

					// Es liegt eine Ber�hrung vor, also berechne die Schnittfl�che
					int yLength = Math.min(yl, i.yl) - Math.max(i.y, pos.y);
					int xLength = Math.min(xw, i.xw) - Math.max(i.x, pos.x);
					value += yLength * xLength * itemTouchValue;
				}
			}
		}

		return value;
	}
	
	public int getUsedLoadingMeter() {
		int maxY = 0;
		for (int y : yMap.keySet())
			maxY = Math.max(maxY, y);
		return maxY;
	}

	@Override
	public Iterator<Item> iterator() {
		return itemList.iterator();
	}

	public void setIgnoreMaxLength() {
		this.ignoreMaxLength  = true;		
	}

	public int getNbrOfLoadedThings() {
		return itemList.length();
	}
	
	public int getContainerType() {
		return containerType;
	}
	
	public void clear() {
		weight = 0;
		itemList.clear();
		activePosList.clear();
		xMap.clear();
		yMap.clear();
		zMap.clear();
	}
}
