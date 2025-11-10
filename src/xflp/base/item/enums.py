"""
Enums for item-related types.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from enum import Enum, auto


class RotationType(Enum):
    """Defines whether an item can be rotated."""

    FIX = 0
    SPINNABLE = 1

    @property
    def rotation_type(self) -> int:
        """Get the rotation type value."""
        return self.value


class PositionType(Enum):
    """Defines the type of a position in the container."""

    TMP = auto()
    ROOT = auto()
    BASIC = auto()
    EXTENDED_H = auto()
    EXTENDED_V = auto()
