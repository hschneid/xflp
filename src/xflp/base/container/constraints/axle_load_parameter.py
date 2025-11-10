"""
Axle load parameter data class.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from dataclasses import dataclass


@dataclass(frozen=True)
class AxleLoadParameter:
    """
    Parameters for axle load calculations.
    
    Attributes:
        first_permissible_axle_load: Maximum load on first axle
        second_permissible_axle_load: Maximum load on second axle
        axle_distance: Distance between axles
    """
    
    first_permissible_axle_load: float
    second_permissible_axle_load: float
    axle_distance: float
