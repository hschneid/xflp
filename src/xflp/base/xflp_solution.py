"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.
"""

from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from .xflp_model import XFLPModel
    from xflp.base.fleximport import DataManager
    from xflp.report import LPReport


class XFLPSolution:
    """
    Solution container for XFLP optimization results.
    """

    def __init__(self, model: 'XFLPModel', data_manager: 'DataManager'):
        """
        Initialize a solution with a model and data manager.

        Args:
            model: The XFLP model containing the solution
            data_manager: The data manager for ID mappings
        """
        self._model = model
        self._data_manager = data_manager

    def get_report(self) -> 'LPReport':
        """
        Generate a report from the solution.

        Returns:
            LPReport containing packed containers and unplanned items
        """
        from xflp.report import LPReport, ContainerReport, LPPackageEvent

        rep = LPReport()

        # Add packed containers to report
        for con in self._model.get_containers():
            container_type_name = self._data_manager.get_container_type_name(
                con.get_container_type()
            )
            c_rep = ContainerReport(container_type_name, con)

            for item in con.get_history():
                e = LPPackageEvent(
                    self._data_manager.get_item_id(item.external_index),
                    item.x,
                    item.y,
                    item.z,
                    item.w,
                    item.l,
                    item.h,
                    item.stacking_group,
                    item.weight,
                    item.stacking_weight_limit,
                    False,  # is_invalid
                    item.loading_type,
                    item.get_volume(),
                    item.get_weight(),
                    0,  # nbr_of_stacks
                    item.is_rotated()
                )
                c_rep.add(e)

            rep.add(c_rep)

        # Add unplanned items to report
        for unplanned_item in self._model.get_unplanned_items():
            e = LPPackageEvent(
                self._data_manager.get_item_id(unplanned_item.external_index),
                -1,
                -1,
                -1,
                unplanned_item.w,
                unplanned_item.l,
                unplanned_item.h,
                unplanned_item.stacking_group,
                unplanned_item.weight,
                unplanned_item.stacking_weight_limit,
                False,  # is_invalid
                unplanned_item.loading_type,
                unplanned_item.get_volume(),
                0,
                0,  # nbr_of_stacks
                False
            )

            rep.add_unplanned_packages(e)

        return rep

    def get_model(self) -> 'XFLPModel':
        """Get the model."""
        return self._model
