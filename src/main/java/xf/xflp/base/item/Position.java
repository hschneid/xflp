package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class Position {

	public int x, y, z;

	protected final int idx;
	public final int type;

	public Position(int x, int y) {
		this(-1, x, y, -1, -1);
	}
	public Position(int x, int y, int z) {
		this(-1, x, y, z, -1);
	}
	protected Position(int idx, int x, int y, int type) {
		this(idx, x, y, -1, type);
	}
	public Position(int idx, int x, int y, int z, int type) {
		this.idx = idx;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	@Override
	public String toString() {
		return "("+x+","+y+","+z+((this instanceof RotatedPosition) ? ",R":"")+")";
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

	public String getKey() {
		return x+"/"+y+"/"+z;
	}
}
