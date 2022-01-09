package xf.xflp.opt.construction.multitype;

import xf.xflp.base.container.Container;
import xf.xflp.base.position.PositionCandidate;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public class ContainerPosition {

    private final Container container;
    private final PositionCandidate position;

    public ContainerPosition(Container container, PositionCandidate position) {
        this.container = container;
        this.position = position;
    }

    public Container getContainer() {
        return container;
    }

    public PositionCandidate getPosition() {
        return position;
    }
}
