# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This strategy is created by transport business case, where mostly rectangular boxes
are stacked. Main focus is on identifying open stacks with same or smaller base. So
the height is used most properly.

Prerequisites:
 - 1 item can be placed only on 1 item. In reality this happens if items have special shoulder-feet-groups.
 - The items are sorted that similar items are packed in stream. So, open stacks are filled with same base items directly,
   without polluting a stack with smaller items.

Author: hschneid
"""

from typing import List, Optional, TYPE_CHECKING

from xflp.opt.construction.strategy.base_strategy import BaseStrategy
from xflp.opt.construction.strategy.highest_lower_left import HighestLowerLeft
from xflp.opt.construction.strategy.width_proportion_factor import WidthProportionFactor
from xflp.exception.xflp_exception import XFLPException, XFLPExceptionType

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.position.position_candidate import PositionCandidate


class SameBaseStrategy(BaseStrategy):
    """
    Strategy that prioritizes stacking items with same or smaller base dimensions.
    """

    def __init__(self):
        """Initialize the same base strategy."""
        self.high_low = HighestLowerLeft()
        self.width_proportion = WidthProportionFactor()

    def choose(
        self,
        item: 'Item',
        container: 'Container',
        pos_list: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Choose position, preferring same or smaller base stacking.

        Args:
            item: The item to be placed
            container: The container
            pos_list: List of position candidates

        Returns:
            The chosen position candidate

        Raises:
            XFLPException: If pos_list is empty or None
        """
        if pos_list is None or len(pos_list) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "List of positions must be not empty or null."
            )

        # Check if there's a stack with same base
        same_base_position = self._check_same_base_stack(item, container, pos_list)
        if same_base_position is not None:
            return same_base_position

        return self._find_position(container, pos_list)

    def _check_same_base_stack(
        self,
        item: 'Item',
        container: 'Container',
        pos_list: List['PositionCandidate']
    ) -> Optional['PositionCandidate']:
        """
        Check for positions on stacks with same or smaller base.

        Args:
            item: The item to be placed
            container: The container
            pos_list: List of position candidates

        Returns:
            Best same/smaller base position or None
        """
        same_base_positions: List['PositionCandidate'] = []
        smaller_base_positions: List['PositionCandidate'] = []

        self._find_base_positions(
            item,
            container,
            pos_list,
            same_base_positions,
            smaller_base_positions
        )

        return self._choose_base_position(same_base_positions, smaller_base_positions)

    def _find_base_positions(
        self,
        item: 'Item',
        container: 'Container',
        pos_list: List['PositionCandidate'],
        same_base_positions: List['PositionCandidate'],
        smaller_base_positions: List['PositionCandidate']
    ) -> None:
        """
        Find positions on stacks with same or smaller bases.

        Args:
            item: The item to be placed
            container: The container
            pos_list: List of position candidates
            same_base_positions: Output list for same base positions
            smaller_base_positions: Output list for smaller base positions
        """
        item_length = max(item.l, item.w)
        item_width = min(item.l, item.w)

        for pos in pos_list:
            if pos.item.get_z() == 0:
                continue

            # Search items below the position
            z_map = container.get_base_data().get_z_map()
            item_idx_list = z_map.get(pos.item.get_z())
            if item_idx_list is None:
                continue

            for idx in item_idx_list:
                below_item = container.get_items()[idx]

                # Check if this item is directly below the position
                if (below_item.x == pos.item.get_x() and
                    below_item.y == pos.item.get_y() and
                    below_item.zh == pos.item.get_z()):

                    # Check if this item has same base
                    if (item_length == max(below_item.l, below_item.w) and
                        item_width == min(below_item.l, below_item.w)):
                        same_base_positions.append(pos)
                    elif (item_length <= max(below_item.l, below_item.w) and
                          item_width <= min(below_item.l, below_item.w)):
                        smaller_base_positions.append(pos)

    def _choose_base_position(
        self,
        same_base_positions: List['PositionCandidate'],
        smaller_base_positions: List['PositionCandidate']
    ) -> Optional['PositionCandidate']:
        """
        Choose best position from same or smaller base positions.

        Args:
            same_base_positions: List of same base positions
            smaller_base_positions: List of smaller base positions

        Returns:
            Best position or None
        """
        found_positions = (
            same_base_positions if len(same_base_positions) > 0
            else smaller_base_positions
        )

        if len(found_positions) == 0:
            return None

        min_high_low_positions = self.get_position_with_min_value(
            found_positions,
            self.high_low._get_distance
        )

        if len(min_high_low_positions) == 0:
            return None

        return min_high_low_positions[0]

    def _find_position(
        self,
        container: 'Container',
        pos_list: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Find best position using combined strategies.

        Args:
            container: The container
            pos_list: List of position candidates

        Returns:
            Best position

        Raises:
            XFLPException: If no position found
        """
        min_high_low_positions = self.get_position_with_min_value(
            pos_list,
            self.high_low._get_distance
        )

        filtered_positions = self.get_position_with_min_value(
            min_high_low_positions,
            lambda candidate: self.width_proportion.get_deviation_of_proportion(
                candidate, container
            )
        )

        if len(filtered_positions) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "There must be at least one position."
            )

        return filtered_positions[0]
