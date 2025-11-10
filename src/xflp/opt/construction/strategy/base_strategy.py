# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Base strategy for position selection in packing algorithms.

Author: hschneid
"""

from abc import ABC, abstractmethod
from typing import List, Callable, TYPE_CHECKING
import sys

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item
    from xflp.base.position.position_candidate import PositionCandidate


class BaseStrategy(ABC):
    """Abstract base class for packing strategies."""

    @abstractmethod
    def choose(
        self,
        item: 'Item',
        container: 'Container',
        pos_list: List['PositionCandidate']
    ) -> 'PositionCandidate':
        """
        Choose the best position from the list of candidates.

        Args:
            item: The item to be placed
            container: The container to place the item in
            pos_list: List of position candidates

        Returns:
            The chosen position candidate

        Raises:
            XFLPException: If no valid position is found
        """
        pass

    def get_position_with_min_value(
        self,
        candidates: List['PositionCandidate'],
        position_value: Callable[['PositionCandidate'], float]
    ) -> List['PositionCandidate']:
        """
        Filter candidates to those with minimum value according to the given function.

        Args:
            candidates: List of position candidates
            position_value: Function to calculate value for each candidate

        Returns:
            List of candidates with minimum value
        """
        if candidates is None:
            return []

        if len(candidates) <= 1:
            return candidates

        # Calculate distances for all candidates
        distances = [position_value(candidate) for candidate in candidates]

        # Find minimum value
        min_value = min(distances)

        # Filter positions with minimum value
        filtered_positions = [
            candidates[i]
            for i in range(len(candidates))
            if distances[i] == min_value
        ]

        return filtered_positions
