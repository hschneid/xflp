package util.parallel;

import java.util.List;


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
public abstract class ParallelFuture<E, A> implements Runnable, Cloneable {

	private E o;
	private List<A> list;
	
	public ParallelFuture() {}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public abstract A run(E o);

	/*  
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if(o != null)
				list.add(run(o));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param o
	 * @param list
	 */
	void set(E o, List<A> list) {
		this.o = o;
		this.list = list;
	}

	/**
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	public ParallelFuture<E, A> copy() throws CloneNotSupportedException {
		return (ParallelFuture<E, A>) super.clone();
	}
}