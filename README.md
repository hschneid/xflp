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
