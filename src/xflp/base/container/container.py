"""
Container interface protocol.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Protocol, List, TYPE_CHECKING

if TYPE_CHECKING:
    from ..item.item import Item
    from ..item.position import Position
    from ..item.space import Space
    from .container_parameter import ContainerParameter
    from .container_base_data import ContainerBaseData


class Container(Protocol):
    """
    Protocol defining the container interface.
    
    A container is a space where items can be loaded and unloaded.
    It manages positions, validates constraints, and tracks loaded items.
    """
    
    def new_instance(self) -> 'Container':
        """
        Create a new container instance with same master data.
        
        The new container is empty.
        
        Returns:
            A new empty container instance
        """
        ...
    
    def add(self, item: 'Item', position: 'Position', is_rotated: bool) -> int:
        """
        Add an item at given position to container.
        
        New positions will be created and covered positions become inactive.
        
        Args:
            item: The item to add
            position: The position to place the item
            is_rotated: Whether the item is rotated
            
        Returns:
            The index of the inserted item for faster access
        """
        ...
    
    def remove(self, item: 'Item') -> None:
        """
        Remove the item from container.
        
        Uncovered positions will be freed.
        
        Args:
            item: The item to remove
        """
        ...
    
    def get_items(self) -> List['Item']:
        """
        Get the already inserted items of this container.
        
        Returns:
            List of items in the container
        """
        ...
    
    def get_history(self) -> List['Item']:
        """
        Get the history of loaded/unloaded items.
        
        This is relevant for creating the solution report.
        
        Returns:
            List of items in order of loading/unloading
        """
        ...
    
    def is_item_allowed(self, item: 'Item') -> bool:
        """
        Check if the given item is allowed to be placed in this container.
        
        Args:
            item: The item to check
            
        Returns:
            True if item can be placed in this container
        """
        ...
    
    def get_active_positions(self) -> List['Position']:
        """
        Get the list of active positions where items can be added.
        
        Returns:
            List of active positions
        """
        ...
    
    def get_container_type(self) -> int:
        """
        Get the type of the container.
        
        Necessary for multi-container-type problems.
        
        Returns:
            The container type ID
        """
        ...
    
    def get_width(self) -> int:
        """Get the width of the container."""
        ...
    
    def get_length(self) -> int:
        """Get the length of the container."""
        ...
    
    def get_height(self) -> int:
        """Get the height of the container."""
        ...
    
    def get_max_weight(self) -> float:
        """Get the maximum weight the container can hold."""
        ...
    
    def get_loaded_volume(self) -> int:
        """Get the loaded volume (volume of inserted items)."""
        ...
    
    def get_loaded_weight(self) -> float:
        """Get the loaded weight (weight of inserted items)."""
        ...
    
    def get_parameter(self) -> 'ContainerParameter':
        """Get the parameters for behavior of this container."""
        ...
    
    def get_base_data(self) -> 'ContainerBaseData':
        """Get internal data structures of container."""
        ...
    
    def get_space(self, pos: 'Position') -> List['Space']:
        """
        Get available spaces at a position.
        
        Args:
            pos: The position to check
            
        Returns:
            List of available spaces
        """
        ...
