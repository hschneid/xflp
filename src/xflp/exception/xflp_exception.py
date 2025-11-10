"""
Exception classes for XFLP.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from enum import Enum, auto


class XFLPExceptionType(Enum):
    """Types of XFLP exceptions."""

    ILLEGAL_STATE = auto()
    ILLEGAL_ARGUMENT = auto()
    ILLEGAL_INPUT = auto()


class XFLPException(Exception):
    """
    Custom exception for XFLP errors.

    Attributes:
        type: The type of exception
        message: Error message
    """

    def __init__(self, exception_type: XFLPExceptionType, message: str,
                 cause: Exception = None):
        """
        Initialize an XFLP exception.

        Args:
            exception_type: Type of the exception
            message: Error message
            cause: Optional underlying exception
        """
        super().__init__(message)
        self.type = exception_type
        self.__cause__ = cause

    def get_type(self) -> XFLPExceptionType:
        """Get the exception type."""
        return self.type
