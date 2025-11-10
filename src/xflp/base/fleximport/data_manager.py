"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import Dict, Set, TYPE_CHECKING

if TYPE_CHECKING:
    from .item_data import ItemData
    from .container_data import ContainerData


class DataManager:
    """
    Manages data mappings for items, shipments, locations, container types,
    and stacking groups.
    """

    def __init__(self):
        """Initialize the data manager with default mappings."""
        self._max_item_id = 0
        self._max_shipment_id = 1
        self._max_location_id = 0
        self._max_stacking_group_id = 1
        self._max_container_type_id = 1

        self._item_map: Dict[str, int] = {}
        self._item_id_map: Dict[int, str] = {}
        self._shipment_map: Dict[str, int] = {}
        self._location_map: Dict[str, int] = {}
        self._stacking_group_map: Dict[str, int] = {}
        self._container_type_map: Dict[str, int] = {}

        # Initialize default values
        self._container_type_map["default_container_type"] = 0
        self._stacking_group_map["default_stacking_group"] = 0
        self._shipment_map["default_shipment"] = 0
        self._location_map[""] = -1

    def add(self, data: 'ItemData | ContainerData') -> None:
        """
        Add data from ItemData or ContainerData.

        Args:
            data: Either ItemData or ContainerData instance
        """
        from .item_data import ItemData
        from .container_data import ContainerData

        if isinstance(data, ItemData):
            self.add_item(data.get_extern_id())
            self.add_shipment(data.get_shipment_id())
            self.add_location(data.get_loading_location())
            self.add_location(data.get_unloading_location())
            self.add_stacking_group(data.get_stacking_group(), data.get_allowed_stacking_groups())
            self.add_container_types(data.get_allowed_container_types())
        elif isinstance(data, ContainerData):
            self.add_container_type(data.get_container_type())

    def add_item(self, item_id: str) -> None:
        """Add an item to the mapping."""
        if item_id not in self._item_map:
            self._item_map[item_id] = self._max_item_id
            self._item_id_map[self._max_item_id] = item_id
            self._max_item_id += 1

    def add_shipment(self, shipment_id: str) -> None:
        """Add a shipment to the mapping."""
        if shipment_id not in self._shipment_map:
            self._shipment_map[shipment_id] = self._max_shipment_id
            self._max_shipment_id += 1

    def add_location(self, location_id: str) -> None:
        """Add a location to the mapping."""
        location_id = location_id.strip().lower()
        if location_id not in self._location_map:
            self._location_map[location_id] = self._max_location_id
            self._max_location_id += 1

    def add_container_type(self, container_type: str) -> None:
        """Add a container type to the mapping."""
        if container_type not in self._container_type_map:
            self._container_type_map[container_type] = self._max_container_type_id
            self._max_container_type_id += 1

    def add_stacking_group(self, stacking_group_id: str, stacking_groups: str) -> None:
        """Add stacking groups to the mapping."""
        stacking_group_id = stacking_group_id.strip().lower()
        if stacking_group_id not in self._stacking_group_map:
            self._stacking_group_map[stacking_group_id] = self._max_stacking_group_id
            self._max_stacking_group_id += 1

        arr = stacking_groups.split(",")
        for s in arr:
            s = s.strip().lower()
            if s not in self._stacking_group_map:
                self._stacking_group_map[s] = self._max_stacking_group_id
                self._max_stacking_group_id += 1

    def add_container_types(self, container_types: str) -> None:
        """Add multiple container types from a comma-separated string."""
        arr = container_types.split(",")
        for s in arr:
            if s not in self._container_type_map:
                self._container_type_map[s] = self._max_container_type_id
                self._max_container_type_id += 1

    def get_item_idx(self, item_id: str) -> int:
        """Get the index for an item ID."""
        return self._item_map[item_id]

    def get_item_id(self, item_idx: int) -> str:
        """Get the item ID for an index."""
        return self._item_id_map[item_idx]

    def get_shipment_idx(self, shipment_id: str) -> int:
        """Get the index for a shipment ID."""
        return self._shipment_map[shipment_id]

    def get_location_idx(self, location_id: str) -> int:
        """Get the index for a location ID."""
        return self._location_map[location_id.strip().lower()]

    def get_stacking_group_idx(self, stacking_group: str) -> int:
        """Get the bit flag index for a stacking group."""
        return 1 << self._stacking_group_map[stacking_group.strip().lower()]

    def get_container_type_idx(self, container_type: str) -> int:
        """Get the index for a container type."""
        return self._container_type_map[container_type]

    def get_container_type_name(self, index: int) -> str:
        """Get the container type name for an index."""
        for key, value in self._container_type_map.items():
            if value == index:
                return key
        return "not found"

    def get_container_types(self, allowed_container_set: str) -> Set[int]:
        """Get a set of container type indices from a comma-separated string."""
        arr = allowed_container_set.split(",")
        res = set()
        for s in arr:
            res.add(self._container_type_map[s])
        return res

    def get_stacking_groups(self, allowed_stacking_groups: str) -> int:
        """Get combined bit flags for allowed stacking groups."""
        res = 0
        arr = allowed_stacking_groups.split(",")
        for s in arr:
            s = s.strip().lower()
            stacking_group = self._stacking_group_map.get(s)
            if stacking_group is not None:
                res += 1 << stacking_group
        return res

    def clear(self) -> None:
        """Clear all mappings and reset counters."""
        self._max_item_id = 0
        self._max_shipment_id = 1
        self._max_location_id = 0
        self._max_stacking_group_id = 1
        self._max_container_type_id = 1

        self._item_map.clear()
        self._shipment_map.clear()
        self._location_map.clear()
        self._stacking_group_map.clear()
        self._container_type_map.clear()

    def clear_items(self) -> None:
        """Clear item-related mappings."""
        self._max_item_id = 0
        self._max_shipment_id = 1
        self._item_id_map.clear()
        self._item_map.clear()
        self._shipment_map.clear()
        self._shipment_map["default_shipment"] = 0

    def reindex_locations(self) -> None:
        """
        Locations must be sorted in their naming. The ID/index is
        used later to identify if a location is earlier in the routing
        of the truck or later.
        """
        locations = sorted(self._location_map.keys())
        self._location_map.clear()
        for location in locations:
            self.add_location(location)
