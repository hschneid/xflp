# xflp
xflp is a solver for truck loading problems in 3D with real world constraints

It support:
* single or multiple bin packing
* rotating of items for 1 axis
* Constraints:
  * Max height of loading space
  * Max weight of loading space
  * Max bearing weight of each item
* multiple stacking groups

Optimization:
* Contruction heuristic
* GRASP heuristic

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
