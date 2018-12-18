/***********************************************************************
 * $Id: ServiceNotFoundException.java,v1.0 2011-3-31 下午05:22:29 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

/**
 * @author Victor
 * 
 */
public class ServiceNotFoundException extends ServiceException {
    private static final long serialVersionUID = 1147600923320842677L;

    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }
}
