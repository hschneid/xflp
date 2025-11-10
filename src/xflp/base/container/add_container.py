"""
Add-only container implementation.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Dict, List, Set, TYPE_CHECKING

from .container_base import ContainerBase
from ..space.space_service import SpaceService

if TYPE_CHECKING:
    from ..item.item import Item
    from ..item.position import Position
    from ..item.space import Space
    from .container_parameter import ContainerParameter
    from .container import Container


class AddContainer(ContainerBase):
    """
    Container that supports adding items but not removing them.
    
    More efficient than AddRemoveContainer for one-way loading scenarios.
    Maintains space information for placement optimization.
    """
    
    def __init__(
        self,
        width: int,
        length: int,
        height: int,
        max_weight: float,
        container_type: int,
        parameter: 'ContainerParameter'
    ):
        """
        Initialize an add-only container.
        
        Args:
            width: Container width
            length: Container length
            height: Container height
            max_weight: Maximum weight capacity
            container_type: Container type identifier
            parameter: Container behavior parameters
        """
        super().__init__(width, length, height, max_weight, container_type, parameter)
        self._init_add_container()
    
    def _init_add_container(self) -> None:
        """Initialize add container specific structures."""
        from ..item.space import Space
        
        self._unique_position_keys: Set[str] = set()
        self._space_positions: Dict['Position', List['Space']] = {}
        self._space_service = SpaceService()
        
        # Add initial space at root position
        if self.active_pos_list:
            self._space_positions[self.active_pos_list[0]] = [
                Space.of(self.length, self.width, self.height)
            ]
    
    def new_instance(self) -> 'Container':
        """
        Create a new empty instance with same configuration.
        
        Returns:
            New AddContainer instance
        """
        return AddContainer(
            self.width,
            self.length,
            self.height,
            self.max_weight,
            self.container_type,
            self.parameter
        )
    
    def add(self, item: 'Item', pos: 'Position', is_rotated: bool) -> int:
        """
        Add an item to the container at the given position.
        
        Updates internal data structures:
        - New positions with spaces
        - Remove covered positions
        
        Args:
            item: The item to add
            pos: The position to place it
            is_rotated: Whether the item should be rotated
            
        Returns:
            The index of the inserted item
        """
        pos = self._norm_position(item, pos, is_rotated)
        
        self._add_item(item, pos)
        
        # Active position gets inactive by adding item
        self._remove_position(pos)
        
        self._remove_covered_positions(item)
        
        # Check existing spaces if new item will shrink them
        self._check_existing_spaces(item)
        
        # Create new insert positions and spaces
        new_pos_list = self._find_insert_positions(item)
        for new_pos in new_pos_list:
            if new_pos.key in self._unique_position_keys:
                continue
            
            self.active_pos_list.append(new_pos)
            self._unique_position_keys.add(new_pos.key)
            
            new_spaces = self._create_spaces(new_pos)
            if new_spaces:
                self._space_positions[new_pos] = new_spaces
            else:
                self._remove_position(new_pos)
        
        self._update_bearing_capacity([item])
        self._add_to_center_of_gravity(item, pos)
        
        self.history.append(item)
        
        return item.index
    
    def _create_spaces(self, new_pos: 'Position') -> List['Space']:
        """
        Create spaces at a new position.
        
        Begins with maximal space and checks for each item in max-space
        if smaller spaces are possible.
        
        Args:
            new_pos: The new position
            
        Returns:
            List of valid spaces
        """
        from ..item.space import Space
        
        max_space = Space.of(
            self.length - new_pos.y,
            self.width - new_pos.x,
            self.height - new_pos.z
        )
        space_items = self._space_service.get_items_in_space(new_pos, max_space, self.item_list)
        
        spaces = {max_space}
        for space_item in space_items:
            next_spaces = set()
            for space in spaces:
                next_spaces.update(
                    self._space_service.create_spaces_at_position(new_pos, space, space_item)
                )
            spaces = next_spaces
        
        return self._space_service.get_dominating_spaces(spaces)
    
    def _remove_position(self, position: 'Position') -> None:
        """
        Remove a position from active positions.
        
        Args:
            position: The position to remove
        """
        if position in self.active_pos_list:
            self.active_pos_list.remove(position)
        self._unique_position_keys.discard(position.key)
        self._space_positions.pop(position, None)
    
    def remove(self, item: 'Item') -> None:
        """
        Remove is not supported in AddContainer.
        
        Raises:
            NotImplementedError: Always
        """
        raise NotImplementedError(
            "Remove in AddContainer is not supported. Use AddRemoveContainer"
        )
    
    def _remove_covered_positions(self, item: 'Item') -> None:
        """
        Remove positions covered by the newly placed item.
        
        Args:
            item: The item that was placed
        """
        for position in self._find_covered_positions(item):
            self._remove_position(position)
    
    def _check_existing_spaces(self, new_item: 'Item') -> None:
        """
        Check if existing spaces need to be updated due to new item.
        
        Args:
            new_item: The newly placed item
        """
        removable_positions: List['Position'] = []
        
        for position in self.active_pos_list:
            # Is position out of reach for newItem
            if (position.x >= new_item.xw or
                position.y >= new_item.yl or
                position.z >= new_item.zh):
                continue
            
            new_spaces = set()
            for space in self._space_positions.get(position, []):
                new_spaces.update(
                    self._space_service.create_spaces_at_position(
                        position,
                        space,
                        new_item
                    )
                )
            
            spaces = self._space_service.get_dominating_spaces(new_spaces)
            if spaces:
                self._space_positions[position] = spaces
            else:
                removable_positions.append(position)
        
        for removable_position in removable_positions:
            self._remove_position(removable_position)
    
    def get_space(self, pos: 'Position') -> List['Space']:
        """
        Get available spaces at a position.
        
        Args:
            pos: The position to check
            
        Returns:
            List of available spaces
        """
        return self._space_positions.get(pos, [])
