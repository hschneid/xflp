"""
Axle load constraint checker.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..container import Container
    from ...item.item import Item
    from ...item.position import Position

from ..enums import ParameterType
from .axle_load_parameter import AxleLoadParameter


class AxleLoadChecker:
    """
    Checks whether the given item at given position is valid
    considering permissible axle load restrictions.
    """
    
    @staticmethod
    def check_permissible_axle_load(
        container: 'Container', 
        item: 'Item', 
        pos: 'Position'
    ) -> bool:
        """
        Check if placing an item would violate axle load constraints.
        
        Args:
            container: The container
            item: The item to place
            pos: The position to place it
            
        Returns:
            True if valid (or no axle load parameter set), False if invalid
        """
        axle_load_param = container.get_parameter().get(ParameterType.AXLE_LOAD)
        
        if axle_load_param is None or not isinstance(axle_load_param, AxleLoadParameter):
            return True
        
        if axle_load_param.axle_distance == 0:
            return True
        
        total_weight = container.get_loaded_weight() + item.weight
        
        # Center of truck
        center_of_truck = axle_load_param.axle_distance / 2.0
        
        # Get maximum Y extent
        max_y = max(
            (i.yl for i in container.get_items() if i is not None),
            default=0
        )
        max_y = max(max_y, pos.y + item.l)
        
        center_of_load = max_y / 2.0
        pad_y = max(0, center_of_truck - center_of_load)
        
        # Get current center of gravity for Y (length), which is the direction of axles
        current_cog_y = container.get_base_data().get_center_of_gravity_for_y()
        new_cog_y = (current_cog_y + ((pos.y + (item.l / 2.0)) * item.weight)) / total_weight
        
        # Main formula to calculate the load at one of the 2 axles
        load_at_second_axle = (total_weight * (new_cog_y + pad_y)) / axle_load_param.axle_distance
        load_at_first_axle = total_weight - load_at_second_axle
        
        return (load_at_first_axle <= axle_load_param.first_permissible_axle_load and
                load_at_second_axle <= axle_load_param.second_permissible_axle_load)
