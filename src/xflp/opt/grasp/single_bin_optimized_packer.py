# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Single bin optimized packer using local search.

Author: hschneid
"""

import random
from typing import List, TYPE_CHECKING

from xflp.opt.xflp_base import XFLPBase
from xflp.opt.construction.onetype.one_container_one_type_packer import OneContainerOneTypePacker
from xflp.base.monitor.status_code import StatusCode

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel
    from xflp.base.item.item import Item


class SingleBinOptimizedPacker(XFLPBase):
    """Optimized packer for single bin using local search algorithms."""

    def __init__(self):
        """Initialize the single bin optimized packer."""
        self.packer = OneContainerOneTypePacker()
        self.rand = random.Random(1234)

    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the optimized packer.

        Args:
            model: The XFLP model

        Raises:
            XFLPException: If optimization fails
        """
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init {len(model.get_unplanned_items())}"
        )

        # Commented out other search methods as in original Java code
        # if len(model.get_unplanned_items()) > 0:
        #     self._do_best_swap(model)
        # if len(model.get_unplanned_items()) > 0:
        #     self._do_swap_next_local_search(model)
        # if len(model.get_unplanned_items()) > 0:
        #     self._do_swap_local_search(model)
        if len(model.get_unplanned_items()) > 0:
            self._do_relocate_local_search(model)

    def _do_best_swap(self, model: 'XFLPModel') -> None:
        """Perform best swap search."""
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init Swap Next {len(model.get_unplanned_items())}"
        )

        items = list(model.get_items())
        best_value = [len(model.get_unplanned_items()), -1, -1, 0]

        for i in range(len(items) - 2):
            # Change item queue
            self._swap(items, i, i + 1)
            # Pack
            model.set_items(items)
            self.packer.execute(model)

            model.get_status_manager().fire_message(
                StatusCode.RUNNING,
                f"{i} {len(model.get_unplanned_items())}"
            )

            # Check if there are unplanned items
            if len(model.get_unplanned_items()) < best_value[0]:
                best_value[0] = len(model.get_unplanned_items())
                best_value[1] = i
                best_value[2] = i + 1
                best_value[3] = 1

                if len(model.get_unplanned_items()) == 0:
                    return

            # Change back
            self._swap(items, i, i + 1)

        if best_value[3] == 1:
            self._swap(items, best_value[1], best_value[2])

    def _do_swap_next_local_search(self, model: 'XFLPModel') -> None:
        """Perform swap next local search."""
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init Swap Next LS {len(model.get_unplanned_items())}"
        )

        items = list(model.get_items())
        best_value = [len(model.get_unplanned_items()), -1, -1, 1]

        while best_value[3] == 1:
            best_value[3] = 0

            for i in range(len(items) - 2):
                # Change item queue
                self._swap(items, i, i + 1)
                # Pack
                model.set_items(items)
                self.packer.execute(model)

                # Check if there are unplanned items
                if len(model.get_unplanned_items()) < best_value[0]:
                    self._set_best_move(model, best_value, i, i + 1)

                # Change back
                self._swap(items, i, i + 1)

            if best_value[3] == 1:
                self._swap(items, best_value[1], best_value[2])

    def _do_swap_local_search(self, model: 'XFLPModel') -> None:
        """Perform swap local search."""
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init Swap {len(model.get_unplanned_items())}"
        )

        items = list(model.get_items())
        best_value = [len(model.get_unplanned_items()), -1, -1, 1]

        while best_value[3] == 1:
            best_value[3] = 0

            for i in range(len(items) - 2):
                for j in range(i + 1, len(items) - 1):
                    # Change item queue
                    self._swap(items, i, j)
                    # Pack
                    model.set_items(items)
                    self.packer.execute(model)

                    # Check if there are unplanned items
                    if len(model.get_unplanned_items()) < best_value[0]:
                        self._set_best_move(model, best_value, i, j)

                        if len(model.get_unplanned_items()) == 0:
                            return

                    # Change back
                    self._swap(items, i, j)

            if best_value[3] == 1:
                self._swap(items, best_value[1], best_value[2])

    def _do_relocate_local_search(self, model: 'XFLPModel') -> None:
        """Perform relocate local search."""
        self.packer.execute(model)
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Init RelocateLS {len(model.get_unplanned_items())}"
        )

        items = list(model.get_items())
        best_items = items.copy()
        best_value = [len(model.get_unplanned_items()), -1, -1, 1]

        for k in range(10):
            model.get_status_manager().fire_message(StatusCode.RUNNING, f"iter {k}")
            best_value[3] = 1

            while best_value[3] == 1:
                best_value[3] = 0

                for i in range(len(items) - 1):
                    for j in range(len(items)):
                        if j == i + 1 or j == i:
                            continue

                        # Change item queue
                        self._move(items, i, j)
                        # Pack
                        model.set_items(items)
                        self.packer.execute(model)

                        model.get_status_manager().fire_message(
                            StatusCode.RUNNING,
                            f"{i} {j} {best_value[0]}"
                        )

                        # Check if there are unplanned items
                        if len(model.get_unplanned_items()) < best_value[0]:
                            self._set_best_move(model, best_value, i, j)
                            best_items = items.copy()

                            if len(model.get_unplanned_items()) == 0:
                                return

                        # Change back
                        if i < j:
                            self._move(items, j - 1, i)
                        else:
                            self._move(items, j, i + 1)

                if best_value[3] == 1:
                    self._swap(items, best_value[1], best_value[2])

            # Make random move in search space
            self._perturb(items)

        # Reset best solution
        model.set_items(best_items)
        self.packer.execute(model)

    def _set_best_move(
        self,
        model: 'XFLPModel',
        best_value: List[int],
        value1: int,
        value2: int
    ) -> None:
        """Set the best move found."""
        best_value[0] = len(model.get_unplanned_items())
        best_value[1] = value1
        best_value[2] = value2
        best_value[3] = 1
        model.get_status_manager().fire_message(
            StatusCode.RUNNING,
            f"Better {best_value}"
        )

    def _perturb(self, items: List['Item']) -> None:
        """Perturb the item order randomly."""
        for n in range(4):
            i = self.rand.randint(0, len(items) - 1)
            j = self.rand.randint(0, len(items) - 1)
            while i == j:
                j = self.rand.randint(0, len(items) - 1)
            self._swap(items, i, j)

    def _swap(self, items: List['Item'], index_a: int, index_b: int) -> None:
        """Exchange the position of two items at given positions."""
        items[index_a], items[index_b] = items[index_b], items[index_a]

    def _move(self, items: List['Item'], index_src: int, index_dst: int) -> None:
        """Move an item from one position to another."""
        src = items[index_src]
        if index_src < index_dst:
            items[index_src:index_dst] = items[index_src + 1:index_dst + 1]
            items[index_dst - 1] = src
        else:
            items[index_dst + 1:index_src + 1] = items[index_dst:index_src]
            items[index_dst] = src
