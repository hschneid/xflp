package util.collection;

import cern.colt.function.IntProcedure;
import cern.colt.list.IntArrayList;
import cern.colt.map.OpenIntIntHashMap;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 **/
public class IntSet {
	OpenIntIntHashMap map = new OpenIntIntHashMap();
	
	public IntSet() {}
	
	public IntSet(IntArrayList al) {
		al.forEach(p -> {add(p); return true;});
	}
	
	public IntSet(int... iA) {
		addAll(iA);
	}

	public void add(int i) {
		map.put(i, 1);
	}

	public void addAll(int... iA) {
		for (int i : iA) add(i);
	}

	public boolean contains(int i) {
		return map.containsKey(i);
	}

	public void remove(int i) {
		map.removeKey(i);
	}
	
	public void removeAll(int... iA) {
		for (int i : iA) remove(i);
	}

	public int size() {
		return map.size();
	}
	
	public synchronized int[] toArray() {
		map.trimToSize();
		return map.keys().copy().elements();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void clear() {
		map.clear();
	}
	
	public void forEachItem(IntProcedure p) {
		map.forEachKey(p);
	}

	public IntArrayList elements() {
		return map.keys();
	}
}

