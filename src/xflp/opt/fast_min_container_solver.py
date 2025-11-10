# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This solver tries to create very fast a reasonable solution.

It uses only construction heuristics.

Goal: Place all items into some instances of container types. The number of containers
      shall be minimal.

Author: hschneid
"""

from typing import TYPE_CHECKING

from xflp.opt.xflp_base import XFLPBase
from xflp.opt.construction.onetype.n_container_one_type_add_packer import NContainerOneTypeAddPacker
from xflp.opt.construction.multitype.n_container_n_type_add_packer import NContainerNTypeAddPacker
from xflp.report.load_type import LoadType

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel


class FastMinContainerSolver(XFLPBase):
    """Fast minimum container solver using construction heuristics."""

    def __init__(self):
        self.one_type_add_packer = NContainerOneTypeAddPacker()
        self.n_type_add_packer = NContainerNTypeAddPacker()

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the fast minimum container solver.

        Args:
            model: The XFLP model to optimize

        Raises:
            XFLPException: If optimization fails
        """
        if self._is_only_adding_items(model):
            if len(model.get_container_types()) > 1:
                self.n_type_add_packer.execute(model)
            else:
                self.one_type_add_packer.execute(model)
        else:
            if len(model.get_container_types()) > 1:
                raise NotImplementedError("Currently add/removing and multiple container types is not supported")
            else:
                raise NotImplementedError("Currently add/removing and single container types is not supported")

    def _is_only_adding_items(self, model: 'XFLPModel') -> bool:
        """Check if all items are only being added (not unloaded)."""
        for item in model.get_items():
            if item.loading_type == LoadType.UNLOAD:
                return False
        return True
