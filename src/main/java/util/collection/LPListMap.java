package util.collection;

import util.Copyable;

import java.util.*;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 * @author Hogo
 *
 * @param <K> Key
 * @param <E> Value
 */
public class LPListMap<K, E> implements Copyable<LPListMap<K,E>> {

	private final HashMap<K, List<E>> map;

	/**
	 * 
	 */
	public LPListMap() {
		map = new HashMap<>();
	}

	public LPListMap(int size) {
		map = new HashMap<>(size);
	}

	public void put(K key, E element) {
		if(map.containsKey(key)) {
			map.get(key).add(element);
		} else {
			List<E> list = new ArrayList<>();
			list.add(element);
			map.put(key, list);
		}
	}
	
	public void putSave(K key, E element) {
		if(map.containsKey(key) && map.get(key) != null) {
			map.get(key).add(element);
		} else {
			List<E> list = new ArrayList<>();
			list.add(element);
			map.put(key, list);
		}
	}

	public void put(K key, List<E> list) {
		if(map.containsKey(key) && map.get(key) != null)
			map.get(key).addAll(list);
		else
			map.put(key, list);
	}
	
	public void putSave(K key, List<E> list) {
		if(list != null) {
			if(map.containsKey(key) && map.get(key) != null)
				map.get(key).addAll(list);
			else
				map.put(key, list);
		}
	}

	public void remove(K key) {
		map.remove(key);
	}

	public void remove(K key, E element) {
		if(map.containsKey(key))
			map.get(key).remove(element);
	}

	public void removeFully(K key, E element) {
		if(map.containsKey(key)) {
			map.get(key).remove(element);
			if(map.get(key).isEmpty())
				remove(key);
		}
	}

	public List<E> get(K key) {
		return map.get(key);
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public int size() {
		return map.size();
	}

	public Iterator<E> iterator(K key) {
		if(map.containsKey(key))
			return map.get(key).iterator();

		return null;
	}

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

	public void removeAll(List<K> list, E e) {
		for (K k : list)
			removeFully(k, e);
	}

	public void put(List<K> list, E e) {
		for (K k : list)
			put(k, e);
	}
	
	public void putSave(List<K> list, E e) {
		for (K k : list)
			putSave(k, e);
	}

	public List<E> getSave(K key) {
		List<E> l = null;
		if(map.containsKey(key)) {
			l = map.computeIfAbsent(key, k -> new ArrayList<>());
		}

		return l;
	}

	public List<E> getSave2(K key) {
		if(map.containsKey(key)) {
			return map.computeIfAbsent(key, k -> new ArrayList<>());
		}
		return new ArrayList<>();
	}
	
	public void clear() {
		map.clear();
	}
}
