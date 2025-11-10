"""
LPReportSummary class for aggregating load planning statistics.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import TYPE_CHECKING

from .lp_package_event import LPPackageEvent
from .load_type import LoadType

if TYPE_CHECKING:
    from ..base.container import Container
    from .container_report import ContainerReport


class LPReportSummary:
    """
    Summary of a load planning solution.

    Aggregates statistics across all containers including vehicle usage,
    unloaded packages, and utilization metrics.
    """

    _LENGTH: int = 2

    def __init__(self) -> None:
        """Initialize the load planning report summary."""
        self._nbr_of_used_vehicles: int = 0
        self._nbr_of_not_loaded_packages: int = 0
        self._utilization_sum: float = 0.0
        self._data_map: dict["Container", list[float]] = {}

    def add(self, container_report: "ContainerReport") -> None:
        """
        Add a container report to the summary.

        Args:
            container_report: The container report to add
        """
        container = container_report.get_container()
        if container not in self._data_map:
            self._data_map[container] = [0.0] * self._LENGTH

        data = self._data_map[container]
        route_summary = container_report.get_summary()

        data[0] += 1
        self._nbr_of_used_vehicles += 1

        self._utilization_sum += route_summary.get_utilization()

    def add_unplanned_package(self, package: LPPackageEvent) -> None:
        """
        Add an unplanned package to the summary.

        Args:
            package: The unplanned package event
        """
        if package.type == LoadType.LOAD:
            self._nbr_of_not_loaded_packages += 1

    def get_nbr_of_used_vehicles_for_container(self, container: "Container") -> float:
        """
        Get the number of used vehicles for a specific container.

        Args:
            container: The container to query

        Returns:
            The number of used vehicles for this container
        """
        return self._data_map[container][0]

    def get_nbr_of_not_loaded_packages_for_container(
        self, container: "Container"
    ) -> float:
        """
        Get the number of not loaded packages for a specific container.

        Args:
            container: The container to query

        Returns:
            The number of not loaded packages for this container
        """
        return self._data_map[container][1]

    def get_nbr_of_used_vehicles(self) -> float:
        """
        Get the total number of used vehicles.

        Returns:
            The total number of used vehicles
        """
        return float(self._nbr_of_used_vehicles)

    def get_nbr_of_not_loaded_packages(self) -> float:
        """
        Get the total number of not loaded packages.

        Returns:
            The total number of not loaded packages
        """
        return float(self._nbr_of_not_loaded_packages)

    def get_utilization(self) -> float:
        """
        Get the average utilization across all containers.

        Returns:
            The average utilization as a ratio (0.0 to 1.0)
        """
        return (
            self._utilization_sum / self._nbr_of_used_vehicles
            if self._nbr_of_used_vehicles > 0
            else 0.0
        )
