"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

Item data class defines all fields which can be used in XFLP suite.
Users can call the set-methods to insert data into this object.
There is no predefined sequence of doing that. All fields are
initialized by default values in that way that restrictions always hold.

Each set-method returns the item data object itself, so that the
set-methods can be called in one way (builder pattern).
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.item import Item
    from .data_manager import DataManager


class ItemData:
    """
    Data class for item import using builder pattern.
    """

    def __init__(self):
        """Initialize item data with default values."""
        self._extern_id: str = ""
        self._shipment_id: str = "default_shipment"

        self._width: int = -1
        self._length: int = -1
        self._height: int = -1
        self._immersive_depth: int = 0

        self._weight: float = 0
        self._stacking_weight_limit: float = float('inf')

        self._stacking_group: str = "default_stacking_group"
        self._allowed_stacking_groups: str = "default_stacking_group"
        self._allowed_container_set: str = "default_container_type"
        self._nbr_of_allowed_stacked_items: int = 2**31 - 1  # Integer.MAX_VALUE

        self._loading_location: str = ""
        self._unloading_location: str = ""

        self._spinnable: bool = True

    # Setter methods (builder pattern)

    def set_extern_id(self, extern_id: str) -> 'ItemData':
        """Set the external ID."""
        self._extern_id = extern_id
        return self

    def set_shipment_id(self, shipment_id: str) -> 'ItemData':
        """Set the shipment ID."""
        self._shipment_id = shipment_id
        return self

    def set_width(self, width: int) -> 'ItemData':
        """Set the width."""
        self._width = width
        return self

    def set_length(self, length: int) -> 'ItemData':
        """Set the length."""
        self._length = length
        return self

    def set_height(self, height: int) -> 'ItemData':
        """Set the height."""
        self._height = height
        return self

    def set_weight(self, weight: float) -> 'ItemData':
        """Set the weight."""
        self._weight = weight
        return self

    def set_stacking_weight_limit(self, stacking_weight_limit: float) -> 'ItemData':
        """Set the stacking weight limit."""
        self._stacking_weight_limit = stacking_weight_limit
        return self

    def set_stacking_group(self, stacking_group: str) -> 'ItemData':
        """Set the stacking group."""
        self._stacking_group = stacking_group
        return self

    def set_allowed_stacking_groups(self, allowed_stacking_groups: str) -> 'ItemData':
        """Set the allowed stacking groups."""
        self._allowed_stacking_groups = allowed_stacking_groups
        return self

    def set_allowed_container_set(self, allowed_container_set: str) -> 'ItemData':
        """Set the allowed container set."""
        self._allowed_container_set = allowed_container_set
        return self

    def set_loading_location(self, loading_location: str) -> 'ItemData':
        """Set the loading location."""
        self._loading_location = loading_location
        return self

    def set_unloading_location(self, unloading_location: str) -> 'ItemData':
        """Set the unloading location."""
        self._unloading_location = unloading_location
        return self

    def set_spinnable(self, spinnable: bool) -> 'ItemData':
        """Set whether the item is spinnable (rotatable)."""
        self._spinnable = spinnable
        return self

    def set_nbr_of_allowed_stacked_items(self, nbr_of_allowed_stacked_items: int) -> 'ItemData':
        """
        Set the number of allowed items below this item when it will be stacked.
        1 means that this item must be stacked on only one other item.
        """
        self._nbr_of_allowed_stacked_items = nbr_of_allowed_stacked_items
        return self

    def set_immersive_depth(self, immersive_depth: int) -> 'ItemData':
        """
        Set the immersive depth. If items have special form groups at the top or bottom
        (shoulder or feet), during stacking the lower and upper item dive into each other
        and the overall height is reduced. The amount of reduced height is the immersive depth.
        """
        self._immersive_depth = immersive_depth
        return self

    # Getter methods

    def get_extern_id(self) -> str:
        """Get the external ID."""
        return self._extern_id

    def get_shipment_id(self) -> str:
        """Get the shipment ID."""
        return self._shipment_id

    def get_width(self) -> int:
        """Get the width."""
        return self._width

    def get_length(self) -> int:
        """Get the length."""
        return self._length

    def get_height(self) -> int:
        """Get the height."""
        return self._height

    def get_weight(self) -> float:
        """Get the weight."""
        return self._weight

    def get_stacking_weight_limit(self) -> float:
        """Get the stacking weight limit."""
        return self._stacking_weight_limit

    def get_stacking_group(self) -> str:
        """Get the stacking group."""
        return self._stacking_group

    def get_allowed_stacking_groups(self) -> str:
        """Get the allowed stacking groups."""
        return self._allowed_stacking_groups

    def get_allowed_container_types(self) -> str:
        """Get the allowed container types."""
        return self._allowed_container_set

    def get_loading_location(self) -> str:
        """Get the loading location."""
        return self._loading_location

    def get_unloading_location(self) -> str:
        """Get the unloading location."""
        return self._unloading_location

    def get_immersive_depth(self) -> int:
        """Get the immersive depth."""
        return self._immersive_depth

    def is_spinable(self) -> bool:
        """Check if the item is spinnable."""
        return self._spinnable

    def create_loading_item(self, manager: 'DataManager') -> 'Item':
        """Create a loading item from this data."""
        item = self._create_item(manager)
        item.set_loading(True)  # is Loading
        item.post_init()
        return item

    def create_unloading_item(self, manager: 'DataManager') -> 'Item':
        """Create an unloading item from this data."""
        item = self._create_item(manager)
        item.set_loading(False)  # is Unloading
        item.post_init()
        return item

    def _create_item(self, manager: 'DataManager') -> 'Item':
        """Create an item instance with data from this ItemData."""
        from xflp.base.item import Item

        item = Item()
        item.set_external_index(manager.get_item_idx(self._extern_id))
        item.set_order_index(manager.get_shipment_idx(self._shipment_id))
        item.set_loading_loc(manager.get_location_idx(self._loading_location))
        item.set_un_loading_loc(manager.get_location_idx(self._unloading_location))
        item.set_w(self._width)
        item.set_l(self._length)
        item.set_h(self._height)
        item.set_weight(self._weight)
        item.set_stacking_weight_limit(self._stacking_weight_limit)
        item.set_allowed_container_set(manager.get_container_types(self._allowed_container_set))
        item.set_stacking_group(manager.get_stacking_group_idx(self._stacking_group))
        item.set_allowed_stacking_groups(manager.get_stacking_groups(self._allowed_stacking_groups))
        item.set_nbr_of_allowed_stacked_items(self._nbr_of_allowed_stacked_items)
        item.set_immersive_depth(self._immersive_depth)
        item.set_spinable(self._spinnable)

        return item
