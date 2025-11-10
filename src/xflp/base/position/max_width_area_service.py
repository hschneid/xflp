"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.container import Container


class MaxWidthAreaService:
    """Service for calculating maximum empty area in containers."""

    def get_max_empty_area(self, container: 'Container') -> float:
        """
        Calculate the maximum empty area in the container.

        Args:
            container: The container to analyze

        Returns:
            The estimated maximum empty area
        """
        pos_list = list(container.get_active_positions())
        pos_list.sort(key=lambda p: p.x)

        # Upper estimation of area
        area = 0
        for i in range(1, len(pos_list)):
            prev_pos = pos_list[i - 1]
            pos = pos_list[i]
            area += (pos.x - prev_pos.x) * (container.get_length() - prev_pos.y)

        # Add remaining area to wall
        # Assumption: there must always be one at the lower end
        last_pos = pos_list[-1]
        area += (container.get_width() - last_pos.x) * container.get_length()

        return area
