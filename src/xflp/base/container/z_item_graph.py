"""
Z-Item graph for tracking vertical stacking relationships.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import List, Optional, TYPE_CHECKING

from ...util.collection.set_index_array_list import SetIndexArrayList
from .z_item_graph_entry import ZItemGraphEntry

if TYPE_CHECKING:
    from ..item.item import Item
    from ...util.collection.lp_list_map import LPListMap


class ZItemGraph:
    """
    Graph structure tracking which items are stacked on top of each other.
    
    Maintains relationships between items along the Z-axis (height).
    For each item, tracks:
    - Which items are directly below it
    - Which items are directly above it
    
    This is used for load bearing calculations and stacking constraints.
    """
    
    def __init__(self):
        """Initialize an empty Z-item graph."""
        self._lower_list: SetIndexArrayList[ZItemGraphEntry] = SetIndexArrayList()
        self._upper_list: SetIndexArrayList[List['Item']] = SetIndexArrayList()
    
    def add(
        self, 
        new_item: 'Item', 
        item_list: List['Item'], 
        z_map: 'LPListMap[int, int]'
    ) -> None:
        """
        Add a new item into the Z-Graph.
        
        Links the item with items above and below it.
        
        Args:
            new_item: The item to add
            item_list: List of all items in container
            z_map: Map from Z-coordinate to item indices
        """
        # Process lower items
        lower_items = self._search_items_below(new_item, item_list, z_map.get(new_item.z))
        entry = ZItemGraphEntry(new_item, lower_items)
        self._lower_list.set(new_item.index, entry)
        
        # Update lower items with new upper item
        for lower_item in lower_items:
            if self._upper_list.get(lower_item.index) is None:
                self._upper_list.set(lower_item.index, [])
            self._upper_list.get(lower_item.index).append(new_item)
        
        # Process upper items
        upper_items = self._search_items_above(new_item, item_list, z_map.get(new_item.zh))
        self._upper_list.set(new_item.index, upper_items)
        
        # Update upper items with new lower item
        for upper_item in upper_items:
            entry = self._lower_list.get(upper_item.index)
            entry.lower_item_list.append(new_item)
            entry.update()
    
    def remove(self, item: 'Item') -> None:
        """
        Remove an item from the Z graph.
        
        Args:
            item: The item to remove
        """
        # Remove item from lower items
        if self._lower_list.get(item.index) is not None:
            lower_items = self._lower_list.get(item.index).lower_item_list
            for lower_item in lower_items:
                upper = self._upper_list.get(lower_item.index)
                if upper and item in upper:
                    upper.remove(item)
        
        # Remove item from upper items
        if self._upper_list.get(item.index) is not None:
            upper_items = self._upper_list.get(item.index)
            for upper_item in upper_items:
                entry = self._lower_list.get(upper_item.index)
                if entry and item in entry.lower_item_list:
                    entry.lower_item_list.remove(item)
        
        # Remove entries
        self._lower_list.remove(item.index)
        self._upper_list.remove(item.index)
    
    def size(self) -> int:
        """Get the number of items in the graph."""
        return self._upper_list.length()
    
    def get_items_below(self, item: 'Item') -> List['Item']:
        """
        Get all items directly below the given item.
        
        Args:
            item: The item to check
            
        Returns:
            List of items below
        """
        entry = self._lower_list.get(item.index)
        return entry.lower_item_list if entry else []
    
    def get_items_above(self, item: 'Item') -> List['Item']:
        """
        Get all items directly above the given item.
        
        Args:
            item: The item to check
            
        Returns:
            List of items above
        """
        items = self._upper_list.get(item.index)
        return items if items else []
    
    def _search_items_below(
        self, 
        item: 'Item', 
        item_list: List['Item'], 
        z_list: Optional[List[int]]
    ) -> List['Item']:
        """
        Find all items directly below the given item.
        
        Args:
            item: The item to check
            item_list: List of all items
            z_list: List of item indices at item.z height
            
        Returns:
            List of items below
        """
        result: List['Item'] = []
        
        if item.z == 0 or z_list is None:
            return result
        
        for z_item_idx in z_list:
            it = item_list[z_item_idx]
            if it is None:
                continue
                
            if (it.zh == item.z and
                it.xw > item.x and it.x < item.xw and
                it.yl > item.y and it.y < item.yl):
                result.append(it)
        
        return result
    
    def _search_items_above(
        self, 
        item: 'Item', 
        item_list: List['Item'], 
        z_list: Optional[List[int]]
    ) -> List['Item']:
        """
        Find all items directly above the given item.
        
        Args:
            item: The item to check
            item_list: List of all items
            z_list: List of item indices at item.zh height
            
        Returns:
            List of items above
        """
        result: List['Item'] = []
        
        if z_list is None:
            return result
        
        for z_item_idx in z_list:
            it = item_list[z_item_idx]
            if it is None:
                continue
                
            if (it.z == item.zh and
                it.xw > item.x and it.x < item.xw and
                it.yl > item.y and it.y < item.yl):
                result.append(it)
        
        return result
