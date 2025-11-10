"""
XFLP - eXtended Flexible Load Planner

A solver for 3D truck loading problems with real-world constraints.

Copyright (c) 2012-2025 Holger Schneider
Licensed under the MIT License
"""

from .xflp_main import XFLP
from .base.model import XFLPModel
from .base.parameter import XFLPParameter
from .base.solution import XFLPSolution
from .report.lp_report import LPReport
from .opt.opt_type import XFLPOptType

__version__ = "0.7.0"
__all__ = ["XFLP", "XFLPModel", "XFLPParameter", "XFLPSolution", "LPReport", "XFLPOptType"]
