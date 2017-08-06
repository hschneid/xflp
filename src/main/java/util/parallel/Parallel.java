package util.parallel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Parallelization similar to C#-Parallel
 * 
 * Static methods which needs a method pointer for
 * the content of parallel tasks. Method pointer
 * are done by instances of ParallelOp, ParallelFuture
 * or ParallelRangeOp.
 * 
 * Number of available processors for tasks are
 * given by static class variable THREAD_COUNT.
 * 
 * Each static foreach-method waits until all tasks
 * are processed.
 * 
 * @author hschneid
 *
 */
public class Parallel {

	public static int THREAD_COUNT = 1;
	
	/**
	 * 
	 * @param <T>
	 * @param <A>
	 * @param list
	 * @param method
	 * @return
	 */
	public static <T, A> List<A> foreach(Collection<T> list, ParallelFuture<T, A> method) {
		List<A> resList = new ArrayList<>(list.size());
		try {
			ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
			for (T val : list) {
				// Prepare
				ParallelFuture<T, A> m = method.copy();
				m.set(val, resList);
				// Run
				es.submit(m);
			}
			es.shutdown();
			es.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		
		return resList;
	}
	
	/**
	 * 
	 * @param <T>
	 * @param list
	 * @param method
	 */
	public static <T> void foreach(Collection<T> list, ParallelOp<T> method) {
		try {
			ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
			for (T val : list) {
				ParallelOp<T> m = method.copy();
				m.set(val);
				es.submit(m);			
			}
			es.shutdown();
			es.awaitTermination(Integer.MAX_VALUE, TimeUnit.HOURS);			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param method
	 */
	public static void foreach(int start, int end, ParallelOp<Integer> method) {
		if(THREAD_COUNT == -1)
			THREAD_COUNT = 1;
		
		try {
			ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);
			
			for (int i = start; i < end; i++) {
				ParallelOp<Integer> m = method.copy();
				m.set(i);
				es.submit(m);			
			}
			es.shutdown();
			es.awaitTermination(10, TimeUnit.HOURS);			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param method
	 */
	public static void foreach(int start, int end, int threadCount, ParallelOp<Integer> method) {
		try {
			ExecutorService es = Executors.newFixedThreadPool(threadCount);
			
			for (int i = start; i < end; i++) {
				ParallelOp<Integer> m = method.copy();
				m.set(i);
				es.submit(m);			
			}
			es.shutdown();
			es.awaitTermination(10, TimeUnit.HOURS);			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param method
	 */
	public static void forRange(int start, int end, ParallelRangeOp<Integer> method) {
		if(THREAD_COUNT == -1)
			THREAD_COUNT = 1;
		
		forRange(start, end, THREAD_COUNT, method);
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param threadCount
	 * @param method
	 */
	public static void forRange(int start, int end, int threadCount, ParallelRangeOp<Integer> method) {
		try {
			ExecutorService es = Executors.newFixedThreadPool(threadCount);
			
			int val = start;
			int length = (int)Math.ceil((end - start)/THREAD_COUNT);
			for (int i = 0; i < THREAD_COUNT - 1; i++) {
				ParallelRangeOp<Integer> m = method.copy();
				m.set(val, val + length - 1);
				es.submit(m);
				val += length;
			}
			
			if(val != end) {
				// Rest
				ParallelRangeOp<Integer> m = method.copy();
				m.set(val, end-1);
				es.submit(m);
			}
			
			es.shutdown();
			es.awaitTermination(10, TimeUnit.HOURS);			
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
