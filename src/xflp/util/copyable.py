"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.


@author hschneid
"""

from typing import Protocol, TypeVar

T = TypeVar('T', bound='Copyable')


class Copyable(Protocol):
    """
    Protocol for objects that can be copied.

    Classes implementing this protocol should provide a copy() method
    that returns a deep copy of the object.
    """

    def copy(self: T) -> T:
        """
        Create a copy of this object.

        Returns:
            A copy of this object.
        """
        ...
