# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Container position data class.

Author: hschneid
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.container.container import Container
    from xflp.base.position.position_candidate import PositionCandidate


class ContainerPosition:
    """Holds a container and a position candidate together."""

    def __init__(self, container: 'Container', position: 'PositionCandidate'):
        """
        Initialize container position.

        Args:
            container: The container
            position: The position candidate
        """
        self._container = container
        self._position = position

    def get_container(self) -> 'Container':
        """Get the container."""
        return self._container

    def get_position(self) -> 'PositionCandidate':
        """Get the position candidate."""
        return self._position
