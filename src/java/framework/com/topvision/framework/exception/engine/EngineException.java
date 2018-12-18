/***********************************************************************
 * $Id: EngineException.java,v1.0 2011-3-31 下午04:57:40 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * 
 * @author Victor
 * 
 */
public abstract class EngineException extends TopvisionRuntimeException {
    private static final long serialVersionUID = 1115902182633163030L;

    public EngineException() {
        super();
    }

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineException(Throwable cause) {
        super(cause);
    }
}
