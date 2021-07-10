package xf.xflp.opt.construction.multitype;

import xf.xflp.base.container.ComplexContainer;
import xf.xflp.base.problem.Position;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public class ContainerPosition {

    private final ComplexContainer container;
    private final Position position;

    public ContainerPosition(ComplexContainer container, Position position) {
        this.container = container;
        this.position = position;
    }

    public ComplexContainer getContainer() {
        return container;
    }

    public Position getPosition() {
        return position;
    }
}
