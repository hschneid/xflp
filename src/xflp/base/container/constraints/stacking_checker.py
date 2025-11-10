"""
Stacking constraint checker.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from ..container import Container
    from ...item.item import Item
    from ...item.position import Position

from ..enums import ParameterType, GroundContactRule
from ...item.tools import get_cut_ratio_coords


class StackingChecker:
    """
    Validates stacking restrictions for items.
    
    Checks:
    - Stacking group compatibility
    - Ground contact requirements
    - Load bearing capacity
    """
    
    @staticmethod
    def check_stacking_restrictions(
        container: 'Container',
        pos: 'Position',
        new_item: 'Item',
        item_w: int,
        item_l: int
    ) -> bool:
        """
        Check if stacking the item at position is valid.
        
        Args:
            container: The container
            pos: The position to place item
            new_item: The item to place
            item_w: Width of item (may be rotated)
            item_l: Length of item (may be rotated)
            
        Returns:
            True if stacking is valid, False otherwise
        """
        # New item will be placed at ground - no stacking check needed
        if pos.z == 0:
            return True
        
        # Check stacking group and ground contact
        return StackingChecker._check_stacking_group_and_ground_contact(
            container, new_item, pos, item_w, item_l, new_item.stacking_group
        )
    
    @staticmethod
    def _check_stacking_group_and_ground_contact(
        container: 'Container',
        item: 'Item',
        pos: 'Position',
        item_w: int,
        item_l: int,
        stacking_group: int
    ) -> bool:
        """
        Check stacking group compatibility and ground contact requirements.
        
        Args:
            container: The container
            item: The item to place
            pos: The position
            item_w: Item width
            item_l: Item length
            stacking_group: The stacking group
            
        Returns:
            True if valid, False otherwise
        """
        z_list = container.get_base_data().get_z_map().get(pos.z)
        
        if z_list is None or len(z_list) == 0:
            return True
        
        item_xw = pos.x + item_w
        item_yl = pos.y + item_l
        
        nbr_of_items_below = 0
        corner_items = [-1, -1, -1, -1]
        corners = [False, False, False, False]
        
        bearing_capacities = container.get_base_data().get_bearing_capacities()
        
        # Check for all lower items if stacking group restriction is valid
        for item_idx in z_list:
            fi = container.get_items()[item_idx]
            
            if StackingChecker._is_not_below(pos, item_w, item_l, fi):
                continue
            
            nbr_of_items_below += 1
            
            # Check stacking group compatibility (bitwise AND)
            if (fi.allowed_stacking_groups & stacking_group) == 0:
                return False
            
            # Check which corners are supported
            if pos.x >= fi.x and pos.x <= fi.xw and pos.y >= fi.y and pos.y <= fi.yl:
                corner_items[0] = fi.external_index
                corners[0] = True
            if item_xw > fi.x and item_xw <= fi.xw and pos.y >= fi.y and pos.y <= fi.yl:
                corner_items[1] = fi.external_index
                corners[1] = True
            if pos.x >= fi.x and pos.x <= fi.xw and item_yl > fi.y and item_yl <= fi.yl:
                corner_items[2] = fi.external_index
                corners[2] = True
            if item_xw > fi.x and item_xw <= fi.xw and item_yl > fi.y and item_yl <= fi.yl:
                corner_items[3] = fi.external_index
                corners[3] = True
            
            # Check if bearing capacity is enough
            bearing_capacity = bearing_capacities.get(item_idx, 0.0)
            area_ratio = get_cut_ratio_coords(pos.x, pos.y, item_w, item_l, fi)
            
            if bearing_capacity - (item.weight * area_ratio) < 0:
                return False
        
        # Check if number of below items exceeds allowed
        if item.nbr_of_allowed_stacked_items > 0:
            if nbr_of_items_below > item.nbr_of_allowed_stacked_items:
                return False
        
        # Check ground contact
        has_any_ground_contact = any(corners)
        has_full_ground_contact = all(corners)
        
        ground_rule = container.get_parameter().get(ParameterType.GROUND_CONTACT_RULE)
        
        if ground_rule == GroundContactRule.SINGLE:
            return StackingChecker._all_equal(corner_items) and has_full_ground_contact
        elif ground_rule == GroundContactRule.FREE:
            return has_any_ground_contact
        
        # Default: COVERED or MULTIPLE
        return has_full_ground_contact
    
    @staticmethod
    def _all_equal(values: list) -> bool:
        """
        Check if all values in the list are equal.
        
        Args:
            values: List of values
            
        Returns:
            True if all equal
        """
        if not values:
            return True
        sorted_values = sorted(values)
        return sorted_values[0] == sorted_values[-1]
    
    @staticmethod
    def _is_not_below(position: 'Position', item_w: int, item_l: int, lower_item: 'Item') -> bool:
        """
        Check if an item is NOT below the given position.
        
        Args:
            position: The position to check
            item_w: Item width
            item_l: Item length
            lower_item: The potential lower item
            
        Returns:
            True if item is not below position
        """
        return (lower_item.zh != position.z or
                lower_item.xw <= position.x or
                lower_item.yl <= position.y or
                lower_item.x >= position.x + item_w or
                lower_item.y >= position.y + item_l)
