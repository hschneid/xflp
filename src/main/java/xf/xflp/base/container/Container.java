package xf.xflp.base.container;

import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

import java.util.List;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public interface Container {

    /**
     *  Creates a new container instance. The master data of the called container are initialized.
     *  The new container is empty.
     */
    Container newInstance();

    /**
     * Adds an item at given position to current container.
     * New positions will be created and covered positions get inactive.
     *
     * Returns the index of the inserted item for faster access
     */
    int add(Item item, Position position, boolean isRotated);

    /**
     * Removes the item from container.
     * Uncovered positions will be freed.
     */
    void remove(Item item);

    /**
     * Returns the already inserted items of this container
     */
    List<Item> getItems();

    /**
     * History when and how a certain item is loaded or unloaded
     * This is relevant for creating the solution report
     **/
    List<Item> getHistory();

    /**
     * Check if the given item is allowed to be placed at this container
     */
    boolean isItemAllowed(Item item);

    /**
     * Returns the list of active positions, which can be used to add an item here.
     */
    List<Position> getActivePositions();

    /**
     * Returns the type of the container, which is necessary for multi-container-type problems
     */
    int getContainerType();

    /**
     * Returns the width of the container
     */
    int getWidth();

    /**
     * Returns the length of the container
     */
    int getLength();

    /**
     * Returns the height of the container
     */
    int getHeight();

    /**
     * Returns the maximal weight, which can be loaded by the container
     */
    float getMaxWeight();

    /**
     * Returns the loaded volume (volume of inserted items)
     */
    long getLoadedVolume();

    /**
     * Returns the loaded weight (weight of inserted items)
     */
    float getLoadedWeight();

    /**
     * Returns the paramter for behaviour of this container
     */
    ContainerParameter getParameter();

    /**
     * Returns internal data structures of container
     */
    ContainerBaseData getBaseData();
}
