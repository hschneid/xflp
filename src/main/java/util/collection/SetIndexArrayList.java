package util.collection;

import java.util.ArrayList;
import java.util.Collection;

/** 
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * Indexed list
 * - Entries have the index of their position in the list
 * - Adding or Removing will not shift entry positions
 * - Reuse of empty slots
 * 
 * @author hschneid
 *
 * @param <E>
 */
public class SetIndexArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -5000228990659431148L;

	private int length = 0;
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, E e) {
		length++;
		if(index >= this.size()) {
			ensureCapacity(index + 10);
			expand((index - this.size()) + 1);
		}

		super.set(index, e);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#set(int, java.lang.Object)
	 */
	@Override
	public E set(int index, E e) {
		if(index >= this.size())
			this.add(index, e);
		
		E oldEntry = this.get(index);
		super.set(index, e);
		
		return oldEntry;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#remove(int)
	 */
	@Override
	public E remove(int index) {
		E e = super.get(index);
		if(e == null)
			return null;
		
		length--;

		super.set(index, null);
		
		return e;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.AbstractCollection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	public int length() {
		return length;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		length = 0;
	}
	
	private void expand(int nbr) {
		for (int i = 0; i < nbr; i++) {
			super.add(null);
		}
	}
}
