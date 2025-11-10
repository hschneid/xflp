# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
This packer puts the items in a sequence for each container type into single container.
Items will only be added to a container.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.opt.packer import Packer
from xflp.opt.construction.multitype.multi_bin_add_heuristic import MultiBinAddHeuristic

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.base.container.container import Container


class OneContainerNTypeAddPacker(Packer):
    """Packer for one container per type (add only)."""

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If packing fails
        """
        strategy = model.get_parameter().get_preferred_packing_strategy()

        # Create one container per type
        containers = self._get_containers(model)

        # Try to insert items in containers
        unplanned_items = MultiBinAddHeuristic(
            strategy,
            model.get_status_manager(),
            model.get_parameter()
        ).create_loading_plan(
            list(model.get_items()),
            containers
        )

        # Put result into model
        model.set_containers(containers)
        model.set_unplanned_items(unplanned_items)

    def _get_containers(self, model: 'XFLPModel') -> List['Container']:
        """Create one container per type."""
        return [ct.new_instance() for ct in model.get_container_types()]
