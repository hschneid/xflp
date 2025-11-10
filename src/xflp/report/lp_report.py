"""
LPReport class for representing load planning solutions.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from typing import Iterator

from .container_report import ContainerReport
from .lp_package_event import LPPackageEvent
from .lp_report_summary import LPReportSummary


class LPReport:
    """
    Structural representation of a load planning solution.

    Contains container reports, unplanned packages, and a summary
    of the overall load planning results.
    """

    def __init__(self) -> None:
        """Initialize a new load planning report."""
        self._summary: LPReportSummary = LPReportSummary()
        self._report_list: list[ContainerReport] = []
        self._unplanned_package_list: list[LPPackageEvent] = []

    def add(self, container_report: ContainerReport) -> None:
        """
        Add a container report to this load planning report.

        Only adds reports that have at least one loaded or unloaded package.

        Args:
            container_report: The container report to add
        """
        summary = container_report.get_summary()
        if (
            summary.get_nbr_of_loaded_packages()
            + summary.get_nbr_of_unloaded_packages()
            > 0
        ):
            self._summary.add(container_report)
            self._report_list.append(container_report)

    def add_unplanned_packages(self, package: LPPackageEvent) -> None:
        """
        Add an unplanned package to this report.

        Args:
            package: The unplanned package event
        """
        self._unplanned_package_list.append(package)
        self._summary.add_unplanned_package(package)

    def get_container_reports(self) -> list[ContainerReport]:
        """
        Get the list of container reports.

        Returns:
            The list of container reports
        """
        return self._report_list

    def get_unplanned_packages(self) -> list[LPPackageEvent]:
        """
        Get the list of unplanned packages.

        Returns:
            The list of unplanned packages
        """
        return self._unplanned_package_list

    def get_summary(self) -> LPReportSummary:
        """
        Get the summary for this load planning report.

        Returns:
            The load planning report summary
        """
        return self._summary

    def import_report(self, report: "LPReport") -> None:
        """
        Import the container reports of another report object into this report.

        Args:
            report: Another report object to import from
        """
        for container_report in report.get_container_reports():
            self.add(container_report)
        for event in report.get_unplanned_packages():
            self.add_unplanned_packages(event)

    def __iter__(self) -> Iterator[ContainerReport]:
        """
        Make the report iterable over container reports.

        Returns:
            An iterator over container reports
        """
        return iter(self._report_list)
