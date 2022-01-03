package xf.xflp.exception;

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
public class XFLPException extends RuntimeException {

    private final XFLPExceptionType type;

    public XFLPException(XFLPExceptionType type, String message) {
        super(message);
        this.type = type;
    }

    public XFLPException(XFLPExceptionType type, String message, Exception e) {
        super(message, e);
        this.type = type;
    }

    public XFLPExceptionType getType() {
        return type;
    }
}
