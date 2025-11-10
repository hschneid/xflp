"""
LoadType enum for item loading/unloading.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from enum import Enum, auto


class LoadType(Enum):
    """Defines whether an item is being loaded or unloaded."""

    LOAD = auto()
    UNLOAD = auto()
