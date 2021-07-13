package xf.xflp.base.item;

/** 
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author hschneid
 *
 */
public class RotatedPosition extends Position {
		
	public final Position pos;

	public RotatedPosition(Position pos) {
		super(pos.x, pos.y, pos.z);
		
		this.pos = pos;
	}

}