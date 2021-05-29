package xf.xflp.base.problem;

/**
 * Copyright (c) 2012-2021 Holger Schneider
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
	
	protected Position(int x, int y) {
		this(-1, x, y, -1, -1, false);
	}
	protected Position(int x, int y, int z) {
		this(-1, x, y, z, -1, false);
	}
	protected Position(int idx, int x, int y, int type, boolean isProjected) {
		this(idx, x, y, -1, type, isProjected);
	}
	protected Position(int x, int y, int type, boolean isProjected) {
		this(-1, x, y, -1, type, isProjected);
	}
	protected Position(int idx, int x, int y, int z, int type, boolean isProjected) {
		this.idx = idx;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.isProjected = isProjected;
	}

	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}
	
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
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return idx == ((Position) o).idx;
	}

	@Override
	public int hashCode() {
		return idx;
	}
}
