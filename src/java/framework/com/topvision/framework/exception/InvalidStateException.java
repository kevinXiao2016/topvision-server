/*
 * State.java
 *
 * Created on 2007年10月11日, 下午2:58
 */
package com.topvision.framework.exception;

/**
 * @author niejun
 */
public class InvalidStateException extends TopvisionRuntimeException {
    private static final long serialVersionUID = 2695345867943532186L;

    public InvalidStateException(String msg) {
        super(msg);
    }
}
