# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This enumeration holds all for the user available optimization methods.

Author: hschneid
"""

from enum import Enum
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.opt.xflp_base import XFLPBase


class XFLPOptType(Enum):
    """Enumeration of available optimization methods."""

    SINGLE_CONTAINER_OPTIMIZER = "SingleBinOptimizedPacker"
    FAST_FIXED_CONTAINER_PACKER = "FastFixedContainerSolver"
    BEST_FIXED_CONTAINER_PACKER = "BestFixedContainerSolver"
    FAST_MIN_CONTAINER_PACKER = "FastMinContainerSolver"
    BEST_MIN_CONTAINER_PACKER = "BestMinContainerSolver"

    def create_instance(self) -> 'XFLPBase':
        """
        Creates an instance of the chosen opt type class.

        Returns:
            An object instance of XFLPBase

        Raises:
            XFLPException: If optimization procedure cannot be instantiated
        """
        from xflp.exception.xflp_exception import XFLPException, XFLPExceptionType

        try:
            if self == XFLPOptType.SINGLE_CONTAINER_OPTIMIZER:
                from xflp.opt.grasp.single_bin_optimized_packer import SingleBinOptimizedPacker
                return SingleBinOptimizedPacker()
            elif self == XFLPOptType.FAST_FIXED_CONTAINER_PACKER:
                from xflp.opt.fast_fixed_container_solver import FastFixedContainerSolver
                return FastFixedContainerSolver()
            elif self == XFLPOptType.BEST_FIXED_CONTAINER_PACKER:
                from xflp.opt.best_fixed_container_solver import BestFixedContainerSolver
                return BestFixedContainerSolver()
            elif self == XFLPOptType.FAST_MIN_CONTAINER_PACKER:
                from xflp.opt.fast_min_container_solver import FastMinContainerSolver
                return FastMinContainerSolver()
            elif self == XFLPOptType.BEST_MIN_CONTAINER_PACKER:
                from xflp.opt.best_min_container_solver import BestMinContainerSolver
                return BestMinContainerSolver()
            else:
                raise XFLPException(
                    XFLPExceptionType.ILLEGAL_STATE,
                    "Unknown optimization type"
                )
        except Exception as e:
            raise XFLPException(
                XFLPExceptionType.ILLEGAL_STATE,
                "No copy of optimization procedure possible",
                e
            )
