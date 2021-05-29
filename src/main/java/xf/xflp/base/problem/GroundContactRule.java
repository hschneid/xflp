package xf.xflp.base.problem;

/**
 * Copyright (c) 2012-2021 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 *
 * @author hschneid
 *
 */
public enum GroundContactRule {

    // A part of an item can hang over a stack
    FREE,
    // An item must stand on the floor or other items for all 4 floor corners
    COVERED,
    // An item can stand on the ground or must cover multiple items
    MULTIPLE
}
