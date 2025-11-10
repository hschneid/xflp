"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

This class means a candidate where and how an item can be placed into container.
"""

from dataclasses import dataclass
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.item import Item, Position


@dataclass(frozen=True)
class PositionCandidate:
    """
    Represents a candidate position for placing an item in a container.

    Attributes:
        position: The position where the item would be placed
        item: The item to be placed
        is_rotated: Whether the item is rotated at this position
    """
    position: 'Position'
    item: 'Item'
    is_rotated: bool

    @staticmethod
    def of(position: 'Position', item: 'Item', is_rotated: bool) -> 'PositionCandidate':
        """
        Factory method to create a PositionCandidate.

        Args:
            position: The position where the item would be placed
            item: The item to be placed
            is_rotated: Whether the item is rotated

        Returns:
            A new PositionCandidate instance
        """
        return PositionCandidate(position, item, is_rotated)
