package xf.xflp.base.problem;

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
public class RotatedPosition extends Position {
		
	protected final Position pos;
	/**
	 * 
	 * @param pos
	 */
	protected RotatedPosition(Position pos) {
		super(pos.x, pos.y, pos.z);
		
		this.pos = pos;
	}

}
