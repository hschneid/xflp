"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.container import Container
    from .position_candidate import PositionCandidate


class TouchingPerimeterService:
    """Service for calculating touching perimeter of items."""

    @staticmethod
    def get_touching_perimeter(container: 'Container',
                              candidate: 'PositionCandidate',
                              item_touch_value: int,
                              consider_walls: bool,
                              consider_base_floor: bool) -> float:
        """
        Calculate the touching perimeter for an item at a candidate position.

        Args:
            container: The container
            candidate: The position candidate
            item_touch_value: Value multiplier for item touching
            consider_walls: Whether to consider container walls
            consider_base_floor: Whether to consider base floor

        Returns:
            The touching perimeter value
        """
        item = candidate.item
        pos = candidate.position

        value = 0
        w = item.w
        l = item.l
        h = item.h

        if candidate.is_rotated:
            w = item.l
            l = item.w

        xw = pos.x + w
        yl = pos.y + l
        zh = pos.z + h

        # X-Axis
        if pos.x == 0:
            # If walls must be considered, full side area is added
            if consider_walls:
                value += h * l

        if xw == container.get_width():
            # If walls must be considered, full side area is added
            if consider_walls:
                value += h * l

        x_item_list = []
        x_map = container.get_base_data().get_x_map()
        if pos.x in x_map:
            x_item_list.extend(x_map[pos.x])
        if xw in x_map:
            x_item_list.extend(x_map[xw])

        if len(x_item_list) > 0:
            # Check all items which touch pos.x
            for j in range(len(x_item_list) - 1, -1, -1):
                index = x_item_list[j]
                i = container.get_items()[index]

                if i.xw == pos.x or i.x == xw:
                    # Check length and height
                    if i.y > yl or i.yl < pos.y:
                        continue
                    if i.z > zh or i.zh < pos.z:
                        continue

                    # If items touch themselves, calculate the cutting plane
                    y_length = min(yl, i.yl) - max(i.y, pos.y)
                    z_length = min(zh, i.zh) - max(i.z, pos.z)
                    value += y_length * z_length * item_touch_value

        # Y-Axis
        if pos.y == 0:
            # If walls must be considered, full side area is added
            if consider_walls:
                value += h * w

        if yl == container.get_length():
            # If walls must be considered, full side area is added
            if consider_walls:
                value += h * w

        y_item_list = []
        y_map = container.get_base_data().get_y_map()
        if pos.y in y_map:
            y_item_list.extend(y_map[pos.y])
        if yl in y_map:
            y_item_list.extend(y_map[yl])

        if len(y_item_list) > 0:
            # Check all items which touch pos.y
            for j in range(len(y_item_list) - 1, -1, -1):
                index = y_item_list[j]
                i = container.get_items()[index]

                if i.yl == pos.y or i.y == yl:
                    # Check width and height
                    if i.x > xw or i.xw < pos.x:
                        continue
                    if i.z > zh or i.zh < pos.z:
                        continue

                    # If items touch themselves, calculate the cutting plane
                    x_length = min(xw, i.xw) - max(i.x, pos.x)
                    z_length = min(zh, i.zh) - max(i.z, pos.z)
                    value += x_length * z_length * item_touch_value

        # Z-Axis
        if pos.z == 0:
            # If walls must be considered, full side area is added
            if consider_base_floor:
                value += w * l

        if zh == container.get_height():
            # If walls must be considered, full side area is added
            if consider_walls:
                value += w * l

        z_item_list = []
        z_map = container.get_base_data().get_z_map()
        if pos.z in z_map:
            z_item_list.extend(z_map[pos.z])
        if zh in z_map:
            z_item_list.extend(z_map[zh])

        # Check all items which touch pos.z
        for j in range(len(z_item_list) - 1, -1, -1):
            index = z_item_list[j]
            i = container.get_items()[index]

            if i.zh == pos.z or i.z == zh:
                # Check length and width
                if i.y > yl or i.yl < pos.y:
                    continue
                if i.x > xw or i.xw < pos.x:
                    continue

                # If items touch themselves, calculate the cutting plane
                y_length = min(yl, i.yl) - max(i.y, pos.y)
                x_length = min(xw, i.xw) - max(i.x, pos.x)
                value += y_length * x_length * item_touch_value

        return value
