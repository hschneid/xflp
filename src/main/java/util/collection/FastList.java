package util.collection;

import java.util.List;

/**
 * Copyright (c) 2012-2026 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 * Interface for adding additional methods to List interface.
 *
 * Majorly the methods are related to access correct length values as the
 * improved function are pointing to multiple lengths:
 * - Number of free slots in ArrayList (normal)
 * - Number of entered objects in list, which are not null.
 * - Number of highest index, where a non-null object is placed.
 */
public interface FastList<E> extends List<E> {

    /**
     * Number of entered objects in list, which are not null.
     *
     * @return int
     */
    int length();

    /**
     * Number of highest index, where a non-null object is placed.
     *
     * @return int
     */
    int getLastUsedIndex();
}
