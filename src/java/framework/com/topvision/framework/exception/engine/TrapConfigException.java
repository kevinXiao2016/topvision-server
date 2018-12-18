/***********************************************************************
 * $Id: TrapConfigException.java,v1.0 2011-3-31 下午05:13:45 Victor Exp $
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
public class TrapConfigException extends EngineException {
    private static final long serialVersionUID = -3729483808789384321L;

    public TrapConfigException() {
        super();
    }

    public TrapConfigException(String message) {
        super(message);
    }

    public TrapConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrapConfigException(Throwable cause) {
        super(cause);
    }
}
