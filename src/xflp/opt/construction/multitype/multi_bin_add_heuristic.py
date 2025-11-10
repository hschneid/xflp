# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Multi-bin add heuristic for packing items into multiple containers.

Author: hschneid
"""

from typing import List, Optional, TYPE_CHECKING

from xflp.base.position.position_service import PositionService
from xflp.base.monitor.status_code import StatusCode
from xflp.opt.construction.multitype.container_position import ContainerPosition

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.xflp_parameter import XFLPParameter
    from xflp.base.monitor.status_manager import StatusManager
    from xflp.opt.construction.strategy.strategy import Strategy
    from xflp.opt.construction.strategy.base_strategy import BaseStrategy
    from xflp.base.position.position_candidate import PositionCandidate


class MultiBinAddHeuristic:
    """Greedy heuristic for adding items to multiple bins."""

    def __init__(
        self,
        strategy: 'Strategy',
        status_manager: 'StatusManager',
        parameter: 'XFLPParameter'
    ):
        """
        Initialize the multi bin add heuristic.

        Args:
            strategy: The packing strategy to use
            status_manager: Status manager for logging
            parameter: XFLP parameters
        """
        self.strategy = strategy.get_strategy()
        self.status_manager = status_manager
        self.parameter = parameter

    def create_loading_plan(
        self,
        items: List['Item'],
        containers: List['Container']
    ) -> List['Item']:
        """
        Create a loading plan for items in multiple containers.

        Args:
            items: List of items to pack
            containers: List of containers to pack into

        Returns:
            List of unplanned items that couldn't fit

        Raises:
            XFLPException: If packing fails
        """
        unplanned_items: List['Item'] = []

        # Reset eventual presets
        self._reset_items(items)

        for i, item in enumerate(items):
            container_positions = self._get_best_container_positions(
                item, containers, self.strategy
            )

            # Add item to container
            if len(container_positions) > 0:
                if self._reached_max_nbr_of_items(containers, self.parameter):
                    self._set_unplanned(unplanned_items, *items[i:])
                    break

                self._insert_into_container(container_positions)
            else:
                self._set_unplanned(unplanned_items, item)

        return unplanned_items

    def _reached_max_nbr_of_items(
        self,
        containers: List['Container'],
        parameter: 'XFLPParameter'
    ) -> bool:
        """Check if max number of items reached across all containers."""
        total_items = sum(len(c.get_items()) for c in containers)
        return total_items >= parameter.get_max_nbr_of_items()

    def _set_unplanned(self, unplanned_items: List['Item'], *items: 'Item') -> None:
        """Mark items as unplanned."""
        for item in items:
            self.status_manager.fire_message(
                StatusCode.RUNNING,
                f"Item {item.index} could not be added."
            )
            unplanned_items.append(item)

    def _get_best_container_positions(
        self,
        item: 'Item',
        containers: List['Container'],
        strategy: 'BaseStrategy'
    ) -> List[ContainerPosition]:
        """
        Get best positions for an item across all containers.

        Args:
            item: The item to place
            containers: List of containers
            strategy: The packing strategy

        Returns:
            List of container positions

        Raises:
            XFLPException: If position finding fails
        """
        container_positions: List[ContainerPosition] = []

        for container in containers:
            best_position = self._get_best_insert_position(item, container, strategy)
            if best_position is not None:
                container_positions.append(ContainerPosition(container, best_position))

        return container_positions

    def _get_best_insert_position(
        self,
        item: 'Item',
        container: 'Container',
        strategy: 'BaseStrategy'
    ) -> Optional['PositionCandidate']:
        """
        Get best insert position for an item in a container.

        Args:
            item: The item to place
            container: The container
            strategy: The packing strategy

        Returns:
            Best position candidate or None

        Raises:
            XFLPException: If position finding fails
        """
        # Check if item is allowed to this container type
        if container.is_item_allowed(item):
            # Fetch existing insert positions
            pos_list = PositionService.find_position_candidates(container, item)

            if len(pos_list) > 0:
                # Choose according to select strategy
                return strategy.choose(item, container, pos_list)

        return None

    def _insert_into_container(self, container_positions: List[ContainerPosition]) -> None:
        """
        Insert item into the first available container position.

        Args:
            container_positions: List of container positions
        """
        # Simply take first - Could be improved later
        container_position = container_positions[0]
        container_position.get_container().add(
            container_position.get_position().item,
            container_position.get_position().position,
            container_position.get_position().is_rotated
        )

    def _reset_items(self, items: List['Item']) -> None:
        """Reset all items."""
        for item in items:
            item.reset()
