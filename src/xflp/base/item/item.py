"""
Item class representing a package/item to be loaded into a container.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Optional, Set
from .position import Position
from .enums import RotationType
from ...report.load_type import LoadType


class Item:
    """
    An item is the entity that is placed into a container.

    It contains all master data, parameters, and planning information
    (like the current position).

    Attributes:
        Dimensions:
            w: Width
            l: Length
            h: Height
            orig_h: Original height (before any immersive depth adjustments)

        Position (in container):
            x, y, z: Bottom-left-front corner coordinates
            xw: x + w (right edge)
            yl: y + l (back edge)
            zh: z + h (top edge)

        Properties:
            size: Footprint area (w * l)
            volume: Total volume (w * l * h)
            weight: Item weight

        Constraints:
            spinable: Can item be rotated?
            stackable: Can items be placed on top?
            stacking_group: Binary representation of item's stacking group
            allowed_stacking_groups: Binary representation of allowed stacking groups on top
            nbr_of_allowed_stacked_items: Max different items that can be below when stacked
            stacking_weight_limit: Max weight this item can support
            immersive_depth: Height reduction when items are stacked on top

        Location/Routing:
            loading_loc: Loading location index
            unloading_loc: Unloading location index

        Container restrictions:
            allowed_container_set: Set of allowed container type IDs

        State:
            external_index: Unique identifier
            order_index: Order ID (for paired loading/unloading)
            index: Position in container's item list (-1 if unpacked)
            container_index: Index of container holding this item (-1 if unpacked)
            is_loading: True if being loaded, False if unloaded
            is_rotated: True if currently rotated
            loading_type: LOAD or UNLOAD
    """

    def __init__(self):
        """Initialize an Item with default values."""
        # Dimensions
        self.w: int = 0  # width
        self.l: int = 0  # length
        self.h: int = 0  # height
        self.orig_h: int = 0  # original height

        # Position
        self.x: int = -1
        self.y: int = -1
        self.z: int = -1
        self.xw: int = -1  # x + width
        self.yl: int = -1  # y + length
        self.zh: int = -1  # z + height

        # Calculated properties
        self.size: int = 0  # footprint
        self.volume: int = 0

        # Physical properties
        self.weight: float = 0.0
        self.stacking_weight_limit: float = 0.0

        # Constraints
        self.spinable: bool = False
        self.stackable: bool = True
        self.stacking_group: int = 0  # Binary representation
        self.allowed_stacking_groups: int = 0  # Binary representation
        self.nbr_of_allowed_stacked_items: int = 0
        self.immersive_depth: int = 0

        # Location/routing
        self.loading_loc: int = 0
        self.unloading_loc: int = 0

        # Container restrictions
        self.allowed_container_set: Optional[Set[int]] = None

        # Identifiers and state
        self.external_index: int = 0
        self.order_index: int = -1
        self.index: int = -1
        self.container_index: int = -1
        self.is_loading: bool = False
        self.is_rotated: bool = False
        self.loading_type: Optional[LoadType] = None

    def post_init(self) -> None:
        """Calculate derived properties after setting dimensions."""
        self.size = self.w * self.l
        self.volume = self.h * self.w * self.l
        self.loading_type = LoadType.LOAD if self.is_loading else LoadType.UNLOAD

    def rotate(self) -> None:
        """Rotate the item 90 degrees (swap width and length)."""
        self.w, self.l = self.l, self.w
        self.is_rotated = not self.is_rotated

    def set_position(self, pos: Position) -> None:
        """
        Set the item's position in the container.

        Args:
            pos: Position object with x, y, z coordinates
        """
        self.x = pos.x
        self.y = pos.y
        self.z = pos.z
        self.xw = self.x + self.w
        self.yl = self.y + self.l
        self.zh = self.z + self.h

    def clear_position(self) -> None:
        """Clear the item's position (mark as unplaced)."""
        self.x = self.y = self.z = self.xw = self.yl = self.zh = -1

    def reset(self) -> None:
        """Reset the item to unpacked state."""
        self.clear_position()
        self.index = -1
        self.container_index = -1
        if self.is_rotated:
            self.rotate()
        self.is_loading = False

    def get_volume(self) -> int:
        """Calculate and return the current volume."""
        return self.w * self.l * self.h

    def __str__(self) -> str:
        """String representation of the item."""
        rotation_marker = "R" if self.is_rotated else ""
        return (f"Item {self.external_index} {self.loading_loc} {self.unloading_loc} "
                f"({self.w},{self.l},{self.h}) [{self.x}, {self.y}, {self.z} {rotation_marker}] "
                f"{self.stacking_group}")

    def __eq__(self, other: object) -> bool:
        """Equality based on index."""
        if not isinstance(other, Item):
            return False
        return self.index == other.index

    def __hash__(self) -> int:
        """Hash based on index."""
        return hash(self.index)

    # Property accessors for consistency with Java getters/setters
    def get_idx(self) -> int:
        """Get the item's index (for Indexable interface compatibility)."""
        return self.index

    def set_idx(self, idx: int) -> None:
        """Set the item's index (for Indexable interface compatibility)."""
        self.index = idx

    def get_weight(self) -> float:
        """Get the item's weight."""
        return self.weight

    def get_yl(self) -> int:
        """Get the Y + length coordinate."""
        return self.yl

    def get_immersive_depth(self) -> int:
        """Get the immersive depth."""
        return self.immersive_depth

    def get_stacking_weight_limit(self) -> float:
        """Get the stacking weight limit."""
        return self.stacking_weight_limit

    def get_nbr_of_allowed_stacked_items(self) -> int:
        """Get the number of allowed stacked items."""
        return self.nbr_of_allowed_stacked_items
