# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This class presents a function to add the given items to a given single container.
All unfitting items will be returned.

The used algorithm to add items is a greedy heuristic. It takes the order of given items
and places one after another to the best available position in container. The best position
is chosen by a strategy.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.base.position.position_service import PositionService
from xflp.base.monitor.status_code import StatusCode

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.xflp_parameter import XFLPParameter
    from xflp.base.monitor.status_manager import StatusManager
    from xflp.opt.construction.strategy.strategy import Strategy
    from xflp.opt.construction.strategy.base_strategy import BaseStrategy


class SingleBinAddHeuristic:
    """Greedy heuristic for adding items to a single bin."""

    def __init__(
        self,
        strategy: 'Strategy',
        status_manager: 'StatusManager',
        parameter: 'XFLPParameter'
    ):
        """
        Initialize the single bin add heuristic.

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
        container: 'Container'
    ) -> List['Item']:
        """
        Create a loading plan for items in a single container.

        Args:
            items: List of items to pack
            container: The container to pack into

        Returns:
            List of unplanned items that couldn't fit

        Raises:
            XFLPException: If packing fails
        """
        unplanned_items: List['Item'] = []

        # Reset eventual presets
        self._reset_items(items)

        for i, item in enumerate(items):
            insert_position = None

            # Check if item is allowed to this container type
            if container.is_item_allowed(item):
                # Fetch existing insert positions
                pos_list = PositionService.find_position_candidates(container, item)

                if len(pos_list) > 0:
                    # Choose according to select strategy
                    insert_position = self.strategy.choose(item, container, pos_list)

            # Add item to container
            if insert_position is not None:
                if self._reached_max_nbr_of_items(container, self.parameter):
                    self._set_unplanned(unplanned_items, *items[i:])
                    break

                container.add(
                    insert_position.item,
                    insert_position.position,
                    insert_position.is_rotated
                )
            else:
                self._set_unplanned(unplanned_items, item)

        return unplanned_items

    def _reset_items(self, items: List['Item']) -> None:
        """Reset all items."""
        for item in items:
            item.reset()

    def _reached_max_nbr_of_items(
        self,
        container: 'Container',
        parameter: 'XFLPParameter'
    ) -> bool:
        """Check if max number of items reached."""
        return len(container.get_items()) >= parameter.get_max_nbr_of_items()

    def _set_unplanned(self, unplanned_items: List['Item'], *items: 'Item') -> None:
        """Mark items as unplanned."""
        for item in items:
            self.status_manager.fire_message(
                StatusCode.RUNNING,
                f"Item {item.index} could not be added."
            )
            unplanned_items.append(item)
