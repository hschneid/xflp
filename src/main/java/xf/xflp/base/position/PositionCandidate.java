package xf.xflp.base.position;

import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * This class means a candidate where and how an item can be placed into container.
 *
 * @author hschneid
 */
public class PositionCandidate {

    public final Position position;
    public final Item item;
    public final boolean isRotated;

    private PositionCandidate(Position position, Item item, boolean isRotated) {
        this.position = position;
        this.item = item;
        this.isRotated = isRotated;
    }

    public static PositionCandidate of(Position position, Item item, boolean isRotated) {
        return new PositionCandidate(position, item, isRotated);
    }
}
