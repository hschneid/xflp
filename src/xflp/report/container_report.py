"""
ContainerReport class for tracking package events in a container.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import TYPE_CHECKING, Iterator

from .container_report_summary import ContainerReportSummary
from .lp_package_event import LPPackageEvent

if TYPE_CHECKING:
    from ..base.container import Container


class ContainerReport:
    """
    Report of a route by a list of events.

    The container report represents the data content of a load planning
    solution for this specific container.

    It contains the package events: loading of an item and unloading.
    """

    def __init__(self, container_type_name: str, container: "Container") -> None:
        """
        Initialize the container report.

        Args:
            container_type_name: The name of the container type
            container: The container instance
        """
        self._container_type_name: str = container_type_name
        self._container: "Container" = container
        self._summary: ContainerReportSummary = ContainerReportSummary(container)
        self._package_event_list: list[LPPackageEvent] = []

    def add(self, event: LPPackageEvent) -> None:
        """
        Add a package event to this container report.

        Args:
            event: The package event to add
        """
        self._summary.add(event)
        self._package_event_list.append(event)

    def get_container(self) -> "Container":
        """
        Get the container instance.

        Returns:
            The container
        """
        return self._container

    def get_summary(self) -> ContainerReportSummary:
        """
        Get the summary for this container report.

        Returns:
            The container report summary
        """
        return self._summary

    def get_package_events(self) -> list[LPPackageEvent]:
        """
        Get the list of package events.

        Returns:
            The list of package events
        """
        return self._package_event_list

    def get_container_type_name(self) -> str:
        """
        Get the container type name.

        Returns:
            The container type name
        """
        return self._container_type_name

    def __iter__(self) -> Iterator[LPPackageEvent]:
        """
        Make the container report iterable over package events.

        Returns:
            An iterator over package events
        """
        return iter(self._package_event_list)
