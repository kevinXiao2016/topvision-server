/***********************************************************************
 * $Id: TrapInstanceException.java,v1.0 2017-11-15 下午02:33:26 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.exception;

/**
 * @author fanzidong
 * @created @2017-11-15-下午02:33:26
 * 
 */
public class TrapInstanceException extends RuntimeException {
    private static final long serialVersionUID = -3060781424658326179L;

    public TrapInstanceException() {
        super();
    }

    public TrapInstanceException(String message) {
        super(message);
    }

    public TrapInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrapInstanceException(Throwable cause) {
        super(cause);
    }
}
