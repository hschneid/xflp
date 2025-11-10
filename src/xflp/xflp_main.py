"""
XFLP - Main API entry point.

Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.

XFLP is the central user interface for this suite.
It combines all methods for data import, optimization execution, parameters and
retrieval of solutions.

The modeling of this class represents a state machine, where iteratively several
methods must be called. The execution method takes all inserted data and parameters
and starts the optimizers.
"""

from typing import Optional, List, TYPE_CHECKING

from .base.fleximport.flexi_importer import FlexiImporter
from .base.fleximport.item_data import ItemData
from .base.fleximport.container_data import ContainerData
from .base.xflp_model import XFLPModel
from .base.xflp_parameter import XFLPParameter
from .base.xflp_solution import XFLPSolution
from .base.monitor.status_manager import StatusManager
from .base.monitor.status_monitor import StatusMonitor
from .base.monitor.status_code import StatusCode
from .exception.xflp_exception import XFLPException, XFLPExceptionType
from .opt.xflp_opt_type import XFLPOptType
from .report.lp_report import LPReport

if TYPE_CHECKING:
    from .base.item.item import Item
    from .base.container.container import Container


class XFLP:
    """
    XFLP is the central user interface for the load planning suite.

    It combines all methods for data import, optimization execution, parameters,
    and retrieval of solutions.

    Example:
        >>> xflp = XFLP()
        >>> xflp.add_container().set_length(13500).set_width(2500).set_height(3000).set_max_weight(25000)
        >>> xflp.add_item().set_extern_id("Packet 1").set_length(1000).set_width(800).set_height(600).set_weight(500)
        >>> xflp.set_type_of_optimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
        >>> xflp.execute_load_planning()
        >>> report = xflp.get_report()
    """

    def __init__(self):
        """Initialize the XFLP solver."""
        # Importer and data warehouse
        self._importer = FlexiImporter()

        # Optimization type - Chosen by user
        self._opt_type: Optional[XFLPOptType] = None

        # Last created solution
        self._last_solution: Optional[XFLPSolution] = None

        # Planning parameters
        self._parameter = XFLPParameter()

        # Manages internal status messages to external observer
        self._status_manager = StatusManager()

    def execute_load_planning(self) -> None:
        """
        Calculate the loading problem with the previously inserted data
        via add_container() and add_item().

        Raises:
            XFLPException: If input data is invalid or missing
        """
        self._status_manager.fire_message(StatusCode.RUNNING, "XFLP started")

        # Flush import buffer
        self._importer.finish_import()

        # Init a planning model
        model = self._init()

        # Optimize the model
        self._opt_type.create_instance().execute(model)

        # Build solution object
        self._last_solution = XFLPSolution(model, self._importer.get_data_manager())

        self._status_manager.fire_message(StatusCode.FINISHED, "XFLP finished successfully.")

    def _init(self) -> XFLPModel:
        """
        Transform the read data into a model for optimization.

        Returns:
            XFLPModel instance ready for optimization

        Raises:
            XFLPException: If given data is incorrect or missing
        """
        self._status_manager.fire_message(StatusCode.RUNNING, "Initialisation")

        # Check input data
        if not self._importer.get_item_list():
            self._status_manager.fire_message(StatusCode.ABORT, "No items are given.")
            raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, "No items are given.")

        if not self._importer.get_container_list():
            self._status_manager.fire_message(
                StatusCode.ABORT, "No container information were set."
            )
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_INPUT, "No container information were set."
            )

        if self._parameter.get_max_nbr_of_items() <= 0:
            self._status_manager.fire_message(
                StatusCode.ABORT, "Number of allowed items must be greater than 0."
            )
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_INPUT,
                "Number of allowed items must be greater than 0.",
            )

        # Items
        items = self._importer.get_converted_item_list()

        # Container
        container_type_list = self._importer.get_converted_container_list(items, self._parameter)

        # Check phase
        self._check_items(items, container_type_list)

        return XFLPModel(
            items=items,
            container_types=container_type_list,
            parameter=self._parameter,
            status_manager=self._status_manager,
        )

    def _check_items(self, item_list: List["Item"], container_type_list: List["Container"]) -> None:
        """
        Validate items for correctness.

        Args:
            item_list: List of items to validate
            container_type_list: List of available container types

        Raises:
            XFLPException: If any item has invalid dimensions or properties
        """
        max_weight_capacity = (
            max((c.get_max_weight() for c in container_type_list), default=float("inf"))
        )

        for item in item_list:
            if item.w <= 0:
                msg = f"Width of item must be greater 0 : Item {item.external_index}"
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

            if item.l <= 0:
                msg = f"Length of item must be greater 0 : Item {item.external_index}"
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

            if item.h <= 0:
                msg = f"Height of item must be greater 0 : Item {item.external_index}"
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

            if item.immersive_depth < 0:
                msg = f"Immersive depth must be >= 0. Item {item.external_index} {item.immersive_depth}"
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

            if item.h - item.immersive_depth <= 0:
                msg = (
                    f"Immersive depth must not lead to negative height : "
                    f"Item {item.external_index} {item.h} {item.immersive_depth}"
                )
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

            if item.weight > max_weight_capacity:
                msg = (
                    f"Item is too heavy for any container. Item {item.external_index} "
                    f"item weight: {item.weight} max weight: {max_weight_capacity}"
                )
                self._status_manager.fire_message(StatusCode.ABORT, msg)
                raise XFLPException(XFLPExceptionType.ILLEGAL_INPUT, msg)

    def get_report(self) -> Optional[LPReport]:
        """
        Get the last planned solution as a report.

        All loading plan information can be acquired from this report.

        Returns:
            Report data structure with detailed loading plan information,
            or None if no solution was calculated.
        """
        if self._last_solution is not None:
            return self._last_solution.get_report()
        return None

    def has_unplanned_items(self) -> bool:
        """
        Check if the last calculated solution contains unplanned items.

        This function can be used to test the result for validity
        without costly report generation.

        Returns:
            True if there are unplanned items, False if all items could be planned
        """
        if self._last_solution is not None and self._last_solution.get_model() is not None:
            return len(self._last_solution.get_model().get_unplanned_items()) != 0
        return False

    def add_item(self) -> ItemData:
        """
        Acquire an item data object to insert data for a new item.

        The next call of this method will finalize the previously acquired
        item data object.

        Returns:
            ItemData builder object for configuring the item
        """
        return self._importer.get_item_data()

    def add_container(self) -> ContainerData:
        """
        Acquire a container data object to insert data for a new container.

        The next call of this method will finalize the previously acquired
        container data object.

        Returns:
            ContainerData builder object for configuring the container
        """
        return self._importer.get_container_data()

    def clear_items(self) -> None:
        """Clear all added items."""
        self._importer.clear_items()

    def clear_containers(self) -> None:
        """Remove all inserted containers and reset planning parameters to default."""
        self._importer.clear_containers()

    def clear_parameters(self) -> None:
        """Reset all parameters to default values."""
        self._parameter.clear()

    def set_status_monitor(self, monitor: StatusMonitor) -> None:
        """
        Insert a specified status monitor object where messages from
        the optimization are communicated.

        A fully transparent information flow is not given, as the
        performance loss would be significant.

        Args:
            monitor: Status monitor object to receive messages
        """
        self._status_manager.add_observer(monitor)

    def set_type_of_optimization(self, opt_type: XFLPOptType) -> None:
        """
        Specify the type of optimization.

        Examples:
            - Single Bin Packer
            - Multi Bin Packer

        Args:
            opt_type: Enum of available optimization types
        """
        self._opt_type = opt_type

    def get_parameter(self) -> XFLPParameter:
        """
        Get the parameter object for configuration.

        Returns:
            XFLPParameter instance for setting optimization parameters
        """
        return self._parameter
