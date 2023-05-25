package xf.xflp.base.position;

import xf.xflp.base.item.Item;
import xf.xflp.base.item.Position;

/**
 * Copyright (c) 2012-2023 Holger Schneider
 * All rights reserved.
 * <p>
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 * <p>
 * This class means a candidate where and how an item can be placed into container.
 *
 * @author hschneid
 */
public record PositionCandidate (
        Position position,
        Item item,
        boolean isRotated
){

    public static PositionCandidate of(Position position, Item item, boolean isRotated) {
        return new PositionCandidate(position, item, isRotated);
    }
}
