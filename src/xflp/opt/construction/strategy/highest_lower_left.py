# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
The strategy is used in construction heuristic to choose best possible insert position.

This type of strategy chooses with maximal priority the highest and secondary the
most left (width) and most decent (length) position.

Author: hschneid
"""

from typing import List, TYPE_CHECKING
import math

from xflp.opt.construction.strategy.base_strategy import BaseStrategy
from xflp.exception.xflp_exception import XFLPException, XFLPExceptionType

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.position.position_candidate import PositionCandidate


class HighestLowerLeft(BaseStrategy):
    """
    Strategy that prioritizes highest positions, then lowest left positions.
    """

    def choose(
        self,
        item: 'Item',
        container: 'Container',
        candidates: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Choose the highest, then lower-left position.

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

        # First, filter by minimum Z distance (highest position)
        filtered_positions = self.get_position_with_min_value(
            candidates,
            self._get_distance_z
        )

        # If multiple positions at same height, use 3D distance
        if len(filtered_positions) > 1:
            filtered_positions = self.get_position_with_min_value(
                filtered_positions,
                self._get_distance
            )

        if len(filtered_positions) == 0:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "There must be at least one position."
            )

        return filtered_positions[0]

    def _get_distance(self, candidate: 'PositionCandidate') -> float:
        """
        Calculate 3D Euclidean distance from origin.

        Args:
            candidate: Position candidate

        Returns:
            Distance value
        """
        if candidate is None:
            return float('inf')

        p = candidate.position
        return math.sqrt(p.x ** 2 + p.y ** 2 + p.z ** 2)

    def _get_distance_z(self, candidate: 'PositionCandidate') -> float:
        """
        Get negative Z coordinate (for minimization to find highest position).

        Args:
            candidate: Position candidate

        Returns:
            Negative Z coordinate
        """
        if candidate is None:
            return float('inf')
        return -float(candidate.position.z)
