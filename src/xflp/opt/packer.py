# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Construction heuristics are packers, which create a solution out of a model.

In some cases a planning strategy can be given, which influences the way of construction.

Author: hschneid
"""

from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel


class Packer(ABC):
    """Interface for packing heuristics."""

    @abstractmethod
    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the packer on the given model.

        Args:
            model: Model contains items, container types, the resulting containers and rejected items

        Raises:
            XFLPException: If packing fails
        """
        pass
