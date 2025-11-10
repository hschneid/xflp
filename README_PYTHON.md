[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![version](https://img.shields.io/static/v1?label=version&message=0.7.0&color=blue)
![Python](https://img.shields.io/badge/python-3.9+-blue.svg)

# XFLP - Python Version

**XFLP (eXtended Flexible Load Planner)** is a solver for 3D truck loading problems with real-world constraints.

This is a **Python port** of the original [Java implementation](https://github.com/hschneid/xflp).

---

## Features

### Core Capabilities
* **Single or multiple bin packing** - Pack items into one or many containers
* **Item rotation** - Rotate items 90° on one axis for better fit
* **Load/unload simulation** - Model loading and unloading sequences
* **Real-world constraints:**
  * Max height and weight per container
  * Max bearing weight per item
  * Last-in, first-out (LIFO) constraints
  * Stacking groups and compatibility
  * Container type restrictions
  * Immersive depth during stacking
  * Permissible axle loads (2 axles)
  * Ground contact rules

### Optimization Methods
* **Construction heuristics** - Fast greedy algorithms
* **GRASP (Greedy Randomized Adaptive Search)** - Advanced metaheuristic
  * Swap and relocate neighborhood search
  * Randomized item ordering

---

## Installation

### From Source

```bash
# Clone the repository
git clone https://github.com/hschneid/xflp.git
cd xflp

# Install in development mode
pip install -e .
```

### Requirements
- Python 3.9 or higher
- No external dependencies (pure Python)

---

## Quick Start

```python
from xflp import XFLP, XFLPOptType

# Create XFLP instance
xflp = XFLP()

# Add a container (truck)
xflp.add_container() \
    .set_length(13500) \
    .set_width(2500) \
    .set_height(3000) \
    .set_max_weight(25000)

# Add items to pack
xflp.add_item() \
    .set_extern_id("Packet 1") \
    .set_length(1000) \
    .set_width(800) \
    .set_height(600) \
    .set_weight(500)

xflp.add_item() \
    .set_extern_id("Packet 2") \
    .set_length(1200) \
    .set_width(1000) \
    .set_height(800) \
    .set_weight(600)

# Configure and execute
xflp.set_type_of_optimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)
xflp.execute_load_planning()

# Get results
report = xflp.get_report()
summary = report.get_summary()

print(f"Containers used: {summary.get_nbr_of_used_vehicles()}")
print(f"Unloaded items: {summary.get_nbr_of_not_loaded_packages()}")
print(f"Utilization: {summary.get_utilization():.2%}")
```

---

## Advanced Usage

### Item Configuration

```python
xflp.add_item() \
    .set_extern_id("Box-123") \
    .set_length(1000) \
    .set_width(800) \
    .set_height(600) \
    .set_weight(500) \
    .set_spinable(True) \
    .set_stackable(True) \
    .set_stacking_weight_limit(1000) \
    .set_allowed_container_set({1, 2}) \
    .set_loading_location("Warehouse A") \
    .set_unloading_location("Store B")
```

### Container Configuration

```python
xflp.add_container() \
    .set_container_type(1) \
    .set_length(13500) \
    .set_width(2500) \
    .set_height(3000) \
    .set_max_weight(25000) \
    .set_axle_load_parameter(
        max_forward_axle_load=8000,
        max_rear_axle_load=11500,
        forward_axle_pos=2000,
        rear_axle_pos=8500
    )
```

### Optimization Parameters

```python
from xflp.opt.construction.strategy.strategy import Strategy
from xflp.base.container.enums import GroundContactRule

# LIFO constraints (for delivery routes)
xflp.get_parameter().set_lifo_importance(1.0)

# Packing strategy
xflp.get_parameter().set_preferred_packing_strategy(Strategy.TOUCHING_PERIMETER)

# Stacking limits
xflp.get_parameter().set_nbr_of_allowed_stacked_items(3)

# Ground contact requirements
xflp.get_parameter().set_ground_contact_rule(GroundContactRule.MUST)

# Max items to pack
xflp.get_parameter().set_max_nbr_of_items(50)

# Max containers to use
xflp.get_parameter().set_max_nbr_of_container(5)
```

### Optimization Types

```python
from xflp import XFLPOptType

# Fast construction heuristic for fixed containers
xflp.set_type_of_optimization(XFLPOptType.FAST_FIXED_CONTAINER_PACKER)

# Best solution with GRASP for fixed containers
xflp.set_type_of_optimization(XFLPOptType.BEST_FIXED_CONTAINER_PACKER)

# Minimize number of containers (fast)
xflp.set_type_of_optimization(XFLPOptType.FAST_MIN_CONTAINER_PACKER)

# Minimize number of containers (best)
xflp.set_type_of_optimization(XFLPOptType.BEST_MIN_CONTAINER_PACKER)
```

### Status Monitoring

```python
from xflp.base.monitor.status_monitor import StatusMonitor
from xflp.base.monitor.status_code import StatusCode

class MyMonitor(StatusMonitor):
    def update(self, code: StatusCode, message: str):
        print(f"[{code.name}] {message}")

xflp.set_status_monitor(MyMonitor())
```

### Detailed Results

```python
report = xflp.get_report()

# Iterate over containers
for container_report in report:
    container = container_report.get_container()
    summary = container_report.get_summary()

    print(f"Container: {container_report.get_container_type_name()}")
    print(f"  Items loaded: {summary.get_nbr_of_loaded_packages()}")
    print(f"  Utilization: {summary.get_utilization():.2%}")

    # Get each item's position
    for event in container_report.get_package_events():
        print(f"  - {event.extern_id}: ({event.x}, {event.y}, {event.z})")

# Check for unplanned items
if xflp.has_unplanned_items():
    unplanned = report.get_unplanned_packages()
    print(f"Unplanned: {len(unplanned)} items")
```

---

## Examples

Run the included example:

```bash
python examples/simple_example.py
```

---

## Architecture

The Python version maintains the same architecture as the Java implementation:

```
xflp/
├── base/
│   ├── item/           # Item data models
│   ├── container/      # Container implementations
│   ├── fleximport/     # Data import (builder pattern)
│   ├── monitor/        # Status monitoring
│   ├── position/       # Position finding services
│   └── space/          # Space management
├── opt/
│   ├── construction/   # Construction heuristics
│   │   ├── strategy/   # Placement strategies
│   │   ├── onetype/    # Single container type packers
│   │   └── multitype/  # Multiple container type packers
│   └── grasp/          # GRASP metaheuristic
├── report/             # Result reporting
├── exception/          # Custom exceptions
└── util/               # Utility collections
```

---

## Key Differences from Java Version

### Pythonic Improvements
- **Type hints** throughout for better IDE support
- **Snake_case** naming convention (vs camelCase)
- **Dataclasses** for immutable records
- **Protocols** for interfaces (structural typing)
- **Properties** and descriptors where appropriate
- **Context managers** could be added for resource management

### API Compatibility
The API is nearly identical to the Java version with naming adjustments:
- `executeLoadPlanning()` → `execute_load_planning()`
- `addItem()` → `add_item()`
- `getReport()` → `get_report()`

---

## Testing

```bash
# Install dev dependencies
pip install -e ".[dev]"

# Run tests
pytest tests/

# Run with coverage
pytest --cov=xflp tests/
```

---

## Performance

The Python version maintains the same algorithmic complexity as Java:
- **Construction heuristics:** Fast, suitable for large problems
- **GRASP optimization:** More thorough but slower
- **Space-based positioning:** O(1) insert position checking
- **Indexed collections:** Efficient slot reuse

For performance-critical applications, consider:
- Using PyPy for JIT compilation
- Profiling with cProfile
- Optional Cython compilation for hot paths

---

## License

This software is released under the [MIT License](https://opensource.org/licenses/MIT).

Copyright (c) 2012-2025 Holger Schneider

---

## Change Log

### 0.7.0 (Python Port)
- Complete conversion from Java to Python
- Maintained all features and algorithms
- Added comprehensive type hints
- Pythonic naming conventions
- Full compatibility with Java API design

For the Java version changelog, see the main [README.md](README.md).

---

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

---

## Support

- **Issues:** [GitHub Issues](https://github.com/hschneid/xflp/issues)
- **Original Java Version:** [github.com/hschneid/xflp](https://github.com/hschneid/xflp)

---

## Citation

If you use XFLP in your research, please cite:

```
Schneider, H. (2025). XFLP: eXtended Flexible Load Planner.
https://github.com/hschneid/xflp
```
