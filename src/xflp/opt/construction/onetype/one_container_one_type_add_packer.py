# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This packer puts the items in a sequence into one container with one container type.
Items will only be added to a container.

Author: hschneid
"""

from typing import TYPE_CHECKING

from xflp.opt.packer import Packer
from xflp.opt.construction.onetype.single_bin_add_heuristic import SingleBinAddHeuristic

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel


class OneContainerOneTypeAddPacker(Packer):
    """Packer for single container with one type (add only)."""

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If packing fails
        """
        container = model.get_container_types()[0].new_instance()

        strategy = model.get_parameter().get_preferred_packing_strategy()

        unplanned_item_list = SingleBinAddHeuristic(
            strategy,
            model.get_status_manager(),
            model.get_parameter()
        ).create_loading_plan(
            list(model.get_items()),
            container
        )

        # Put result into model
        model.set_containers([container])
        model.set_unplanned_items(unplanned_item_list)
