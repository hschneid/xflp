"""
Copyright (c) 2012-2025 Holger Schneider
All rights reserved.

This source code is licensed under the MIT License (MIT) found in the
LICENSE file in the root directory of this source tree.


@author hschneid
"""

from xflp import XFLP


def main() -> None:
    """
    Main entry point for the XFLP application.

    Executes the load planning process.
    """
    try:
        XFLP().execute_load_planning()
    except Exception as e:
        print(f"Error during load planning execution: {e}")
        raise


if __name__ == "__main__":
    main()
