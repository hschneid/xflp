"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

FlexiImporter is the class which holds the concept for flexible and adaptive
import of data into the XFLP suite. The general points are:
- abstraction of data handling between chaotic user and stable algorithms
- abstraction of import sequence (which data is first?)
- Easy-to-fill data objects by eliminating import methods (set/add) with static parameters
- Handling of vehicles (default or fleet)
- Assigning the vehicle priority by sorting the vehicles with their capacities

After import, collected data can be accessed by XFLP suite.
"""

from typing import List, TYPE_CHECKING

from .data_manager import DataManager
from .item_data import ItemData
from .container_data import ContainerData

if TYPE_CHECKING:
    from xflp.base.item import Item
    from xflp.base.container import Container
    from xflp.base import XFLPParameter


class FlexiImporter:
    """
    Flexible importer for XFLP data with builder pattern support.
    """

    def __init__(self):
        """Initialize the flexible importer."""
        self._data_manager = DataManager()

        self._item_list: List[ItemData] = []
        self._container_list: List[ContainerData] = []

        self._last_item_data: ItemData | None = None
        self._last_container_data: ContainerData | None = None

    def finish_import(self) -> None:
        """
        Finalize the import process. All achieved data objects are
        finalized and inserted into the data lists.
        """
        if self._last_item_data is not None:
            self._data_manager.add(self._last_item_data)
            self._item_list.append(self._last_item_data)
            self._last_item_data = None

        if self._last_container_data is not None:
            self._data_manager.add(self._last_container_data)
            self._container_list.append(self._last_container_data)
            self._last_container_data = None

        self._data_manager.reindex_locations()

    def get_item_data(self) -> ItemData:
        """
        Achieve an item data object where user can import data in any sequence.
        The call of this method means that the last achieved item data object
        is finalized and added to the internal item list.

        Returns:
            ItemData object for building item configuration
        """
        if self._last_item_data is not None:
            self._data_manager.add(self._last_item_data)
            self._item_list.append(self._last_item_data)

        self._last_item_data = ItemData()
        return self._last_item_data

    def get_container_data(self) -> ContainerData:
        """
        Achieve a container data object where user can import data in any sequence.
        The call of this method means that the last achieved container data object
        is finalized and added to the internal container list.

        By achieving a container data object, the default container is put out of
        container list. So the default container parameter have to be announced in
        specific own container data.

        Returns:
            ContainerData object for building container configuration
        """
        if self._last_container_data is not None:
            self._data_manager.add(self._last_container_data)
            self._container_list.append(self._last_container_data)

        self._last_container_data = ContainerData()
        return self._last_container_data

    def clear(self) -> None:
        """Clears all internal data lists and reset the internal fields."""
        self._item_list.clear()
        self._container_list.clear()

        self._last_item_data = None
        self._last_container_data = None

        self._data_manager.clear()

    def clear_items(self) -> None:
        """Clears all imported items."""
        self._item_list.clear()
        self._last_item_data = None
        self._data_manager.clear_items()

    def clear_containers(self) -> None:
        """Removes all inserted containers and reset the planning parameters to default."""
        self._container_list.clear()
        self._last_container_data = None

    def get_item_list(self) -> List[ItemData]:
        """Get the collected item data objects."""
        return self._item_list

    def get_converted_item_list(self) -> List['Item']:
        """
        Transform imported items into loading and unloading items.

        Returns:
            List of converted Item instances
        """
        items = []
        for item_data in self._item_list:
            item = item_data.create_loading_item(self._data_manager)
            if len(item_data.get_unloading_location()) > 0:
                items.append(item)
                items.append(item_data.create_unloading_item(self._data_manager))
            else:
                items.append(item)

        # Filter out None items
        items = [item for item in items if item is not None]

        # Check if we have locations
        has_locations = len(set(
            loc for item in items
            for loc in [item.loading_loc, item.un_loading_loc]
        )) > 1

        if has_locations:
            # Sort items by location and loading/unloading
            def sort_key(item: 'Item'):
                loc = item.loading_loc if item.is_loading else item.un_loading_loc
                return (
                    loc,
                    not item.is_loading,  # Loading items first (False < True)
                    -item.get_un_loading_loc() if item.is_loading else item.get_un_loading_loc(),
                    item.get_idx()
                )

            items.sort(key=sort_key)

        return items

    def get_container_list(self) -> List[ContainerData]:
        """Get the collected container data objects."""
        return self._container_list

    def get_converted_container_list(self, items: List['Item'],
                                    parameter: 'XFLPParameter') -> List['Container']:
        """
        Convert container data to Container instances.

        Args:
            items: List of items to be packed
            parameter: XFLP parameters

        Returns:
            List of Container instances
        """
        is_adding_and_removing_items = self._check_for_add_remove(items)

        return [
            con.create(self._data_manager, parameter, is_adding_and_removing_items)
            for con in self._container_list
        ]

    def _check_for_add_remove(self, items: List['Item']) -> bool:
        """Check if any items have unloading locations."""
        for item in items:
            if item.get_un_loading_loc() != -1:
                return True
        return False

    def get_data_manager(self) -> DataManager:
        """Get the data manager."""
        return self._data_manager
