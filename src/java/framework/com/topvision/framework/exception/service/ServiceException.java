/***********************************************************************
 * $Id: ServiceException.java,v1.0 2011-3-31 下午05:21:50 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * @author Victor
 * 
 */
public abstract class ServiceException extends TopvisionRuntimeException {
    private static final long serialVersionUID = -6080444667108469973L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
