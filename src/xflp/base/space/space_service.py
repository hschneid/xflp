"""
Space service for managing available spaces in containers.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import List, Set, Collection, TYPE_CHECKING

if TYPE_CHECKING:
    from ..item.item import Item
    from ..item.position import Position
    from ..item.space import Space


class SpaceService:
    """
    Service for calculating and managing available spaces in a container.
    
    Handles space subdivision when new items are placed.
    """
    
    def create_spaces_at_position(
        self, 
        position: 'Position', 
        space: 'Space', 
        new_item: 'Item'
    ) -> List['Space']:
        """
        Create new spaces after an item potentially intersects with a space.
        
        Args:
            position: The position of the space
            space: The original space
            new_item: The new item that may intersect
            
        Returns:
            List of resulting spaces (may be subdivided)
        """
        from ..item.space import Space
        
        # Are position and space out of reach for newItem
        if self.is_item_not_in_space(position, space, new_item):
            return [space]
        
        # New item is touching this space!
        
        # New item is over the position
        item_hovering = position.z < new_item.z
        # New item is in view range (upper right of position)
        width_limited = position.y >= new_item.y and position.y < new_item.yl
        length_limited = position.x >= new_item.x and position.x < new_item.xw
        item_over_position = width_limited and length_limited
        
        spaces: List['Space'] = []
        
        if item_hovering:
            spaces.append(Space.of(
                space.l,
                space.w,
                new_item.z - position.z
            ))
            if not item_over_position:
                spaces.append(Space.of(
                    (new_item.y - position.y) if length_limited else space.l,
                    (new_item.x - position.x) if width_limited else space.w,
                    space.h
                ))
        
        if width_limited or length_limited:
            if not item_hovering and not item_over_position:
                spaces.append(Space.of(
                    (new_item.y - position.y) if length_limited else space.l,
                    (new_item.x - position.x) if width_limited else space.w,
                    space.h
                ))
        # New item is only partially in view range (cutting position coordinates)
        else:
            spaces.append(Space.of(
                new_item.y - position.y,
                space.w,
                space.h
            ))
            spaces.append(Space.of(
                space.l,
                new_item.x - position.x,
                space.h
            ))
        
        return spaces
    
    def is_item_not_in_space(
        self, 
        position: 'Position', 
        space: 'Space', 
        item: 'Item'
    ) -> bool:
        """
        Check if an item does NOT intersect with a space at a position.
        
        Args:
            position: The position of the space
            space: The space
            item: The item to check
            
        Returns:
            True if item doesn't intersect the space
        """
        return (position.x + space.w <= item.x or
                position.y + space.l <= item.y or
                position.z + space.h <= item.z or
                position.x >= item.xw or
                position.y >= item.yl or
                position.z >= item.zh)
    
    def get_items_in_space(
        self, 
        position: 'Position', 
        space: 'Space', 
        all_items: List['Item']
    ) -> Set['Item']:
        """
        Get all items that intersect with a given space.
        
        Args:
            position: The position of the space
            space: The space
            all_items: All items to check
            
        Returns:
            Set of items that intersect the space
        """
        items_in_space: Set['Item'] = set()
        
        for item in all_items:
            # Entries can be null if removed from item list
            if item is None or self.is_item_not_in_space(position, space, item):
                continue
            items_in_space.add(item)
        
        return items_in_space
    
    def get_dominating_spaces(self, spaces: Collection['Space']) -> List['Space']:
        """
        Filter spaces to only include non-dominated ones.
        
        A space dominates another if it has the same or larger dimensions
        in all aspects.
        
        Args:
            spaces: Collection of spaces
            
        Returns:
            List of dominating spaces
        """
        if len(spaces) == 1:
            return list(spaces)
        
        dominating_spaces = list(spaces)
        dominated_spaces: List['Space'] = []
        
        for space_a in spaces:
            for space_b in spaces:
                if space_a is space_b:
                    continue
                
                # Check if space_a dominates space_b
                if (space_a.l == space_b.l and 
                    space_a.w == space_b.w and 
                    space_a.h > space_b.h):
                    dominated_spaces.append(space_b)
                
                if (space_a.l == space_b.l and 
                    space_a.h == space_b.h and 
                    space_a.w > space_b.w):
                    dominated_spaces.append(space_b)
                
                if (space_a.h == space_b.h and 
                    space_a.w == space_b.w and 
                    space_a.l > space_b.l):
                    dominated_spaces.append(space_b)
        
        # Remove dominated spaces
        for dominated in set(dominated_spaces):
            if dominated in dominating_spaces:
                dominating_spaces.remove(dominated)
        
        return dominating_spaces
