"""
Queue for processing items in bearing weight order.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Dict, List, TYPE_CHECKING

if TYPE_CHECKING:
    from ...item.item import Item
    from ..z_item_graph import ZItemGraph


class BearingWeightQueue:
    """
    A queue of stacked items where each item is returned when all upper items
    are processed.
    
    This ensures bearing weights are calculated top-down.
    """
    
    def __init__(self, nbr_of_items: int):
        """
        Initialize the queue.
        
        Args:
            nbr_of_items: Maximum number of items
        """
        self._is_processed: List[bool] = [False] * nbr_of_items
        self._upper_items: Dict[int, List[int]] = {}
    
    def add(self, new_item: 'Item', graph: 'ZItemGraph') -> None:
        """
        Add an item to queue with all their upper items.
        
        The given item is marked for processing.
        
        Args:
            new_item: The item to add
            graph: The Z-item graph
        """
        uppers = graph.get_items_above(new_item)
        upper_idx = [upper_item.index for upper_item in uppers]
        
        # If unprocessed upper items were found, add them instead
        for upper_item in uppers:
            if not self._is_processed[upper_item.index]:
                self.add(upper_item, graph)
                return
        
        self._upper_items[new_item.index] = upper_idx
    
    def get_next(self) -> int:
        """
        Get next item where all upper items are processed.
        
        Returns:
            The item index, or -1 if none available
        """
        for idx in list(self._upper_items.keys()):
            if self._are_all_processed(idx):
                del self._upper_items[idx]
                return idx
        return -1
    
    def has_more(self) -> bool:
        """
        Check if there are more items to process.
        
        Returns:
            True if items remain
        """
        return len(self._upper_items) > 0
    
    def _are_all_processed(self, item_idx: int) -> bool:
        """
        Check if all upper items of an item are processed.
        
        Args:
            item_idx: The item index
            
        Returns:
            True if all uppers are processed
        """
        upper_idx = self._upper_items.get(item_idx, [])
        return all(self._is_processed[idx] for idx in upper_idx)
    
    def set_processed(self, index: int) -> None:
        """
        Mark an item as processed.
        
        Args:
            index: The item index
        """
        if index < len(self._is_processed):
            self._is_processed[index] = True
