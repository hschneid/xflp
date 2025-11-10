"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.container import GroundContactRule
    from xflp.opt.construction.strategy import Strategy


class XFLPParameter:
    """
    Parameters for XFLP optimization.
    """

    def __init__(self):
        """Initialize with default parameters."""
        self._lifo_importance: float = 0.0
        self._max_nbr_of_container: int = 2**31 - 1  # Integer.MAX_VALUE
        self._preferred_packing_strategy = None  # Will be set to Strategy.HIGH_LOW_LEFT
        self._nbr_of_allowed_stacked_items: int = 2**31 - 1
        self._ground_contact_rule = None  # Will be set to GroundContactRule.FREE
        self._max_nbr_of_items: int = 2**31 - 1

        # Import here to avoid circular imports
        from xflp.base.container import GroundContactRule
        from xflp.opt.construction.strategy import Strategy

        self._preferred_packing_strategy = Strategy.HIGH_LOW_LEFT
        self._ground_contact_rule = GroundContactRule.FREE

    def clear(self) -> None:
        """Clear parameters (currently does nothing)."""
        pass

    def get_lifo_importance(self) -> float:
        """Get the LIFO importance."""
        return self._lifo_importance

    def set_lifo_importance(self, lifo_importance: float) -> None:
        """Set the LIFO importance."""
        self._lifo_importance = lifo_importance

    def get_preferred_packing_strategy(self) -> 'Strategy':
        """
        Get the preferred packing strategy.

        The packing strategy is used by the placing algorithm to choose the best
        next insert position.

        This value must be chosen from enum:
        - HIGH_LOW_LEFT: Choose highest, deepest and most left position
        - TOUCHING_PERIMETER: Choose the position where new item will touch as much
          as possible already placed items. If there are multiple best positions,
          choose with HIGH_LOW_LEFT.
        - WIDTH_PROPORTION: Choose the position where the width of the new item is
          nearest to a full proportion of the container width. If there are multiple
          best positions, choose with TOUCHING_PERIMETER.

        Default: HIGH_LOW_LEFT
        """
        return self._preferred_packing_strategy

    def set_preferred_packing_strategy(self, preferred_packing_strategy: 'Strategy') -> None:
        """Set the preferred packing strategy."""
        self._preferred_packing_strategy = preferred_packing_strategy

    def get_max_nbr_of_container(self) -> int:
        """Get the maximum number of containers."""
        return self._max_nbr_of_container

    def set_max_nbr_of_container(self, max_nbr_of_container: int) -> None:
        """Set the maximum number of containers."""
        self._max_nbr_of_container = max_nbr_of_container

    def get_nbr_of_allowed_stacked_items(self) -> int:
        """Get the number of allowed stacked items."""
        return self._nbr_of_allowed_stacked_items

    def set_nbr_of_allowed_stacked_items(self, nbr_of_allowed_stacked_items: int) -> None:
        """
        Set the maximal number of items which are allowed to be placed/stacked on top of an item.

        If value is set to 1, then the algorithm can place/stack only one other item
        on top of any item.

        Default: No limitation
        """
        self._nbr_of_allowed_stacked_items = nbr_of_allowed_stacked_items

    def get_ground_contact_rule(self) -> 'GroundContactRule':
        """Get the ground contact rule."""
        return self._ground_contact_rule

    def set_ground_contact_rule(self, ground_contact_rule: 'GroundContactRule') -> None:
        """Set the ground contact rule."""
        self._ground_contact_rule = ground_contact_rule

    def get_max_nbr_of_items(self) -> int:
        """Get the maximum number of items."""
        return self._max_nbr_of_items

    def set_max_nbr_of_items(self, max_nbr_of_items: int) -> None:
        """Set the maximum number of items."""
        self._max_nbr_of_items = max_nbr_of_items
