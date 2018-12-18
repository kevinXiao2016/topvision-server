/***********************************************************************
 * $Id: FormatException.java,v1.0 2011-6-29 下午07:10:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception;

/**
 * @author Victor
 * @created @2011-6-29-下午07:10:41
 * 
 */
public class FormatException extends TopvisionRuntimeException {
    private static final long serialVersionUID = 4078952339962864220L;

    public FormatException() {
        super();
    }

    public FormatException(String message) {
        super(message);
    }

    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatException(Throwable cause) {
        super(cause);
    }
}
