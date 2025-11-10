"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

The XFLPModel holds all necessary input data.
"""

from typing import List, TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.container import Container
    from xflp.base.item import Item
    from xflp.base.monitor import StatusManager
    from .xflp_parameter import XFLPParameter


class XFLPModel:
    """
    The XFLPModel holds all necessary input data for the optimization.
    """

    def __init__(self, items: List['Item'], container_types: List['Container'],
                 parameter: 'XFLPParameter', status_manager: 'StatusManager'):
        """
        Initialize an optimization model object with the given input data.

        Args:
            items: Array of items to be packed
            container_types: Array of container types (only one per type, will be copied)
            parameter: General parameters for all optimization procedures
            status_manager: Manager for status monitoring
        """
        self._items = items
        self._container_types = container_types
        self._parameter = parameter
        self._status_manager = status_manager

        # Result objects
        self._containers: List['Container'] = []
        self._unplanned_items: List['Item'] = []

    def get_container_types(self) -> List['Container']:
        """Get the container types."""
        return self._container_types

    def get_parameter(self) -> 'XFLPParameter':
        """Get the parameters."""
        return self._parameter

    def get_items(self) -> List['Item']:
        """Get the items."""
        return self._items

    def get_containers(self) -> List['Container']:
        """Get the result containers."""
        return self._containers

    def set_containers(self, containers: List['Container']) -> None:
        """Set the result containers."""
        self._containers = containers

    def get_unplanned_items(self) -> List['Item']:
        """Get the unplanned items."""
        return self._unplanned_items

    def set_unplanned_items(self, unplanned_items: List['Item']) -> None:
        """Set the unplanned items."""
        self._unplanned_items = unplanned_items

    def set_items(self, items: List['Item']) -> None:
        """Set the items."""
        self._items = items

    def get_status_manager(self) -> 'StatusManager':
        """Get the status manager."""
        return self._status_manager
