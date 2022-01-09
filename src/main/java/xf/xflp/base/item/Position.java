package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public class Position {

	public final int idx, x, y, z;
	public final PositionType type;

	private Position(int idx, int x, int y, int z, PositionType type) {
		this.idx = idx;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}

	public static Position of(int idx, int x, int y, int z, PositionType type) {
		return new Position(idx, x, y, z, type);
	}

	public static Position of(int x, int y, int z) {
		return of(-1, x, y, z, PositionType.TMP);
	}

	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
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
