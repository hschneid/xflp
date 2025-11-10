"""
Container parameter interface protocol.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Protocol, Any
from .enums import ParameterType


class ContainerParameter(Protocol):
    """
    Protocol for container parameter storage and retrieval.
    
    Provides a generic key-value interface for container configuration.
    """
    
    def add(self, param_type: ParameterType, value: Any) -> None:
        """
        Add or update a parameter.
        
        Args:
            param_type: The type of parameter
            value: The parameter value
        """
        ...
    
    def get(self, param_type: ParameterType) -> Any:
        """
        Get a parameter value.
        
        Args:
            param_type: The type of parameter to retrieve
            
        Returns:
            The parameter value, or None if not set
        """
        ...
