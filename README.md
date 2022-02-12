[![MIT License](https://img.shields.io/apm/l/atomic-design-ui.svg?)](https://github.com/tterb/atomic-design-ui/blob/master/LICENSEs)
[![BCH compliance](https://bettercodehub.com/edge/badge/hschneid/xflp?branch=master)](https://bettercodehub.com/)
![alt text](https://img.shields.io/static/v1?label=version&message=0.5.2&color=-)

# xflp
xflp is a solver for truck loading problems in 3D with real world constraints

It supports:
* single or multiple bin packing
* rotating of items for 1 axis
* simulation of loading and unloading of items  
* Constraints:
  * Max height of loading space
  * Max weight of loading space
  * Max bearing weight of each item
  * last in, first out condition
* consideration of stacking groups
* consideration of container types
* consideration of immersive depth during stacking

Optimization:
* Construction heuristic
* GRASP heuristic
  * Swap and relocate neighborhood search

# Usage
```
XFLP xflp = new XFLP();
xflp.addContainer().setLength(13500).setWidth(2500).setHeight(3000).setMaxWeight(25000);
xflp.addItem().setExternID("Packet 1").setLength(13500).setWidth(2500).setHeight(3000).setWeight(25000);
xflp.addItem().setExternID("Packet 2").setLength(13500).setWidth(2500).setHeight(3000).setWeight(25000);
xflp.addItem().setExternID("Packet 3").setLength(13500).setWidth(2500).setHeight(3000).setWeight(25000);

xflp.executeLoadPlanning();

LPReport report = xflp.getReport();
int nbrOfUnloadedPackages = report.getSummary().getNbrOfUnLoadedPackages();
```

## License
This software is released under [MIT License] (https://opensource.org/licenses/MIT)

## Change log
### 0.5.2 - Performance upgrade
- Improved bearing check by storing bearing capacities during container-adding.
  - For big problems the runtime improves by 43%.
- Refactored and removed old code

### 0.5.1 - Performance upgrade
- Add spaces for faster checking of possible insert positions
  - Each position defines a list of spaces, where items might fit into.
  - Adding new item means complex calculation/correction of spaces
  - Check, whether item fits at a certain position is reduced from O(n) to O(1)
    - For big problems the runtime improves by factor 3.
- Refactorings of optimization types 
- Minor refactoring of ground contact checking
- Internal refactorings

### 0.5.0 - Performance upgrade
- Refactoring of container code
  - Split algorithmic functions to service classes
  - Add general definition of containers
  - Have at least 2 implementations of container:
    - Items can be added and removed (current implementation)
    - Items can only be added. Implementation can be way simpler and performance is better.
- Refactoring of current container service methods, which improves performance
- Added check for duplicate positions, which maybe bring a bit of performance
- Import function checks, which type of container is necessary.
- After import, some values of items are checked for validity.


### 0.4.1
- Add immersive depth

### 0.4.0 - More restrictions
- Add restriction, that one item must be fully placed on top of one item
  - Business motivation: Certain packages may have restriction due to shoulder or feet forms, that stacking on multiple items is not possible.
- Fixed copyright information
- Refactored import classes
- Use specific exception instead of runtime exceptions

### 0.3.2
- Added SpotBugs in build process to find smellies
- Fixed some smellies
  - In many cases a XFLPException is thrown instead of IllegalArgumentExceptions
### 0.3.1
- "Fast Fixed solver" uses new width proportion factor for choosing the next insert position
### 0.3.0 - Stacking groups
- Fix of stacking group feature
- Added a priorization criteria for choosing the next insert position. It takes the proportion of the item to the container into account.
- More reasonable structure of planning heuristics
### 0.2.0
- First stable release 