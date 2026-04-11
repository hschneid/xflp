package xf.xflp.base.container;

import util.collection.LPListMap;
import xf.xflp.base.item.Position;

import java.util.Map;

/**
 * Copyright (c) 2012-2026 Holger Schneider
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

    /**
     * Returns the precomputed minimum immersive depth for each active position.
     * Key is the Position, value is the minimum immersive depth of all items
     * whose top face is at pos.z() and whose footprint contains the point (pos.x(), pos.y()).
     */
    int getImmersiveDepthAtPosition(Position position);

    float getCenterOfGravityForY();

    /** Returns the maximum yl (y + length) of all items currently in the container. */
    int getMaxYl();
}
