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
public class IndexedArrayList<E extends Indexable> extends ArrayList<E> {

	private static final long serialVersionUID = -5000228990659431148L;

	private transient int[] freeIndexArr;
	private int freeIndexCursor = -1;
	private int length = 0;

	/**
	 * 
	 */
	public IndexedArrayList() {
		super();
		freeIndexArr = new int[10];
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		length++;
		if(freeIndexCursor != -1) {
			int insertPos = freeIndexArr[freeIndexCursor--];
			e.setIdx(insertPos);
			super.set(insertPos, e);
			return true;
		} 
		
		e.setIdx(super.size());
		return super.add(e);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, E e) {
		length++;
		if(index >= super.size()) {
			ensureCapacity(index + 10);
			expand((index - super.size()) + 1);
		}
		e.setIdx(index);
		super.set(index, e);
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
	 * @see java.util.ArrayList#set(int, java.lang.Object)
	 */
	@Override
	public E set(int index, E e) {
		if(index >= super.size()) {
			this.add(index, e);
		}
		E oldEntry = super.get(index);

		e.setIdx(index);
		super.set(index, e);

		return oldEntry;
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
		e.setIdx(-1);
		super.set(index, null);

		// Wenn der freeIndexArray zu klein wird, vergrößer ihn um das 1,5 fache
		freeIndexCursor++;
		if(freeIndexCursor == freeIndexArr.length) {
			int[] newFreeIndexArr = new int[(freeIndexArr.length * 3)/2];
			System.arraycopy(freeIndexArr, 0, newFreeIndexArr, 0, freeIndexArr.length);
			this.freeIndexArr = newFreeIndexArr;
		}
		freeIndexArr[freeIndexCursor] = index;

		return e;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		if(!(o instanceof Indexable))
			return false;
		int idx = ((Indexable)o).getIdx();
		if(super.get(idx) != o)
			return false;
		this.remove(idx);
		return true;
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
		return super.size();
	}
	public int size() {
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
		freeIndexArr = new int[10];
		freeIndexCursor = -1;
	}

	private void expand(int nbr) {
		for (int i = 0; i < nbr; i++) {
			super.add(null);
		}
	}
}
