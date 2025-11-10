# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Item order random search optimization.

Author: hschneid
"""

import random
from typing import TYPE_CHECKING

from xflp.opt.xflp_base import XFLPBase
from xflp.base.monitor.status_code import StatusCode

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.opt.packer import Packer
    from xflp.base.item.item import Item


class ItemOrderRandomSearch(XFLPBase):
    """Random search optimization by shuffling item order."""

    def __init__(self, packer: 'Packer'):
        """
        Initialize the item order random search.

        Args:
            packer: The packer to use for each iteration
        """
        self.packer = packer
        self.rand = random.Random(1234)
        self.nbr_of_iterations = 2000

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the random search optimization.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If optimization fails
        """
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init Random search {len(model.get_unplanned_items())}"
        )

        best_items = list(model.get_items())
        best_value = len(model.get_unplanned_items())

        for k in range(self.nbr_of_iterations):
            items = best_items.copy()

            # Make random move in search space
            model.set_items(self._perturb(items))

            # Pack
            self.packer.execute(model)

            # Check if there are unplanned items
            if len(model.get_unplanned_items()) < best_value:
                best_items = list(model.get_items())
                model.get_status_manager().fire_message(
                    StatusCode.RUNNING,
                    f"Better {len(model.get_unplanned_items())}"
                )
                best_value = len(model.get_unplanned_items())

                if len(model.get_unplanned_items()) == 0:
                    break

        # Reset best solution
        model.set_items(best_items)
        self.packer.execute(model)

    def _perturb(self, items: list) -> list:
        """
        Perturb the item order by shuffling.

        Args:
            items: List of items

        Returns:
            Shuffled list of items
        """
        self.rand.shuffle(items)
        return items
