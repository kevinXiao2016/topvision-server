/***********************************************************************
 * $Id: PingException.java,v1.0 2011-4-1 上午10:47:44 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * 
 */
public class PingException extends EngineException {
    private static final long serialVersionUID = 4353929291016123625L;

    public PingException() {
        super();
    }

    public PingException(String message) {
        super(message);
    }

    public PingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PingException(Throwable cause) {
        super(cause);
    }
}
