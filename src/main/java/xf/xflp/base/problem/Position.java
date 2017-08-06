package xf.xflp.base.problem;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author Hogo
 *
 */
public class Position {

	protected int x, y, z;

	protected final int idx;
	protected final int type;
	protected final boolean isProjected;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	protected Position(int x, int y) {
		this(-1, x, y, -1, -1, false);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	protected Position(int x, int y, int z) {
		this(-1, x, y, z, -1, false);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param type
	 * @param isProjected
	 */
	protected Position(int idx, int x, int y, int type, boolean isProjected) {
		this(idx, x, y, -1, type, isProjected);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param type
	 * @param isProjected
	 */
	protected Position(int x, int y, int type, boolean isProjected) {
		this(-1, x, y, -1, type, isProjected);
	}
	
	/**
	 * 
	 * @param idx
	 * @param x
	 * @param y
	 * @param z
	 * @param type
	 * @param isProjected
	 */
	protected Position(int idx, int x, int y, int z, int type, boolean isProjected) {
		this.idx = idx;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.isProjected = isProjected;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}
	
//	/*
//	 * (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		return idx;
//	}
//	
//	/*
//	 * (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		return (((Position)obj).idx == this.idx);
//	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getZ() {
		return z;
	}
}
