# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This solver tries to create very fast a reasonable solution.

It uses only construction heuristics.

Goal: All items should be packed into a single set of container types.
      If items are not fitting, then they will be placed in separate list. (unplanned)

Author: hschneid
"""

from typing import TYPE_CHECKING

from xflp.opt.xflp_base import XFLPBase
from xflp.opt.construction.onetype.one_container_one_type_packer import OneContainerOneTypePacker
from xflp.opt.construction.onetype.one_container_one_type_add_packer import OneContainerOneTypeAddPacker
from xflp.opt.construction.multitype.one_container_n_type_add_packer import OneContainerNTypeAddPacker
from xflp.report.load_type import LoadType

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel


class FastFixedContainerSolver(XFLPBase):
    """Fast fixed container solver using construction heuristics."""

    def __init__(self):
        self.one_type_packer = OneContainerOneTypePacker()
        self.one_type_add_packer = OneContainerOneTypeAddPacker()
        self.n_type_add_packer = OneContainerNTypeAddPacker()

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the fast fixed container solver.

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
                self.one_type_packer.execute(model)

    def _is_only_adding_items(self, model: 'XFLPModel') -> bool:
        """Check if all items are only being added (not unloaded)."""
        for item in model.get_items():
            if item.loading_type == LoadType.UNLOAD:
                return False
        return True
