"""
Direct container parameter implementation.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Any, Optional
from .enums import ParameterType, GroundContactRule
from .constraints.axle_load_parameter import AxleLoadParameter


class DirectContainerParameter:
    """
    Simple dictionary-based implementation of ContainerParameter.
    
    Stores parameters directly with type-safe access.
    """
    
    def __init__(self):
        """Initialize with default values."""
        self._lifo_importance: float = 0.0
        self._ground_contact_rule: Optional[GroundContactRule] = None
        self._axle_load: Optional[AxleLoadParameter] = None
    
    def add(self, param_type: ParameterType, value: Any) -> None:
        """
        Add or update a parameter.
        
        Args:
            param_type: The type of parameter
            value: The parameter value
        """
        if param_type == ParameterType.LIFO_IMPORTANCE:
            self._lifo_importance = float(value)
        elif param_type == ParameterType.GROUND_CONTACT_RULE:
            self._ground_contact_rule = value
        elif param_type == ParameterType.AXLE_LOAD:
            self._axle_load = value
    
    def get(self, param_type: ParameterType) -> Any:
        """
        Get a parameter value.
        
        Args:
            param_type: The type of parameter to retrieve
            
        Returns:
            The parameter value, or None if not set
        """
        if param_type == ParameterType.LIFO_IMPORTANCE:
            return self._lifo_importance
        elif param_type == ParameterType.GROUND_CONTACT_RULE:
            return self._ground_contact_rule
        elif param_type == ParameterType.AXLE_LOAD:
            return self._axle_load
        return None
