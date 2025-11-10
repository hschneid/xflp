"""
Abstract base class for containers.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Optional, TYPE_CHECKING

from ...util.collection.indexed_array_list import IndexedArrayList
from ...util.collection.lp_list_map import LPListMap
from .z_item_graph import ZItemGraph
from .constraints.load_bearing_checker import LoadBearingChecker

if TYPE_CHECKING:
    from ..item.item import Item
    from ..item.position import Position
    from .container_parameter import ContainerParameter
    from .container import Container


class ContainerBase(ABC):
    """
    Abstract base class for all container implementations.
    
    Provides common functionality for:
    - Item storage and indexing
    - Position management
    - Coordinate mapping (X, Y, Z)
    - Z-axis item graph (stacking relationships)
    - Load bearing calculations
    - Center of gravity tracking
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
        Initialize container with dimensions and parameters.
        
        Args:
            width: Container width
            length: Container length
            height: Container height
            max_weight: Maximum weight capacity
            container_type: Container type identifier
            parameter: Container behavior parameters
        """
        # Container index
        self.index: int = -1
        
        # Dimensions
        self.width = width
        self.height = height
        self.length = length
        self.max_weight = max_weight
        self.container_type = container_type
        self.weight: float = 0.0
        
        # Item storage
        self.item_list: IndexedArrayList['Item'] = IndexedArrayList()
        
        # Position management
        self.active_pos_list: List['Position'] = []
        
        # Coordinate maps (coordinate -> list of item indices)
        self.x_map: LPListMap[int, int] = LPListMap()
        self.y_map: LPListMap[int, int] = LPListMap()
        self.z_map: LPListMap[int, int] = LPListMap()
        
        # Z-axis item relationship graph
        self.z_graph = ZItemGraph()
        
        # Item to position bidirectional mapping
        self.item_position_map: Dict['Item', 'Position'] = {}
        self.position_item_map: Dict['Position', 'Item'] = {}
        
        # History of loaded items for solution report
        self.history: List['Item'] = []
        
        # Bearing capacity tracking
        self.bearing_capacities: Dict[int, float] = {}
        self.load_bearing_checker = LoadBearingChecker()
        
        # Position indexing
        self.max_pos_idx = 0
        
        # Parameters
        self.parameter = parameter
        
        # Center of gravity (Y-axis)
        self.center_of_gravity_for_y: float = 0.0
        
        self._init()
    
    def _init(self) -> None:
        """Initialize with root position."""
        from ..item.position import Position
        from ..item.enums import PositionType
        
        start = self._create_position(0, 0, 0, PositionType.ROOT)
        self.active_pos_list.append(start)
    
    def is_item_allowed(self, item: 'Item') -> bool:
        """
        Check if an item is allowed in this container.
        
        Args:
            item: The item to check
            
        Returns:
            True if item can be placed in this container
        """
        from ..fleximport.container_data import ContainerData
        
        # If item can be loaded on any container
        if (len(item.allowed_container_set) == 1 and
            ContainerData.DEFAULT_CONTAINER_TYPE in item.allowed_container_set):
            return True
        
        # Or only on specific ones
        return self.container_type in item.allowed_container_set
    
    def get_loaded_volume(self) -> int:
        """
        Calculate total loaded volume.
        
        Returns:
            Sum of volumes of all items
        """
        return sum(item.volume for item in self.item_list if item is not None)
    
    def get_loaded_weight(self) -> float:
        """
        Get total loaded weight.
        
        Returns:
            Current weight
        """
        return self.weight
    
    def _add_item(self, item: 'Item', pos: 'Position') -> None:
        """
        Add an item to the container's internal structures.
        
        Args:
            item: The item to add
            pos: The position to place it
        """
        # Adjust height for immersive depth
        item.h = self._retrieve_height(item, pos)
        
        item.set_position(pos)
        self.item_list.add(item)
        item.container_index = self.index
        
        # Bidirectional mapping
        self.item_position_map[item] = pos
        self.position_item_map[pos] = item
        
        # Update coordinate maps
        self.x_map.put(item.x, item.index)
        self.x_map.put(item.xw, item.index)
        self.y_map.put(item.y, item.index)
        self.y_map.put(item.yl, item.index)
        self.z_map.put(item.z, item.index)
        self.z_map.put(item.zh, item.index)
        
        self.weight += item.weight
        
        # Insert into Z-Graph
        self.z_graph.add(item, self.item_list, self.z_map)
    
    def _find_insert_positions(self, item: 'Item') -> List['Position']:
        """
        Find new insert positions after placing an item.
        
        Creates 3 basic positions and up to 2 projected positions.
        
        Args:
            item: The item that was just placed
            
        Returns:
            List of new positions
        """
        from ..item.enums import PositionType
        
        pos_list: List['Position'] = []
        
        # 3 basic positions
        vertical_position = None
        horizontal_position = None
        
        if item.yl < self.length:
            vertical_position = self._create_position(item.x, item.yl, item.z, PositionType.BASIC)
            pos_list.append(vertical_position)
        
        if item.xw < self.width:
            horizontal_position = self._create_position(item.xw, item.y, item.z, PositionType.BASIC)
            pos_list.append(horizontal_position)
        
        if item.z + item.h < self.height:
            pos_list.append(self._create_position(item.x, item.y, item.z + item.h, PositionType.BASIC))
        
        # 2 projected positions (only on ground level)
        if item.z == 0:
            if item.x > 0 and vertical_position is not None:
                left_element = self._find_next_left_element(vertical_position)
                left_pos = left_element.xw if left_element else 0
                
                if left_pos < item.x:
                    pos_list.append(
                        self._create_position(left_pos, item.yl, item.z, PositionType.EXTENDED_H)
                    )
            
            if item.y > 0 and horizontal_position is not None:
                lower_element = self._find_next_deeper_element(horizontal_position)
                lower_pos = lower_element.yl if lower_element else 0
                
                if lower_pos < item.y:
                    pos_list.append(
                        self._create_position(item.xw, lower_pos, item.z, PositionType.EXTENDED_V)
                    )
        
        return pos_list
    
    def _norm_position(self, item: 'Item', pos: 'Position', is_rotated: bool) -> 'Position':
        """
        Normalize position by rotating item if necessary.
        
        Args:
            item: The item
            pos: The position
            is_rotated: Whether to rotate the item
            
        Returns:
            The position (unchanged)
        """
        if is_rotated:
            item.rotate()
        return pos
    
    def _find_next_left_element(self, pos: 'Position') -> Optional['Item']:
        """
        Find the next element to the left of a position.
        
        Args:
            pos: The position to check
            
        Returns:
            The leftmost item, or None
        """
        left_item = None
        
        for item in self.item_list:
            if (item is None or 
                item.y > pos.y or 
                item.yl < pos.y or 
                item.x > pos.x or 
                item.xw > pos.x or 
                pos.y == item.yl):
                continue
            
            if left_item is None or item.xw > left_item.xw:
                left_item = item
        
        return left_item
    
    def _find_next_deeper_element(self, pos: 'Position') -> Optional['Item']:
        """
        Find the next element deeper (in Y direction) than a position.
        
        Args:
            pos: The position to check
            
        Returns:
            The deepest item, or None
        """
        lower_item = None
        
        for item in self.item_list:
            if (item is None or 
                item.x > pos.x or 
                item.xw < pos.x or 
                item.y > pos.y or 
                item.yl > pos.y or 
                pos.x == item.xw):
                continue
            
            if lower_item is None or item.yl > lower_item.yl:
                lower_item = item
        
        return lower_item
    
    def _find_covered_positions(self, item: 'Item') -> List['Position']:
        """
        Find positions covered by the newly placed item.
        
        Args:
            item: The item
            
        Returns:
            List of covered positions
        """
        covered_positions: List['Position'] = []
        
        for pos in self.active_pos_list:
            # Position on lower edge of object
            if (pos.z == item.z and 
                pos.x >= item.x and 
                pos.x < item.xw and 
                pos.y == item.y):
                covered_positions.append(pos)
            # Position on left edge of object
            elif (pos.z == item.z and 
                  pos.y >= item.y and 
                  pos.y < item.yl and 
                  pos.x == item.x):
                covered_positions.append(pos)
        
        return covered_positions
    
    def _create_position(self, x: int, y: int, z: int, pos_type) -> 'Position':
        """
        Create a new position with unique index.
        
        Args:
            x: X coordinate
            y: Y coordinate
            z: Z coordinate
            pos_type: Position type
            
        Returns:
            New position
        """
        from ..item.position import Position
        
        pos = Position.of(x, y, z, self.max_pos_idx, pos_type)
        self.max_pos_idx += 1
        return pos
    
    def _update_bearing_capacity(self, items: List['Item']) -> None:
        """
        Update bearing capacities for given items.
        
        Args:
            items: Items to update
        """
        self.load_bearing_checker.update(self, items)
    
    def _retrieve_height(self, item: 'Item', pos: 'Position') -> int:
        """
        Calculate actual height considering immersive depth.
        
        If stacking on other items with immersive depth, reduce height.
        
        Args:
            item: The item
            pos: The position
            
        Returns:
            Adjusted height
        """
        from ..item.tools import find_items_below
        
        if pos.z == 0:
            return item.h
        
        lower_items = find_items_below(self, pos, item)
        if not lower_items:
            return item.h
        
        min_immersive_depth = min(li.immersive_depth for li in lower_items)
        
        new_height = item.h - min_immersive_depth
        return max(1, new_height)
    
    def _add_to_center_of_gravity(self, item: 'Item', pos: 'Position') -> None:
        """
        Update center of gravity after adding item.
        
        Args:
            item: The item
            pos: The position
        """
        self.center_of_gravity_for_y += (pos.y + (item.l / 2.0)) * item.weight
    
    def _remove_from_center_of_gravity(self, item: 'Item', pos: 'Position') -> None:
        """
        Update center of gravity after removing item.
        
        Args:
            item: The item
            pos: The position
        """
        self.center_of_gravity_for_y -= (pos.y + (item.l / 2.0)) * item.weight
    
    # Public interface methods
    
    def get_items(self) -> List['Item']:
        """Get list of items in container."""
        return self.item_list
    
    def get_active_positions(self) -> List['Position']:
        """Get list of active positions."""
        return self.active_pos_list
    
    def get_history(self) -> List['Item']:
        """Get history of loaded/unloaded items."""
        return self.history
    
    def get_parameter(self) -> 'ContainerParameter':
        """Get container parameters."""
        return self.parameter
    
    def get_width(self) -> int:
        """Get container width."""
        return self.width
    
    def get_height(self) -> int:
        """Get container height."""
        return self.height
    
    def get_length(self) -> int:
        """Get container length."""
        return self.length
    
    def get_max_weight(self) -> float:
        """Get maximum weight capacity."""
        return self.max_weight
    
    def get_container_type(self) -> int:
        """Get container type."""
        return self.container_type
    
    def get_base_data(self) -> 'ContainerBase':
        """Get base data (self)."""
        return self
    
    # ContainerBaseData protocol implementation
    
    def get_x_map(self) -> LPListMap[int, int]:
        """Get X coordinate map."""
        return self.x_map
    
    def get_y_map(self) -> LPListMap[int, int]:
        """Get Y coordinate map."""
        return self.y_map
    
    def get_z_map(self) -> LPListMap[int, int]:
        """Get Z coordinate map."""
        return self.z_map
    
    def get_z_graph(self) -> ZItemGraph:
        """Get Z-axis item graph."""
        return self.z_graph
    
    def get_bearing_capacities(self) -> Dict[int, float]:
        """Get bearing capacities."""
        return self.bearing_capacities
    
    def get_center_of_gravity_for_y(self) -> float:
        """Get center of gravity for Y axis."""
        return self.center_of_gravity_for_y
    
    # Abstract methods to be implemented by subclasses
    
    @abstractmethod
    def new_instance(self) -> 'Container':
        """Create a new instance of this container type."""
        pass
    
    @abstractmethod
    def add(self, item: 'Item', position: 'Position', is_rotated: bool) -> int:
        """Add an item to the container."""
        pass
    
    @abstractmethod
    def remove(self, item: 'Item') -> None:
        """Remove an item from the container."""
        pass
    
    @abstractmethod
    def get_space(self, pos: 'Position') -> List:
        """Get available space at a position."""
        pass
