"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

The default status monitor, which prints the messages to the
command line.
"""

from .status_monitor import StatusMonitor
from .status_code import StatusCode


class DefaultStatusMonitor(StatusMonitor):
    """
    Default implementation of StatusMonitor that prints messages to console.
    """

    def get_message(self, code: StatusCode, message: str) -> None:
        """
        Print the message to console with its status code.

        Args:
            code: Type of the message
            message: The message itself
        """
        print(f"[{code.name}] {message}")
