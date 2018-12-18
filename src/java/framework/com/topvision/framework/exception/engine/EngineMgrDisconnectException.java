/***********************************************************************
 * $Id: EngineMgrDisconnectException.java,v1.0 2016-6-29 下午4:07:45 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Rod John
 * @created @2016-6-29-下午4:07:45
 *
 */
public class EngineMgrDisconnectException extends EngineException {
    private static final long serialVersionUID = -8397255821682831399L;

    public EngineMgrDisconnectException() {
        super();
    }

    public EngineMgrDisconnectException(String message) {
        super(message);
    }

    public EngineMgrDisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineMgrDisconnectException(Throwable cause) {
        super(cause);
    }
}
