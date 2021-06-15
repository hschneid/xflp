[![MIT License](https://img.shields.io/apm/l/atomic-design-ui.svg?)](https://github.com/tterb/atomic-design-ui/blob/master/LICENSEs)
[![BCH compliance](https://bettercodehub.com/edge/badge/hschneid/xflp?branch=master)](https://bettercodehub.com/)
![alt text](https://img.shields.io/static/v1?label=version&message=0.4.0&color=-)

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

Optimization:
* Contruction heuristic
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
### 0.4.0
- Add restriction, that one item must be fully placed on top of one item
  - Business motivation: Certain packages may have restriction due to shoulder or feet forms, that stacking on multiple items is not possible.
- Fixed copyright information
- Refactored import classes

### 0.3.2
- Added SpotBugs in build process to find smellies
- Fixed some smellies
  - In many cases a XFLPException is thrown instead of IllegalArgumentExceptions
### 0.3.1
- "Fast Fixed solver" uses new width proportion factor for choosing the next insert position
### 0.3.0
- Fix of stacking group feature
- Added a priorization criteria for choosing the next insert position. It takes the proportion of the item to the container into account.
- More reasonable structure of planning heuristics
### 0.2.0
- First stable release 