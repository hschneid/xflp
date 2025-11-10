"""
Z-Item graph entry for tracking stacking relationships.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import List, TYPE_CHECKING

if TYPE_CHECKING:
    from ..item.item import Item


class ZItemGraphEntry:
    """
    Entry in the Z-Item graph representing an item and its relationships.
    
    Tracks items below this item and the contact area ratios.
    
    Attributes:
        item: The item this entry represents
        lower_item_list: List of items directly below this item
        cut_ratio_list: Contact area ratios with lower items
        item_ratio_arr: Combined array of lower items and ratios
    """
    
    def __init__(self, item: 'Item', lower_item_list: List['Item']):
        """
        Initialize a graph entry.
        
        Args:
            item: The item this entry represents
            lower_item_list: List of items below this item
        """
        self.item = item
        self.lower_item_list = lower_item_list
        self.cut_ratio_list: List[float] = []
        self.item_ratio_arr = [self.lower_item_list, self.cut_ratio_list]
        
        self.update()
    
    def update(self) -> None:
        """
        Update the cut ratios based on current lower items.
        
        Calculates the contact area ratio between this item and each lower item,
        then normalizes so ratios sum to 1.0.
        """
        from ..item.tools import get_cut_ratio
        
        self.cut_ratio_list.clear()
        
        if not self.lower_item_list:
            return
        
        b_cuts = []
        total = 0.0
        
        for lower_item in self.lower_item_list:
            ratio = get_cut_ratio(self.item, lower_item)
            b_cuts.append(ratio)
            total += ratio
        
        # Normalize ratios to sum to 1.0
        if total > 0:
            for ratio in b_cuts:
                self.cut_ratio_list.append(ratio * (1.0 / total))
