/***********************************************************************
 * $Id: TopvisionException.java,v1.0 2011-7-11 下午02:33:26 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception;

/**
 * @author Victor
 * @created @2011-7-11-下午02:33:26
 * 
 */
public class TopvisionException extends RuntimeException {
    private static final long serialVersionUID = -3060781424658326179L;

    public TopvisionException() {
        super();
    }

    public TopvisionException(String message) {
        super(message);
    }

    public TopvisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TopvisionException(Throwable cause) {
        super(cause);
    }
}
