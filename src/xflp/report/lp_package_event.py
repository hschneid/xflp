"""
LPPackageEvent dataclass for package loading/unloading events.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from dataclasses import dataclass

from .load_type import LoadType


@dataclass(frozen=True)
class LPPackageEvent:
    """
    Represents a package event in the load planning solution.

    This class stores information about a package being loaded or unloaded,
    including its position, dimensions, weight, and other attributes.
    """

    id: str
    x: int
    y: int
    z: int
    w: int
    l: int
    h: int
    stacking_grp: int
    weight: float
    weight_limit: float
    is_invalid: bool
    type: LoadType
    used_volume_in_container: float
    used_weight_in_container: float
    nbr_stacks_in_container: int
    is_rotated_position: bool
