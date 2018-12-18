/***********************************************************************
 * $Id: UnknownTargetException.java,v1.0 2011-3-31 下午05:11:46 Victor Exp $
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
public class UnknownTargetException extends EngineException {
    private static final long serialVersionUID = -7980133971251277557L;

    public UnknownTargetException() {
        super();
    }

    public UnknownTargetException(String message) {
        super(message);
    }

    public UnknownTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownTargetException(Throwable cause) {
        super(cause);
    }
}
