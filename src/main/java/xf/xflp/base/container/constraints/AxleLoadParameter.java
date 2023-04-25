package xf.xflp.base.container.constraints;

/**
 * Copyright (c) 2012-2023 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 *
 */
public record AxleLoadParameter(
        float firstPermissibleAxleLoad,
        float secondPermissibleAxleLoad,
        float axleDistance) {
}
