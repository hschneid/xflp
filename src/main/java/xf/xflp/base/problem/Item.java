package xf.xflp.base.problem;


import java.util.Set;

import util.Copyable;
import util.collection.Indexable;
import xf.xflp.report.PackageEventType;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * A Item object wraps a Package object with planning informations
 * 
 * @author Hogo
 *
 */
public class Item implements Copyable<Item>, Cloneable, Indexable {

	public final int size, volume, h;
	public int x, y, z, xw, yl, zh, w, l;
	
	public boolean spinable, stackable;
	public final int loadingLoc, unLoadingLoc;
	
	// Binary representation, where only one bit can be active
	public final int stackingGroup;
	// Allowed container types (cooled, dangerous goods, etc.)
	public final Set<Integer> allowedContainerSet;
	// Allowed items that can be stacked on top (binary representation)
	public final int allowedStackingGroups;

	public final float weight;
	public final float stackingWeightLimit;
	
	/* Unique index of this item object*/
	public final int externalIndex;
	/* Type of item: loading or unloading */
	public final PackageEventType loadingType;
	/* External externalIndex of this order. There can be two items
	 * with the same order externalIndex (up- and unloading) */
	public int orderIndex = -1;
	/* Idx in data structure of its holding container */
	/* -1 if its unpacked */
	public int index = -1;
	/* Idx of the container, where the item is packed in. */
	/* -1 if its unpacked */
	public int containerIndex = -1;
	
	// Defines if this item is loaded (true) or unloaded (false)
	public boolean isLoading = false;
	// Defines if this item was rotated (true) or not rotated (false)
	public boolean isRotated = false;
	
	public Item(
			int externalIdx,
			int orderIndex,
			int loadingLoc,
			int unLoadingLoc,
			int width,
			int length,
			int height,
			float weight,
			float bearingCapacity,
			Set<Integer> allowedContainerSet,
			int stackingGroup,
			int allowedStackingGroups,
			boolean isSpinable,
			boolean isLoading
			) {
		this.x = this.y = this.z = this.xw = this.yl = this.zh = -1;

		this.w = width;
		this.l = length;
		this.h = height;
		this.size = w * l;
		this.volume = h * w * l;
		this.weight = weight;
		this.stackingWeightLimit = bearingCapacity;
		
		this.stackingGroup = stackingGroup;
		this.allowedStackingGroups = allowedStackingGroups;
		
		this.spinable = isSpinable;
		this.stackable = true;
		
		this.externalIndex = externalIdx;
		this.orderIndex = orderIndex;
		this.loadingLoc = loadingLoc;
		this.unLoadingLoc = unLoadingLoc;
		this.loadingType = (isLoading == true) ? PackageEventType.LOAD : PackageEventType.UNLOAD;
		
		this.allowedContainerSet = allowedContainerSet;
	}
	
	/**
	 * 
	 */
	public void rotate() {
		int tmp = w;
		w = l;
		l = tmp;
		
		isRotated = !isRotated;
	}
	
	/**
	 * 
	 * @param pos
	 */
	public void setPosition(Position pos) {
		x = pos.x;
		y = pos.y;
		z = pos.z;
		xw = x + w;
		yl = y + l;
		zh = z + h;
	}
	
	/**
	 * 
	 */
	public void clearPosition() {
		this.x = this.y = this.z = this.xw = this.yl = this.zh = -1;
	}

	/*
	 * (non-Javadoc)
	 * @see da.util.Copyable#copy()
	 */
	@Override
	public Item copy() {
		try {
			Item i = (Item)super.clone();
			
			return i;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\tItem "+this.externalIndex+" "+loadingLoc+" "+unLoadingLoc+" "+(((isLoading)?1:-1)+" ("+w+","+l+","+h+")");
	}
	
	/**
	 * 
	 */
	public void reset() {
		clearPosition();
		this.index = -1;
		this.containerIndex = -1;
		if(this.isRotated)
			rotate();
		this.isLoading = false;
	}
	
	/**
	 * 
	 */
	public void resetWithoutRotate() {
		clearPosition();
		this.index = -1;
		this.containerIndex = -1;
		this.isLoading = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#getIdx()
	 */
	@Override
	public int getIdx() {
		return index;
	}
	
	/*
	 * (non-Javadoc)
	 * @see util.collection.Indexable#setIdx(int)
	 */
	@Override
	public void setIdx(int idx) {
		this.index = idx;
	}
}
