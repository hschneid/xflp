"""
Position data class for representing 3D coordinates.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from dataclasses import dataclass
from typing import Optional
from .enums import PositionType


@dataclass(frozen=True)
class Position:
    """
    Represents a 3D position in a container.

    Attributes:
        idx: Index of the position
        x: X-coordinate
        y: Y-coordinate
        z: Z-coordinate (height)
        type: Type of the position
    """

    idx: int
    x: int
    y: int
    z: int
    type: PositionType

    @classmethod
    def of(cls, x: int, y: int, z: int, idx: int = -1,
           pos_type: PositionType = PositionType.TMP) -> "Position":
        """
        Create a Position instance.

        Args:
            x: X-coordinate
            y: Y-coordinate
            z: Z-coordinate
            idx: Index (default: -1)
            pos_type: Position type (default: TMP)

        Returns:
            Position instance
        """
        return cls(idx=idx, x=x, y=y, z=z, type=pos_type)

    def __str__(self) -> str:
        """String representation showing coordinates."""
        return f"({self.x},{self.y},{self.z})"

    def __eq__(self, other: object) -> bool:
        """Equality based on index."""
        if not isinstance(other, Position):
            return False
        return self.idx == other.idx

    def __hash__(self) -> int:
        """Hash based on index."""
        return hash(self.idx)

    @property
    def key(self) -> str:
        """Get a string key representing the coordinates."""
        return f"{self.x}/{self.y}/{self.z}"
