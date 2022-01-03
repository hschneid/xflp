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
public class DirectContainerParameter implements ContainerParameter {

    private float lifoImportance;
    private GroundContactRule groundContactRule;

    @Override
    public void add(ParameterType type, Object value) {
        switch (type) {
            case LIFO_IMPORTANCE:
                lifoImportance = (Float)value;
                break;
            case GROUND_CONTACT_RULE:
                groundContactRule = (GroundContactRule) value;
                break;
        }
    }

    @Override
    public Object get(ParameterType type) {
        switch (type) {
            case LIFO_IMPORTANCE:
                return lifoImportance;
            case GROUND_CONTACT_RULE:
                return groundContactRule;
        }

        return null;
    }
}
