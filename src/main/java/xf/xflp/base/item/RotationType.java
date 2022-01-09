package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public enum RotationType {

    FIX(0),
    SPINNABLE(1);

    public int getRotationType() {
        return rotationType;
    }

    private int rotationType;

    RotationType(int rotationType) {
        this.rotationType = rotationType;
    }
}
