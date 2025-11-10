# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This packer puts the items in a sequence for each container type into multiple containers.
Items will only be added to a container.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.opt.packer import Packer
from xflp.opt.construction.multitype.multi_bin_add_heuristic import MultiBinAddHeuristic

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.base.container.container import Container


class NContainerNTypeAddPacker(Packer):
    """Packer for multiple containers with multiple types (add only)."""

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If packing fails
        """
        strategy = model.get_parameter().get_preferred_packing_strategy()
        heuristic = MultiBinAddHeuristic(
            strategy,
            model.get_status_manager(),
            model.get_parameter()
        )

        containers: List['Container'] = []
        unplanned_items = list(model.get_items())

        while len(unplanned_items) > 0:
            # Create one container per type
            new_containers = self._get_containers(model)

            # Try to insert items in containers
            rest_items = heuristic.create_loading_plan(unplanned_items, new_containers)

            containers.extend(new_containers)
            # Rest containers will go into next round
            unplanned_items = rest_items

        # Put result into model
        model.set_containers(containers)
        model.set_unplanned_items(unplanned_items)

    def _get_containers(self, model: 'XFLPModel') -> List['Container']:
        """Create one container per type."""
        return [ct.new_instance() for ct in model.get_container_types()]
