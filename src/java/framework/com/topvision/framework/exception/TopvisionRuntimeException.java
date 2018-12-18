/***********************************************************************
 * $Id: TopvisionRuntimeException.java,v1.0 2011-3-31 下午04:56:27 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception;

/**
 * 所有自定义异常的根异常
 * 
 * @author Victor
 * 
 */
public abstract class TopvisionRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1145447953169319638L;

    public TopvisionRuntimeException() {
        super();
    }

    public TopvisionRuntimeException(String message) {
        super(message);
    }

    public TopvisionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TopvisionRuntimeException(Throwable cause) {
        super(cause);
    }
}
