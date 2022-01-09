package xf.xflp.base.container;

/**
 * Copyright (c) 2012-2022 Holger Schneider
 * All rights reserved.
 *
 * This source code is licensed under the MIT License (MIT) found in the
 * LICENSE file in the root directory of this source tree.
 *
 * @author hschneid
 */
public interface ContainerParameter {

    void add(ParameterType type, Object value);

    Object get(ParameterType type);
}
