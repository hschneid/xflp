# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Abstract base class for optimization procedures.

Author: hschneid
"""

from abc import ABC, abstractmethod
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.base.xflp_model import XFLPModel


class XFLPBase(ABC):
    """Abstract base class for XFLP optimization methods."""

    @abstractmethod
    def execute(self, model: 'XFLPModel') -> None:
        """
        Execute the optimization on the given model.

        Args:
            model: Model contains items, container types, the resulting containers and rejected items

        Raises:
            XFLPException: If optimization fails
        """
        pass
