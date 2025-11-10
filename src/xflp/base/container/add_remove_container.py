"""
Add and remove container implementation.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Dict, List, Set, Optional, TYPE_CHECKING

from .container_base import ContainerBase
from ...util.collection.lp_list_map import LPListMap
from ..space.space_service import SpaceService

if TYPE_CHECKING:
    from ..item.item import Item
    from ..item.position import Position
    from ..item.space import Space
    from .container_parameter import ContainerParameter
    from .container import Container


class AddRemoveContainer(ContainerBase):
    """
    Container that supports both adding and removing items.
    
    More complex than AddContainer but allows full flexibility.
    Maintains position hierarchy and can restore positions when items are removed.
    """
    
    # Root position (virtual, not in 3D space)
    _ROOT_POS = None
    
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
        Initialize an add/remove container.
        
        Args:
            width: Container width
            length: Container length
            height: Container height
            max_weight: Maximum weight capacity
            container_type: Container type identifier
            parameter: Container behavior parameters
        """
        super().__init__(width, length, height, max_weight, container_type, parameter)
        self._init_add_remove_container()
    
    def _init_add_remove_container(self) -> None:
        """Initialize add/remove container specific structures."""
        from ..item.position import Position
        
        # Create root position if not exists
        if AddRemoveContainer._ROOT_POS is None:
            AddRemoveContainer._ROOT_POS = Position.of(-1, -1, -1, -1)
        
        # Position state tracking
        self._inactive_pos_list: Set['Position'] = set()
        self._covered_pos_list: List['Position'] = []
        
        # Position hierarchy (parent-child relationships)
        self._pos_follower_map: LPListMap['Position', 'Position'] = LPListMap()
        self._pos_ancestor_map: Dict['Position', 'Position'] = {}
        
        # Position to item mapping (which item created this position)
        self._position_item_map: Dict['Position', 'Item'] = {}
        
        # Unique position tracking
        self._unique_position_keys: Set[str] = set()
        
        # Space tracking
        self._space_positions: Dict['Position', List['Space']] = {}
        self._space_service = SpaceService()
        
        # Initialize position tree with root
        if self.active_pos_list:
            self._insert_tree(self.active_pos_list[0], AddRemoveContainer._ROOT_POS)
    
    def new_instance(self) -> 'Container':
        """
        Create a new empty instance with same configuration.
        
        Returns:
            New AddRemoveContainer instance
        """
        return AddRemoveContainer(
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
        self._switch_active_to_inactive(pos)
        
        # Switch covered positions to inactive
        cov_pos_list = self._find_covered_positions(item)
        for cov_pos in cov_pos_list:
            self._switch_active_to_covered(cov_pos)
            self._space_positions.pop(cov_pos, None)
        
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
                # Create the new position
                self._space_positions[new_pos] = new_spaces
                
                # The new position depends on the given position
                self._insert_tree(new_pos, pos)
                # This position was created by this item
                self._position_item_map[new_pos] = item
            else:
                # The free space is too small, position is not valid
                self._remove_new_position(new_pos)
        
        self._update_bearing_capacity([item])
        self._add_to_center_of_gravity(item, pos)
        
        self.history.append(item)
        return item.index
    
    def remove(self, item: 'Item') -> None:
        """
        Remove an item from the container.
        
        Restores positions and updates all affected structures.
        
        Args:
            item: The item to remove
        """
        from ..item.enums import PositionType
        
        lower_items = self.z_graph.get_items_below(item)
        
        # Remove item
        self._remove_item(item)
        
        position = self.item_position_map.pop(item, None)
        if position is None:
            raise IndexError("Item is not allocated to any position")
        
        # Reactivate position
        self._switch_inactive_to_active(position)
        self._recreate_spaces(position)
        
        # Check existing spaces for removed item
        self._check_existing_spaces_for_removed_item(item)
        
        # Reactivate covered positions
        covered_pos_list = self._find_uncovering_positions(item)
        for pos in covered_pos_list:
            self._switch_covered_to_active(pos)
            self._recreate_spaces(pos)
        
        # Project horizontal positions
        projectable_pos_h_list = self._find_projectable_horizontal_positions(item)
        for pos in projectable_pos_h_list:
            left_item = self._find_next_left_element(pos)
            new_position = Position.of(
                (left_item.xw if left_item else 0), pos.y, pos.z,
                pos.idx, pos.type
            )
            self._replace_position(pos, new_position)
            self._recreate_spaces(new_position)
        
        # Project vertical positions
        projectable_pos_v_list = self._find_projectable_vertical_positions(item)
        for pos in projectable_pos_v_list:
            lower_item = self._find_next_deeper_element(pos)
            new_position = Position.of(
                pos.x, (lower_item.yl if lower_item else 0), pos.z,
                pos.idx, pos.type
            )
            if pos != new_position:
                self._replace_position(pos, new_position)
                self._recreate_spaces(new_position)
        
        # Check position tree
        self._check_position(position)
        
        self._update_bearing_capacity(lower_items)
        self._remove_from_center_of_gravity(item, position)
        
        self.history.append(item)
    
    def _create_spaces(self, new_pos: 'Position') -> List['Space']:
        """Create spaces at a new position."""
        from ..item.space import Space
        
        max_space = Space.of(
            self.length - new_pos.y,
            self.width - new_pos.x,
            self.height - new_pos.z
        )
        space_items = self._space_service.get_items_in_space(new_pos, max_space, self.item_list)
        
        if not space_items:
            return [max_space]
        
        spaces = {max_space}
        for space_item in space_items:
            next_spaces = set()
            for space in spaces:
                next_spaces.update(
                    self._space_service.create_spaces_at_position(new_pos, space, space_item)
                )
            spaces = next_spaces
        
        return self._space_service.get_dominating_spaces(spaces)
    
    def _recreate_spaces(self, pos: 'Position') -> None:
        """Recreate spaces at a position."""
        self._space_positions[pos] = self._create_spaces(pos)
    
    def _switch_inactive_to_active(self, pos: 'Position') -> None:
        """Move position from inactive to active."""
        self._inactive_pos_list.discard(pos)
        self.active_pos_list.append(pos)
    
    def _switch_covered_to_active(self, pos: 'Position') -> None:
        """Move position from covered to active."""
        if pos in self._covered_pos_list:
            self._covered_pos_list.remove(pos)
        self.active_pos_list.append(pos)
    
    def _switch_active_to_inactive(self, pos: 'Position') -> None:
        """Move position from active to inactive."""
        if pos in self.active_pos_list:
            self.active_pos_list.remove(pos)
        self._inactive_pos_list.add(pos)
        self._space_positions.pop(pos, None)
    
    def _switch_active_to_covered(self, pos: 'Position') -> None:
        """Move position from active to covered."""
        if pos in self.active_pos_list:
            self.active_pos_list.remove(pos)
        self._covered_pos_list.append(pos)
    
    def _find_projectable_horizontal_positions(self, item: 'Item') -> List['Position']:
        """Find horizontal extended positions that need re-projection."""
        from ..item.enums import PositionType
        
        result: List['Position'] = []
        for pos in self.active_pos_list:
            if pos.type == PositionType.EXTENDED_H:
                if pos.x == item.xw and pos.y >= item.y and pos.y < item.yl:
                    result.append(pos)
        return result
    
    def _find_projectable_vertical_positions(self, item: 'Item') -> List['Position']:
        """Find vertical extended positions that need re-projection."""
        from ..item.enums import PositionType
        
        result: List['Position'] = []
        for pos in self.active_pos_list:
            if pos.type == PositionType.EXTENDED_V:
                if pos.y == item.yl and pos.x >= item.x and pos.x < item.xw:
                    result.append(pos)
        return result
    
    def _find_uncovering_positions(self, item: 'Item') -> List['Position']:
        """Find positions uncovered by item removal."""
        from ..item.enums import PositionType
        
        result: List['Position'] = []
        for pos in self._covered_pos_list:
            # Basic covered positions
            if pos.z == item.z and pos.x == item.x and pos.y >= item.y and pos.y < item.yl:
                result.append(pos)
            elif (pos.z == item.z and pos.x == item.xw and pos.y >= item.y and pos.y < item.yl and
                  pos.type == PositionType.EXTENDED_H and pos not in self.position_item_map.values()):
                result.append(pos)
            elif pos.z == item.z and pos.y == item.y and pos.x >= item.x and pos.x < item.xw:
                result.append(pos)
            elif (pos.z == item.z and pos.y == item.yl and pos.x >= item.x and pos.x < item.xw and
                  pos.type == PositionType.EXTENDED_V and pos not in self.position_item_map.values()):
                result.append(pos)
        return result
    
    def _insert_tree(self, entry: 'Position', ancestor: 'Position') -> None:
        """Insert position into hierarchy tree."""
        self._pos_ancestor_map[entry] = ancestor
        self._pos_follower_map.put(ancestor, entry)
    
    def _check_tree_and_remove(self, pos: 'Position') -> None:
        """Recursively check and remove unused follower positions."""
        map_list = self._pos_follower_map.get(pos)
        if map_list is not None:
            follower_list = list(map_list)
            for follower in follower_list:
                if follower in self._inactive_pos_list:
                    return
                
                # Deep check followers
                if (self._pos_follower_map.contains_key(follower) and 
                    self._pos_follower_map.get(follower)):
                    self._check_tree_and_remove(follower)
                
                # If still has followers, return
                if (self._pos_follower_map.contains_key(follower) and 
                    self._pos_follower_map.get(follower)):
                    return
            
            # Remove all followers
            for follower in follower_list:
                self._remove_position(follower)
    
    def _remove_position(self, pos: 'Position') -> None:
        """Remove a position completely."""
        from ..item.enums import PositionType
        
        if pos.type != PositionType.ROOT:
            self._pos_follower_map.remove(pos)
            ancestor = self._pos_ancestor_map.get(pos)
            if ancestor and self._pos_follower_map.get(ancestor):
                self._pos_follower_map.get(ancestor).remove(pos)
            self._pos_ancestor_map.pop(pos, None)
            
            if pos in self.active_pos_list:
                self.active_pos_list.remove(pos)
            self._inactive_pos_list.discard(pos)
            if pos in self._covered_pos_list:
                self._covered_pos_list.remove(pos)
            self._position_item_map.pop(pos, None)
            self._space_positions.pop(pos, None)
            self._unique_position_keys.discard(pos.key)
    
    def _remove_new_position(self, pos: 'Position') -> None:
        """Remove a newly created position that's invalid."""
        from ..item.enums import PositionType
        
        if pos.type != PositionType.ROOT:
            if self._pos_follower_map.contains_key(pos):
                self._pos_follower_map.remove(pos)
            
            pos_ancestor = self._pos_ancestor_map.get(pos)
            if pos_ancestor and self._pos_follower_map.get(pos_ancestor):
                if pos in self._pos_follower_map.get(pos_ancestor):
                    self._pos_follower_map.get(pos_ancestor).remove(pos)
                self._pos_ancestor_map.pop(pos, None)
            
            if pos in self.active_pos_list:
                self.active_pos_list.remove(pos)
            self._inactive_pos_list.discard(pos)
            if pos in self._covered_pos_list:
                self._covered_pos_list.remove(pos)
            self._position_item_map.pop(pos, None)
            self._space_positions.pop(pos, None)
            self._unique_position_keys.discard(pos.key)
    
    def _replace_position(self, old_position: 'Position', new_position: 'Position') -> None:
        """Replace an old position with a new one in all structures."""
        from ..item.enums import PositionType
        
        if old_position.type != PositionType.ROOT:
            # Update follower map
            if self._pos_follower_map.contains_key(old_position):
                self._pos_follower_map.put_list(new_position, self._pos_follower_map.get(old_position))
                for key in self._pos_follower_map.key_set():
                    follower = self._pos_follower_map.get(key)
                    if follower and old_position in follower:
                        follower.remove(old_position)
                        if new_position not in follower:
                            follower.append(new_position)
            
            # Update ancestor map
            if old_position in self._pos_ancestor_map:
                self._pos_ancestor_map[new_position] = self._pos_ancestor_map[old_position]
                for k, v in list(self._pos_ancestor_map.items()):
                    if v == old_position:
                        self._pos_ancestor_map[k] = new_position
            
            # Update position item map
            if old_position in self._position_item_map:
                self._position_item_map[new_position] = self._position_item_map[old_position]
            
            # Update item position map
            for item, pos in list(self.item_position_map.items()):
                if pos == old_position:
                    self.item_position_map[item] = new_position
            
            # Update position lists
            if old_position in self.active_pos_list:
                self.active_pos_list.remove(old_position)
                self.active_pos_list.append(new_position)
            if old_position in self._inactive_pos_list:
                self._inactive_pos_list.discard(old_position)
                self._inactive_pos_list.add(new_position)
            if old_position in self._covered_pos_list:
                self._covered_pos_list.remove(old_position)
                self._covered_pos_list.append(new_position)
            
            # Update space positions
            if old_position in self._space_positions:
                self._space_positions[new_position] = self._space_positions[old_position]
                del self._space_positions[old_position]
            
            # Update unique keys
            self._unique_position_keys.add(new_position.key)
            self._unique_position_keys.discard(old_position.key)
    
    def _check_position(self, pos: 'Position') -> None:
        """Check if a position can be removed."""
        from ..item.enums import PositionType
        
        self._check_tree_and_remove(pos)
        
        ancestor = self._pos_ancestor_map.get(pos)
        
        followers = self._pos_follower_map.get(pos)
        if (not followers or not followers) and ancestor in self.active_pos_list and pos.type != PositionType.ROOT:
            self._remove_position(pos)
            self._check_position(ancestor)
    
    def _remove_item(self, item: 'Item') -> None:
        """Remove item from internal structures."""
        index = item.index
        
        # Delete from Z-Graph
        self.z_graph.remove(item)
        
        self.item_list.remove(item.index)
        
        # Remove from coordinate maps
        x_list = self.x_map.get(item.x)
        if x_list and index in x_list:
            x_list.remove(index)
        xw_list = self.x_map.get(item.xw)
        if xw_list and index in xw_list:
            xw_list.remove(index)
        
        y_list = self.y_map.get(item.y)
        if y_list and index in y_list:
            y_list.remove(index)
        yl_list = self.y_map.get(item.yl)
        if yl_list and index in yl_list:
            yl_list.remove(index)
        
        z_list = self.z_map.get(item.z)
        if z_list and index in z_list:
            z_list.remove(index)
        zh_list = self.z_map.get(item.zh)
        if zh_list and index in zh_list:
            zh_list.remove(index)
        
        self.weight -= item.weight
        item.h = item.orig_h
        item.container_index = -1
    
    def _check_existing_spaces(self, new_item: 'Item') -> None:
        """Check if existing spaces need updating."""
        removable_positions: List['Position'] = []
        
        for position in self.active_pos_list:
            if (position.x >= new_item.xw or
                position.y >= new_item.yl or
                position.z >= new_item.zh):
                continue
            
            new_spaces = set()
            for space in self._space_positions.get(position, []):
                new_spaces.update(
                    self._space_service.create_spaces_at_position(position, space, new_item)
                )
            
            spaces = self._space_service.get_dominating_spaces(new_spaces)
            if spaces:
                self._space_positions[position] = spaces
            else:
                removable_positions.append(position)
        
        for removable_position in removable_positions:
            self._remove_position(removable_position)
    
    def _check_existing_spaces_for_removed_item(self, item: 'Item') -> None:
        """Update spaces after item removal."""
        for pos in self.active_pos_list:
            if pos not in self._space_positions:
                continue
            
            if (item.xw > pos.x and
                item.yl > pos.y and
                item.zh > pos.z):
                # Removed item is potentially in the range of an existing space
                self._recreate_spaces(pos)
    
    def get_space(self, pos: 'Position') -> List['Space']:
        """Get available spaces at a position."""
        return self._space_positions.get(pos, [])
