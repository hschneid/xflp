package util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import util.Copyable;

/** 
 * Copyright (c) 2012-present Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author Hogo
 *
 * @param <K>
 * @param <E>
 */
public class LPListMap<K, E> implements Copyable<LPListMap<K,E>> {

	private HashMap<K, List<E>> map;

	/**
	 * 
	 */
	public LPListMap() {
		map = new HashMap<>();
	}

	/**
	 * 
	 * @param size
	 */
	public LPListMap(int size) {
		map = new HashMap<>(size);
	}

	/**
	 * 
	 * @param lm
	 */
	public LPListMap(LPListMap<K, E> lm) {
		map = new HashMap<>(lm.size());
		for (K k : lm.keySet())
			put(k, lm.get(k));

	}

	/**
	 * 
	 * @param key
	 * @param element
	 */
	public void put(K key, E element) {
		if(map.containsKey(key)) {
			map.get(key).add(element);
		} else {
			List<E> list = new ArrayList<>();
			list.add(element);
			map.put(key, list);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param element
	 */
	public void putSave(K key, E element) {
		if(map.containsKey(key) && map.get(key) != null) {
			map.get(key).add(element);
		} else {
			List<E> list = new ArrayList<>();
			list.add(element);
			map.put(key, list);
		}
	}

	/**
	 * 
	 * @param key
	 * @param list
	 */
	public void put(K key, List<E> list) {
		if(map.containsKey(key) && map.get(key) != null)
			map.get(key).addAll(list);
		else
			map.put(key, list);
	}
	
	/**
	 * 
	 * @param key
	 * @param list
	 */
	public void putSave(K key, List<E> list) {
		if(list != null) {
			if(map.containsKey(key) && map.get(key) != null)
				map.get(key).addAll(list);
			else
				map.put(key, list);
		}
	}

	/**
	 * 
	 * @param key
	 */
	public void remove(K key) {
		map.remove(key);
	}

	/**
	 * 
	 * @param key
	 * @param element
	 */
	public void remove(K key, E element) {
		if(map.containsKey(key))
			map.get(key).remove(element);
	}

	/**
	 * 
	 * @param key
	 * @param element
	 */
	public void removeFully(K key, E element) {
		if(map.containsKey(key)) {
			map.get(key).remove(element);
			if(map.get(key).isEmpty())
				remove(key);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public List<E> get(K key) {
		return map.get(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	/**
	 * 
	 * @return
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return map.size();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Iterator<E> iterator(K key) {
		if(map.containsKey(key))
			return map.get(key).iterator();

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see da.util.Copyable#copy()
	 */
	@Override
	public LPListMap<K, E> copy() {
		LPListMap<K, E> copy = new LPListMap<>(size());

		for (K key : keySet()) {
			List<E> list = get(key);
			List<E> newList = new ArrayList<>(list.size());
			Collections.copy(newList, list);

			copy.put(key, newList);
		}

		return copy;
	}

	/**
	 * 
	 * @param list
	 * @param e
	 */
	public void removeAll(List<K> list, E e) {
		for (K k : list)
			removeFully(k, e);
	}

	/**
	 * @param list
	 * @param e
	 */
	public void put(List<K> list, E e) {
		for (K k : list)
			put(k, e);
	}
	
	/**
	 * @param list
	 * @param e
	 */
	public void putSave(List<K> list, E e) {
		for (K k : list)
			putSave(k, e);
	}

	/**
	 * @param key
	 * @return
	 */
	public List<E> getSave(K key) {
		List<E> l = null;
		if(map.containsKey(key)) {
			l = map.get(key);
			if(l == null) {
				l = new ArrayList<>();
				map.put(key, l);
			}
		}

		return l;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public List<E> getSave2(K key) {
		if(map.containsKey(key)) {
			List<E> l = map.get(key);
			if(l == null) {
				l = new ArrayList<>();
				map.put(key, l);
			}
			return l;
		}
		return new ArrayList<>();
	}
	
	/**
	 * 
	 */
	public void clear() {
		map.clear();
	}
}
