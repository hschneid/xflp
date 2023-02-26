package xf.xflp.base.container;

import xf.xflp.base.item.Position;
import xf.xflp.base.item.Space;

import java.util.List;

/**
 * Copyright (c) 2012-2023 Holger Schneider
 * All rights reserved.
 * <p>
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public interface SpaceContainer extends Container {

    List<Space> getSpace(Position pos);
}
