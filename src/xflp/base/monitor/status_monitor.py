"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

Interface for Status Monitor Objects, which can be used to
transfer planning informations from inside the XFLP package
to the user.
"""

from abc import ABC, abstractmethod
from .status_code import StatusCode


class StatusMonitor(ABC):
    """
    Abstract base class for status monitoring.

    This interface allows users to receive planning information
    from inside the XFLP package.
    """

    @abstractmethod
    def get_message(self, code: StatusCode, message: str) -> None:
        """
        Method is called when a message occurs in the planning suite.

        Args:
            code: Type of the message
            message: The message itself
        """
        pass
