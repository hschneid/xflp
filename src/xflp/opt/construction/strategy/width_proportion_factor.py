# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
The strategy is used in construction heuristic to choose best possible insert position.

This type of strategy chooses position, which width is a good proportion of container width.
As alternative strategy it uses the HighestLowerLeft.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.opt.construction.strategy.base_strategy import BaseStrategy
from xflp.opt.construction.strategy.highest_lower_left import HighestLowerLeft
from xflp.exception.xflp_exception import XFLPException, XFLPExceptionType

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.position.position_candidate import PositionCandidate


class WidthProportionFactor(BaseStrategy):
    """
    Strategy that prioritizes positions with good width proportions.
    """

    def __init__(self):
        """Initialize the width proportion factor strategy."""
        self.fallback_strategy = HighestLowerLeft()

    def choose(
        self,
        item: 'Item',
        container: 'Container',
        candidates: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Choose the position with best width proportion.

        Args:
            item: The item to be placed
            container: The container
            candidates: List of position candidates

        Returns:
            The chosen position candidate

        Raises:
            XFLPException: If candidates list is empty or None
        """
        if candidates is None or len(candidates) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "List of positions must be not empty or null."
            )

        if len(candidates) == 1:
            return candidates[0]

        # Filter by minimum deviation from proportion
        filtered_positions = self.get_position_with_min_value(
            candidates,
            lambda candidate: self.get_deviation_of_proportion(candidate, container)
        )

        if len(filtered_positions) == 1:
            return filtered_positions[0]

        if len(filtered_positions) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "There must be at least one position."
            )

        return self.fallback_strategy.choose(item, container, filtered_positions)

    def get_deviation_of_proportion(
        self,
        candidate: 'PositionCandidate',
        container: 'Container'
    ) -> float:
        """
        Calculate deviation from ideal width proportion.

        Args:
            candidate: Position candidate
            container: The container

        Returns:
            Deviation value (rounded percentage)
        """
        con_width = container.get_width()
        space_width = con_width - candidate.position.x
        item_width = candidate.item.l if candidate.is_rotated else candidate.item.w

        proportion = space_width / float(item_width)
        if proportion < 0:
            return float('inf')

        best_proportion = int(proportion)
        deviation = abs(proportion - best_proportion)

        return round(deviation * 100)
