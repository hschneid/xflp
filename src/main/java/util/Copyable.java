package util;

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
 * @param <E>
 */
public interface Copyable<E> {

	/**
	 * 
	 * @return
	 */
	public E copy();
}
