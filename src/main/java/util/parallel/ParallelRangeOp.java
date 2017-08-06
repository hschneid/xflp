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
public abstract class ParallelRangeOp<E> implements Runnable, Cloneable {

	private E start, end;
	
	public ParallelRangeOp() {}
	
	public abstract void run(E start, E end);

	/*  
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if(start != null && end != null)
				run(start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param start
	 * @param end
	 */
	void set(E start, E end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	public ParallelRangeOp<E> copy() throws CloneNotSupportedException {
		return (ParallelRangeOp<E>) super.clone();
	}
}