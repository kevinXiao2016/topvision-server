/***********************************************************************
 * $Id: EngineThreadException.java,v1.0 2011-10-12 下午05:30:38 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * @created @2011-10-12-下午05:30:38
 * 
 */
public class EngineThreadException extends EngineException {
    private static final long serialVersionUID = 8713359114369750592L;

    public EngineThreadException() {
        super();
    }

    public EngineThreadException(String message) {
        super(message);
    }

    public EngineThreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineThreadException(Throwable cause) {
        super(cause);
    }
}
