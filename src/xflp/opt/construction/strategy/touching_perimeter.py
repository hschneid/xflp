# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
The strategy is used in construction heuristic to choose best possible insert position.

This type of strategy chooses the position with the highest touching perimeter value.
This value describes the contact with walls or other items.
High value means much contact.

If multiple positions have the highest touching perimeter value, then the
strategy HighestLowerLeft is used to decide.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.opt.construction.strategy.base_strategy import BaseStrategy
from xflp.opt.construction.strategy.highest_lower_left import HighestLowerLeft
from xflp.exception.xflp_exception import XFLPException, XFLPExceptionType
from xflp.base.position.touching_perimeter_service import TouchingPerimeterService

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.position.position_candidate import PositionCandidate


class TouchingPerimeter(BaseStrategy):
    """
    Strategy that prioritizes positions with highest touching perimeter.
    """

    def __init__(self, consider_walls: bool = True, consider_base_floor: bool = True):
        """
        Initialize the touching perimeter strategy.

        Args:
            consider_walls: Whether to consider wall contacts
            consider_base_floor: Whether to consider base floor contacts
        """
        self.fallback_strategy = HighestLowerLeft()
        self.consider_walls = consider_walls
        self.consider_base_floor = consider_base_floor

    def choose(
        self,
        item: 'Item',
        container: 'Container',
        pos_list: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Choose the position with highest touching perimeter.

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

        # Filter by maximum touching perimeter (negative for minimization)
        filtered_positions = self.get_position_with_min_value(
            pos_list,
            lambda candidate: -TouchingPerimeterService.get_touching_perimeter(
                container,
                candidate,
                1,
                True,
                True
            )
        )

        # Return if single position found
        if len(filtered_positions) == 1:
            return filtered_positions[0]
        elif len(filtered_positions) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "There must be at least one position."
            )
        else:
            # Use fallback strategy for tie-breaking
            return self.fallback_strategy.choose(item, container, filtered_positions)
