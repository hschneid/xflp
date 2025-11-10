"""
Utility tools for item operations.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import List, TYPE_CHECKING

if TYPE_CHECKING:
    from .item import Item
    from .position import Position
    from ..container.container import Container


def get_cut_ratio(root_item: 'Item', cut_item: 'Item') -> float:
    """
    Calculate the area ratio of the intersection between two items.
    
    Returns the area size ratio of the cut between root item and cut item
    to root item's full area size.
    
    Args:
        root_item: The base item
        cut_item: The item to check intersection with
        
    Returns:
        cut area size / root area size
    """
    return get_cut_ratio_coords(
        root_item.x, root_item.y, root_item.w, root_item.l,
        cut_item
    )


def get_cut_ratio_coords(x: int, y: int, w: int, l: int, cut_item: 'Item') -> float:
    """
    Calculate the area ratio of the intersection between coordinates and an item.
    
    Args:
        x: X coordinate
        y: Y coordinate
        w: Width
        l: Length
        cut_item: The item to check intersection with
        
    Returns:
        cut area size / area size
    """
    xx = min(cut_item.xw, x + w) - max(cut_item.x, x)
    yy = min(cut_item.yl, y + l) - max(cut_item.y, y)
    
    return (xx * yy) / (w * l)


def find_items_below(container: 'Container', pos: 'Position', new_item: 'Item') -> List['Item']:
    """
    Find all items below a given position.
    
    Args:
        container: The container to search in
        pos: The position to check
        new_item: The item to be placed at the position
        
    Returns:
        List of items directly below the position
    """
    if not container.get_base_data().get_z_map().contains_key(pos.z):
        return []
    
    below_items: List['Item'] = []
    z_items = container.get_base_data().get_z_map().get(pos.z)
    
    if z_items is None:
        return []
    
    for i in range(len(z_items) - 1, -1, -1):
        lower_item = container.get_items()[z_items[i]]
        if (lower_item.zh == pos.z and
            lower_item.x < pos.x + new_item.w and
            lower_item.xw > pos.x and
            lower_item.y < pos.y + new_item.l and
            lower_item.yl > pos.y):
            below_items.append(lower_item)
    
    return below_items
