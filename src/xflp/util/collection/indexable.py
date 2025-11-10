"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

@author hschneid
"""

from typing import Protocol


class Indexable(Protocol):
    """
    Protocol for objects that can be indexed.

    Classes implementing this protocol should maintain an integer index
    that can be get and set.
    """

    def get_idx(self) -> int:
        """
        Get the index of this object.

        Returns:
            The current index.
        """
        ...

    def set_idx(self, idx: int) -> None:
        """
        Set the index of this object.

        Args:
            idx: The new index value.
        """
        ...
