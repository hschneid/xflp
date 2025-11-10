# Copyright (c) 2012-2025 Holger Schneider
# All rights reserved.
#
# This source code is licensed under the MIT License (MIT) found in the
# LICENSE file in the root directory of this source tree.

"""
XFLP Packing Strategies Module

This module contains different strategies for selecting positions during packing.
"""

from xflp.opt.construction.strategy.strategy import Strategy
from xflp.opt.construction.strategy.base_strategy import BaseStrategy
from xflp.opt.construction.strategy.highest_lower_left import HighestLowerLeft
from xflp.opt.construction.strategy.touching_perimeter import TouchingPerimeter
from xflp.opt.construction.strategy.width_proportion_factor import WidthProportionFactor
from xflp.opt.construction.strategy.same_base_strategy import SameBaseStrategy

__all__ = [
    'Strategy',
    'BaseStrategy',
    'HighestLowerLeft',
    'TouchingPerimeter',
    'WidthProportionFactor',
    'SameBaseStrategy',
]
