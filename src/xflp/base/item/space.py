"""
Space data class representing available space dimensions.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from dataclasses import dataclass


@dataclass(frozen=True)
class Space:
    """
    Represents available space dimensions in a container.

    Attributes:
        l: Length
        w: Width
        h: Height
    """

    l: int
    w: int
    h: int

    @classmethod
    def of(cls, l: int, w: int, h: int) -> "Space":
        """
        Create a Space instance.

        Args:
            l: Length
            w: Width
            h: Height

        Returns:
            Space instance
        """
        return cls(l=l, w=w, h=h)

    def __str__(self) -> str:
        """String representation showing dimensions."""
        return f"(w:{self.w} l:{self.l} h:{self.h})"
