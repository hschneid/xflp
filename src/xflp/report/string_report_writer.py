"""
StringReportWriter class for generating string representations of reports.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from .lp_report import LPReport


class StringReportWriter:
    """
    Writer class for converting load planning reports to string format.

    Generates a human-readable text representation of the load planning
    solution including utilization statistics and package events.
    """

    def write(self, report: LPReport) -> str:
        """
        Write a load planning report to a string.

        Args:
            report: The load planning report to write

        Returns:
            A formatted string representation of the report
        """
        lines: list[str] = []

        # Write summary header
        summary = report.get_summary()
        utilization_pct = summary.get_utilization() * 100.0
        lines.append(
            f">>> {summary.get_nbr_of_used_vehicles()} ({utilization_pct:.1f}%)"
        )

        # Write each container report
        for container_report in report.get_container_reports():
            container_summary = container_report.get_summary()
            container_utilization = (
                container_summary.get_max_used_volume()
                / container_summary.get_max_volume()
            ) * 100.0

            lines.append(
                f"--- {container_report.get_container_type_name()}"
                f"({container_utilization:.1f}%)"
            )

            # Write each package event
            for event in container_report.get_package_events():
                event_line = (
                    f"{event.id} "
                    f"{event.type.name} "
                    f"{event.weight} "
                    f"{event.weight_limit} "
                    f"{event.stacking_grp} | "
                    f"{event.w} "
                    f"{event.l} "
                    f"{event.h} : "
                    f"{event.x} "
                    f"{event.y} "
                    f"{event.z}"
                )
                lines.append(event_line)

        return "\n".join(lines) + "\n"
