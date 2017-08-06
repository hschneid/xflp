package util.collection;

import java.util.Iterator;
import java.util.List;

import cern.colt.function.IntProcedure;
import cern.colt.list.IntArrayList;
import cern.colt.list.ObjectArrayList;
import cern.colt.map.AbstractIntObjectMap;
import cern.colt.map.OpenIntObjectHashMap;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author Sarah B�lles
 *
 * @since 18.07.2011
 */
public class OpenIntMap<T> {
	private AbstractIntObjectMap map = new OpenIntObjectHashMap();
	public static interface IntProcedureBase<R> {
		boolean apply(int key, R value);
	}
	
	public OpenIntMap() {
		this(10);
	}
	
	public OpenIntMap(int initialSize) {
		map = new OpenIntObjectHashMap();
		map.ensureCapacity(initialSize);
	}
	
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			ObjectArrayList list = map.values();
			IntArrayList keys = map.keys();
			int pos = -1;
			int end = list.size() - 1;
			boolean removed = false;
			
			@Override
			public boolean hasNext() {
				return pos < end;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				if (!hasNext()) throw new IllegalStateException("No element left.");
				removed = false;
				return (T) list.get(++pos);
			}
			
			@Override
			public void remove() {
				if (removed) throw new IllegalStateException("Element already removed.");
				if (pos < 0 || pos > end) throw new IllegalStateException("No element left.");
				map.removeKey(keys.get(pos));
				removed = true;
			}
		};
	}
	
	public void clear() {
		map.clear();
	}
	
	public boolean containsKey(int key) {
		return map.containsKey(key);
	}
	
	public boolean containsValue(T value) {
		return map.containsValue(value);
	}
	
	public boolean forEachKey(IntProcedure key) {
		return map.forEachKey(key);
	}
	
	@SuppressWarnings("unchecked")
	public boolean forEachPair(IntProcedureBase<T> procedure) {
		return map.forEachPair((key, value) -> procedure.apply(key, (T) value));
	}
	
	@SuppressWarnings("unchecked")
	public T get(int key) {
		return (T) map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	public int keyOf(T value) {
		return map.keyOf(value);
	}
	
	public IntArrayList keys() {
		IntArrayList out = map.keys();
		out.trimToSize();
		return out;
	}
	
	public void keys(IntArrayList keys) {
		map.keys(keys);
	}
	
	public void keysSortedByValue(IntArrayList keys) {
		map.keysSortedByValue(keys);
	}
	
	public boolean put(int key, T value) {
		return map.put(key, value);
	}
	
	public boolean removeKey(int key) {
		return map.removeKey(key);
	}
	
	public int size() {
		return map.size();
	}
	
	public void trimToSize() {
		map.trimToSize();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> values() {
		return map.values().toList();
	}
	
	@SuppressWarnings("unchecked")
	public void values(final List<T> out) {
		map.values().forEach((value) -> {
			out.add((T) value);
			return true;
		});
	}
}