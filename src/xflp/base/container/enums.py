"""
Enums for container-related types.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from enum import Enum, auto


class GroundContactRule(Enum):
    """
    Defines stacking rules for items in containers.
    
    Rules for how items must be positioned relative to the container floor
    or other items:
    - FREE: Unlimited stacking, items can hang over a stack
    - COVERED: Item must stand on floor or other items for all 4 floor corners
    - SINGLE: Item must be stacked only upon one other item
    - MULTIPLE: Item can stand on ground or must cover multiple items
    """
    
    FREE = auto()
    COVERED = auto()
    SINGLE = auto()
    MULTIPLE = auto()


class ParameterType(Enum):
    """
    Defines types of container parameters.
    
    Used to identify different configuration parameters for containers:
    - LIFO_IMPORTANCE: Last-in-first-out importance factor
    - GROUND_CONTACT_RULE: Stacking/ground contact rule
    - AXLE_LOAD: Axle load distribution parameters
    """
    
    LIFO_IMPORTANCE = auto()
    GROUND_CONTACT_RULE = auto()
    AXLE_LOAD = auto()
