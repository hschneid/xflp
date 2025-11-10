# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
Strategy enumeration for packing algorithms.

Author: hschneid
"""

from enum import Enum
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from xflp.opt.construction.strategy.base_strategy import BaseStrategy


class Strategy(Enum):
    """Enumeration of available packing strategies."""

    TOUCHING_PERIMETER = "touching_perimeter"
    HIGH_LOW_LEFT = "high_low_left"
    SAME_BASE = "same_base"
    WIDTH_PROPORTION = "width_proportion"

    def get_strategy(self) -> 'BaseStrategy':
        """
        Get an instance of the strategy.

        Returns:
            BaseStrategy instance
        """
        if self == Strategy.TOUCHING_PERIMETER:
            from xflp.opt.construction.strategy.touching_perimeter import TouchingPerimeter
            return TouchingPerimeter()
        elif self == Strategy.HIGH_LOW_LEFT:
            from xflp.opt.construction.strategy.highest_lower_left import HighestLowerLeft
            return HighestLowerLeft()
        elif self == Strategy.SAME_BASE:
            from xflp.opt.construction.strategy.same_base_strategy import SameBaseStrategy
            return SameBaseStrategy()
        elif self == Strategy.WIDTH_PROPORTION:
            from xflp.opt.construction.strategy.width_proportion_factor import WidthProportionFactor
            return WidthProportionFactor()
        else:
            raise ValueError(f"Unknown strategy: {self}")
