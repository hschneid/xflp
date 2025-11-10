"""
ContainerReportSummary class for aggregating container metrics.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import TYPE_CHECKING

from .lp_package_event import LPPackageEvent
from .load_type import LoadType

if TYPE_CHECKING:
    from ..base.container import Container


class ContainerReportSummary:
    """
    Summary of values for a container/route.

    Events can be added, where the values are updated directly.
    Tracks the number of loaded and unloaded packages, as well as
    volume and weight utilization.
    """

    def __init__(self, container: "Container") -> None:
        """
        Initialize the container report summary.

        Args:
            container: The container for which to create the summary
        """
        self._nbr_of_loaded_packages: int = 0
        self._nbr_of_unloaded_packages: int = 0
        self._max_used_volume: float = 0.0
        self._max_volume: float = float(
            container.get_height() * container.get_length() * container.get_width()
        )
        self._max_used_weight: float = 0.0

    def add(self, event: LPPackageEvent) -> None:
        """
        Add a package event and update the summary statistics.

        Args:
            event: The package event to add
        """
        if event.type == LoadType.LOAD:
            self._nbr_of_loaded_packages += 1
            # Only loaded items increase the max loaded volume/weight values
            self._max_used_volume += event.used_volume_in_container
            self._max_used_weight += event.used_weight_in_container
        elif event.type == LoadType.UNLOAD:
            self._nbr_of_unloaded_packages += 1

    def get_nbr_of_loaded_packages(self) -> int:
        """
        Get the number of loaded packages.

        Returns:
            The number of loaded packages
        """
        return self._nbr_of_loaded_packages

    def get_nbr_of_unloaded_packages(self) -> int:
        """
        Get the number of unloaded packages.

        Returns:
            The number of unloaded packages
        """
        return self._nbr_of_unloaded_packages

    def get_max_used_volume(self) -> float:
        """
        Get the maximum used volume.

        Returns:
            The maximum used volume
        """
        return self._max_used_volume

    def get_max_used_weight(self) -> float:
        """
        Get the maximum used weight.

        Returns:
            The maximum used weight
        """
        return self._max_used_weight

    def get_max_volume(self) -> float:
        """
        Get the maximum volume of the container.

        Returns:
            The maximum volume
        """
        return self._max_volume

    def get_utilization(self) -> float:
        """
        Calculate the utilization of the container.

        Returns:
            The utilization as a ratio (0.0 to 1.0)
        """
        return self._max_used_volume / self._max_volume if self._max_volume > 0 else 0.0
