# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
The packer NContainerOneTypeAddPacker plans a set of items into a set of containers. It uses only one
container type. All items will be packed into containers, because the number of containers is
unlimited. The ordering of items is predefined in the first step and will not changed during the
pack process.

The packer considers only items which will be added to a container. The adding and removing will
not be provided.

Author: hschneid
"""

from typing import List, TYPE_CHECKING

from xflp.opt.xflp_base import XFLPBase
from xflp.opt.construction.onetype.single_bin_add_heuristic import SingleBinAddHeuristic

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item


class NContainerOneTypeAddPacker(XFLPBase):
    """Packer for multiple containers with one type (add only)."""

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If packing fails
        """
        strategy = model.get_parameter().get_preferred_packing_strategy()
        heuristic = SingleBinAddHeuristic(
            strategy,
            model.get_status_manager(),
            model.get_parameter()
        )

        container_list: List['Container'] = []
        unpacked_items = list(model.get_items())

        container_idx = 0
        while len(unpacked_items) > 0 and self._has_more_container(model, container_idx):
            container_idx += 1

            # Create new container
            current_container = self._create_container(model)

            # Try to pack all unplanned items into the current empty container. The order
            # of items is untouched by this planning. Each unplanned item will be checked.
            unpacked_items = heuristic.create_loading_plan(unpacked_items, current_container)

            # Escape: When no item could be loaded into container, then stop the further planning
            if len(current_container.get_items()) == 0:
                break

            container_list.append(current_container)

        # Write created containers to model. There are no unplanned items.
        model.set_containers(container_list)
        model.set_unplanned_items(unpacked_items)

    def _has_more_container(self, model: 'XFLPModel', container_idx: int) -> bool:
        """Check if more containers are available."""
        return container_idx < model.get_parameter().get_max_nbr_of_container()

    def _create_container(self, model: 'XFLPModel') -> 'Container':
        """Create a new container instance."""
        return model.get_container_types()[0].new_instance()
