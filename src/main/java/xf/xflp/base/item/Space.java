package xf.xflp.base.item;

/**
 * Copyright (c) 2012-2025 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public record Space(int l, int w, int h) {

    public static Space of(int l, int w, int h) {
        return new Space(l, w, h);
    }

    @Override
    public String toString() {
        return "(w:" + w +
                " l:" + l +
                " h:" + h + ")";
    }
}
