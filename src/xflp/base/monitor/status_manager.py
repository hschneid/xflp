"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

Manages several Status Monitor objects from the user
and distributes the messages to the monitors.
"""

import time
from typing import List
from .status_monitor import StatusMonitor
from .status_code import StatusCode


class StatusManager:
    """
    Manages multiple status monitors and distributes messages to them.
    """

    def __init__(self):
        """Initialize the status manager."""
        self._observer_list: List[StatusMonitor] = []
        self._start_time: float = 0

    def add_observer(self, mon: StatusMonitor) -> None:
        """
        Add a status monitor observer.

        Args:
            mon: The status monitor to add
        """
        self._observer_list.append(mon)

    def fire_message(self, code: StatusCode, message: str) -> None:
        """
        Send a message to all registered observers.

        Args:
            code: The status code
            message: The message to send
        """
        for mon in self._observer_list:
            mon.get_message(code, message)

    def set_start_time(self) -> None:
        """Sets an internal value to the current time."""
        self._start_time = time.time()

    def get_duration_since_start_in_sec(self) -> int:
        """
        Get the duration in seconds since the set start time.

        Returns:
            Duration in seconds since start time was set
        """
        return int((time.time() - self._start_time))

    def clear_observer(self) -> None:
        """Removes all registered observers."""
        self._observer_list.clear()
