"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

Possible status codes of messages.
"""

from enum import Enum, auto


class StatusCode(Enum):
    """Enumeration of possible status codes for messages."""

    RUNNING = auto()
    RUNNING1 = auto()
    RUNNING2 = auto()
    FINISHED = auto()
    ABORT = auto()
    EXCEPTION = auto()
    UNDEF = auto()
