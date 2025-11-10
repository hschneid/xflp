"""
Container base data interface protocol.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Protocol, Dict, TYPE_CHECKING

if TYPE_CHECKING:
    from ...util.collection.lp_list_map import LPListMap
    from .z_item_graph import ZItemGraph


class ContainerBaseData(Protocol):
    """
    Protocol for accessing internal container data structures.
    
    Provides access to coordinate maps, item graph, and bearing capacities.
    """
    
    def get_x_map(self) -> 'LPListMap[int, int]':
        """Get the X-coordinate to item index map."""
        ...
    
    def get_y_map(self) -> 'LPListMap[int, int]':
        """Get the Y-coordinate to item index map."""
        ...
    
    def get_z_map(self) -> 'LPListMap[int, int]':
        """Get the Z-coordinate to item index map."""
        ...
    
    def get_z_graph(self) -> 'ZItemGraph':
        """Get the Z-axis item relationship graph."""
        ...
    
    def get_bearing_capacities(self) -> Dict[int, float]:
        """Get the bearing capacity map for items."""
        ...
    
    def get_center_of_gravity_for_y(self) -> float:
        """Get the center of gravity along the Y axis."""
        ...
