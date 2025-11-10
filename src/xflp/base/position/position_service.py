"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import TYPE_CHECKING, List

from xflp.base.item import RotationType

if TYPE_CHECKING:
    from xflp.base.container import Container, AddContainer, AddRemoveContainer
    from xflp.base.item import Item, Position, Space


class PositionService:
    """Service for finding and validating item positions in containers."""

    @staticmethod
    def find_position_candidates(container: 'Container', item: 'Item') -> List['PositionCandidate']:
        """
        Returns all possible and valid insert positions for this item.

        Args:
            container: The container to place the item in
            item: The item to place

        Returns:
            List of valid position candidates
        """
        from xflp.base.container import ParameterType, AddContainer
        from xflp.base.container.constraints import StackingChecker, AxleLoadChecker
        from .position_candidate import PositionCandidate

        candidates = []

        item_w = item.w
        item_l = item.l
        nbr_of_active_positions = len(container.get_active_positions())

        # Check weight capacity of container
        if container.get_loaded_weight() + item.weight > container.get_max_weight():
            return candidates

        # For every rotation state
        rotation_type = PositionService._get_rotation_type(item)
        for rotation in range(rotation_type.get_rotation_type() + 1):
            if rotation > 0:
                item_w = item.l
                item_l = item.w

            # For every active position
            for k in range(nbr_of_active_positions - 1, -1, -1):
                pos = container.get_active_positions()[k]

                # Check overlapping with walls
                if (pos.x + item_w) > container.get_width():
                    continue
                if (pos.y + item_l) > container.get_length():
                    continue

                item_h = PositionService._retrieve_height(item, pos, container)
                if (pos.z + item_h) > container.get_height():
                    continue

                if PositionService._check_overlapping(container, item, item_w, item_l, pos, item_h):
                    continue

                # Check stacking restrictions
                if not StackingChecker.check_stacking_restrictions(container, pos, item, item_w, item_l):
                    continue

                # Check permissible axle load
                if not AxleLoadChecker.check_permissible_axle_load(container, item, pos):
                    continue

                # Create PositionCandidate if this item is rotated
                candidates.append(
                    PositionCandidate.of(pos, item, (rotation == 1))
                )

        return candidates

    @staticmethod
    def _check_overlapping(container: 'Container', item: 'Item', item_w: int,
                          item_l: int, pos: 'Position', item_h: int) -> bool:
        """Check if item overlaps with existing items or spaces."""
        from xflp.base.container import AddContainer

        if isinstance(container, AddContainer):
            return PositionService._check_overlapping_with_spaces(container, pos, item_w, item_l, item_h)
        else:
            return PositionService._check_overlapping_with_items(container, item, item_w, item_l, pos, item_h)

    @staticmethod
    def _check_overlapping_with_items(container: 'Container', item: 'Item',
                                     item_w: int, item_l: int, pos: 'Position',
                                     item_h: int) -> bool:
        """
        Checks if new item at this position will collide with other items in container.

        Returns:
            True if collision (invalid), False if valid
        """
        items = container.get_items()

        for idx in range(len(items) - 1, -1, -1):
            other_item = items[idx]
            if other_item is None:
                continue

            if (other_item.x < (pos.x + item_w) and other_item.xw > pos.x and
                other_item.y < (pos.y + item_l) and other_item.yl > pos.y and
                other_item.z < (pos.z + item_h) and other_item.zh > pos.z):
                return True

            # Check LIFO properties
            if PositionService._check_lifo(container, other_item, pos, item, item_w):
                return True

        return False

    @staticmethod
    def _check_overlapping_with_spaces(container: 'AddContainer', pos: 'Position',
                                      item_w: int, item_l: int, item_h: int) -> bool:
        """Check if item fits into available spaces."""
        spaces = container.get_space(pos)

        # If item is fitting into one of the spaces, then it is okay.
        for space in spaces:
            # Is item fitting into space
            if (space.l >= item_l and
                space.w >= item_w and
                space.h >= item_h):
                return False

        return True

    @staticmethod
    def _check_lifo(container: 'Container', other_item: 'Item', pos: 'Position',
                   new_item: 'Item', item_w: int) -> bool:
        """Check LIFO (Last In First Out) constraints."""
        from xflp.base.container import AddRemoveContainer, ParameterType

        if not isinstance(container, AddRemoveContainer):
            return False

        lifo_importance = container.get_parameter().get(ParameterType.LIFO_IMPORTANCE)

        if lifo_importance == 1:
            # Check if item is further from loading edge than position
            # Check if item is in unloading corridor to loading edge
            if (other_item.yl <= pos.y and
                other_item.x < (pos.x + item_w) and
                other_item.xw > pos.x):
                # If unloading rank of new item is greater than
                # unloading rank of the item, this position won't work
                return new_item.un_loading_loc > other_item.un_loading_loc

        return False

    @staticmethod
    def _get_rotation_type(item: 'Item') -> RotationType:
        """Get rotation type for item."""
        return RotationType.SPINNABLE if (item.spinable and item.w != item.l) else RotationType.FIX

    @staticmethod
    def _retrieve_height(item: 'Item', pos: 'Position', container: 'Container') -> int:
        """
        If it is a stacking position (z > 0), then the immersive depth of lower items
        must be checked. If this is the case, then the height of given item is reduced.
        """
        if pos.z == 0:
            return item.h

        min_immersive_depth = PositionService._get_min_immersive_depth_of_below(pos, item, container)
        new_height = item.h - min_immersive_depth
        return new_height if new_height > 0 else 1

    @staticmethod
    def _get_min_immersive_depth_of_below(pos: 'Position', new_item: 'Item',
                                         container: 'Container') -> int:
        """Get minimum immersive depth of items below."""
        z_map = container.get_base_data().get_z_map()

        if pos.z not in z_map:
            return 0

        min_immersive_depth_of_below = float('inf')

        z_items = z_map[pos.z]
        for i in range(len(z_items) - 1, -1, -1):
            lower_item = container.get_items()[z_items[i]]
            if (lower_item.zh == pos.z and
                lower_item.x < pos.x + new_item.w and
                lower_item.xw > pos.x and
                lower_item.y < pos.y + new_item.l and
                lower_item.yl > pos.y):
                min_immersive_depth_of_below = min(min_immersive_depth_of_below,
                                                   lower_item.get_immersive_depth())

        return int(min_immersive_depth_of_below) if min_immersive_depth_of_below != float('inf') else 0
