# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Item packer for single container with adding and removing items.

This packer puts the items in a sequence into one single container.
It is able to add and to remove the items with respect to their loading type (LOAD, UNLOAD).
There is no optimization in container allocation or item sequence.

Author: hschneid
"""

from typing import Dict, List, TYPE_CHECKING

from xflp.opt.packer import Packer
from xflp.base.position.position_service import PositionService
from xflp.base.monitor.status_code import StatusCode
from xflp.report.load_type import LoadType

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.base.container.container import Container
    from xflp.base.item.item import Item


class OneContainerOneTypePacker(Packer):
    """Packer for single container with one type (supports load/unload)."""

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If packing fails
        """
        container = model.get_container_types()[0].new_instance()
        strategy = model.get_parameter().get_preferred_packing_strategy().get_strategy()

        loaded_item_map: Dict[int, 'Item'] = {}
        unplanned_item_list: List['Item'] = []

        # For all items with respect to given sort order
        items = model.get_items()
        # Reset eventual presets
        self._reset_items(items)

        for item in items:
            if item.loading_type == LoadType.LOAD:
                insert_position = None

                # Check if item is allowed to this container type
                if container.is_item_allowed(item):
                    # Fetch existing insert positions
                    candidates = PositionService.find_position_candidates(container, item)

                    if len(candidates) > 0:
                        # Choose according to select strategy
                        insert_position = strategy.choose(item, container, candidates)

                # Add item to container
                if insert_position is not None:
                    container.add(
                        insert_position.item,
                        insert_position.position,
                        insert_position.is_rotated
                    )
                    loaded_item_map[item.external_index] = item
                else:
                    model.get_status_manager().fire_message(
                        StatusCode.RUNNING,
                        f"Item {item.index} could not be added."
                    )
                    unplanned_item_list.append(item)
            else:
                # Remove item from container
                # It is not checked if item was really loaded to container.
                # Before removing the unloading item must be replaced by the loaded item object
                # for index problems
                loaded_item = loaded_item_map.get(item.external_index)
                if loaded_item is not None:
                    container.remove(loaded_item)

        # Put result into model
        model.set_containers([container])
        model.set_unplanned_items(unplanned_item_list)

    def _reset_items(self, items: List['Item']) -> None:
        """Reset all items."""
        for item in reversed(items):
            item.reset()
