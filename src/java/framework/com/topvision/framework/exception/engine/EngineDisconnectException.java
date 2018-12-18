/***********************************************************************
 * $Id: EngineDisconnectException.java,v1.0 2015年12月5日 下午2:19:59 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * @created @2015年12月5日-下午2:19:59
 *
 */
public class EngineDisconnectException extends EngineException {
    private static final long serialVersionUID = -3633479335711600418L;

    public EngineDisconnectException() {
        super();
    }

    public EngineDisconnectException(String message) {
        super(message);
    }

    public EngineDisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public EngineDisconnectException(Throwable cause) {
        super(cause);
    }
}
