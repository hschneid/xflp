package util.parallel;


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
public abstract class ParallelOp<E> implements Runnable, Cloneable {

	private E o;
	
	public ParallelOp() {}
	
	/**
	 * 
	 * @param o
	 */
	public abstract void run(E o);

	/*  
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if(o != null)
				run(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param o
	 */
	void set(E o) {
		this.o = o;
	}

	/**
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	public ParallelOp<E> copy() throws CloneNotSupportedException {
		return (ParallelOp<E>) super.clone();
	}
}