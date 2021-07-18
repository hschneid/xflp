package xf.xflp.base.container;

import util.collection.LPListMap;

public interface ContainerBaseData {

    LPListMap<Integer, Integer> getXMap();
    LPListMap<Integer, Integer> getYMap();
    LPListMap<Integer, Integer> getZMap();

    ZItemGraph getZGraph();
}
