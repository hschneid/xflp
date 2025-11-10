#!/usr/bin/env python3
"""
Simple example demonstrating the XFLP API.

This example shows how to:
1. Create containers
2. Add items
3. Configure optimization
4. Run planning
5. Get results
"""

import sys
from pathlib import Path

# Add src to path for imports
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from xflp import XFLP, XFLPOptType


def main():
    """Run a simple container loading example."""
    print("=" * 60)
    print("XFLP - Simple Container Loading Example")
    print("=" * 60)

    # Create XFLP instance
    xflp = XFLP()

    # Add a container (truck)
    print("\n1. Adding container...")
    xflp.add_container() \
        .set_length(13500) \
        .set_width(2500) \
        .set_height(3000) \
        .set_max_weight(25000)
    print("   Container: 13500 x 2500 x 3000 mm, max weight: 25000 kg")

    # Add items to pack
    print("\n2. Adding items...")
    items = [
        ("Packet 1", 1000, 800, 600, 500),
        ("Packet 2", 1200, 1000, 800, 600),
        ("Packet 3", 900, 900, 700, 450),
        ("Packet 4", 1100, 950, 650, 520),
        ("Packet 5", 850, 850, 550, 400),
    ]

    for name, length, width, height, weight in items:
        xflp.add_item() \
            .set_extern_id(name) \
            .set_length(length) \
            .set_width(width) \
            .set_height(height) \
            .set_weight(weight)
        print(f"   {name}: {length} x {width} x {height} mm, {weight} kg")

    # Set optimization type
    print("\n3. Configuring optimization...")
    xflp.set_type_of_optimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
    print("   Using: FAST_FIXED_CONTAINER_PACKER")

    # Execute planning
    print("\n4. Executing load planning...")
    try:
        xflp.execute_load_planning()
        print("   ✓ Planning completed successfully")
    except Exception as e:
        print(f"   ✗ Planning failed: {e}")
        return 1

    # Get results
    print("\n5. Results:")
    report = xflp.get_report()

    if report:
        summary = report.get_summary()
        print(f"   Containers used: {summary.get_nbr_of_used_vehicles()}")
        print(f"   Items loaded: {len(items) - summary.get_nbr_of_not_loaded_packages()}")
        print(f"   Items not loaded: {summary.get_nbr_of_not_loaded_packages()}")
        print(f"   Utilization: {summary.get_utilization():.2%}")

        if xflp.has_unplanned_items():
            print("\n   ⚠ Warning: Some items could not be loaded")
            unplanned = report.get_unplanned_packages()
            for item in unplanned:
                print(f"      - {item.get_external_index()}")
        else:
            print("\n   ✓ All items successfully loaded!")

        # Show detailed container information
        print("\n6. Container Details:")
        for container_report in report:
            container = container_report.get_container()
            c_summary = container_report.get_summary()
            print(f"\n   Container: {container_report.get_container_type_name()}")
            print(f"   - Dimensions: {container.get_width()} x {container.get_length()} x {container.get_height()} mm")
            print(f"   - Items loaded: {c_summary.get_nbr_of_loaded_packages()}")
            print(f"   - Volume used: {c_summary.get_max_used_volume()} / {c_summary.get_max_volume()}")
            print(f"   - Weight used: {c_summary.get_max_used_weight():.0f} / {container.get_max_weight()} kg")
            print(f"   - Utilization: {c_summary.get_utilization():.2%}")

            print(f"\n   Loaded items:")
            for event in container_report.get_package_events():
                print(f"      - {event.extern_id}: position ({event.x}, {event.y}, {event.z})")

    print("\n" + "=" * 60)
    print("Example completed!")
    print("=" * 60)
    return 0


if __name__ == "__main__":
    sys.exit(main())
