package xf.xflp.base.container;

import util.collection.LPListMap;

import java.util.Map;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public interface ContainerBaseData {

    LPListMap<Integer, Integer> getXMap();
    LPListMap<Integer, Integer> getYMap();
    LPListMap<Integer, Integer> getZMap();

    ZItemGraph getZGraph();

    Map<Integer, Float> getBearingCapacities();
}
