/***********************************************************************
 * $Id: RmiException.java,v1.0 2011-4-11 下午02:18:18 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

/**
 * @author Victor
 * @created @2011-4-11-下午02:18:18
 * 
 */
public class RmiException extends ServiceException {
    private static final long serialVersionUID = 7726574032455665254L;

    public RmiException() {
        super();
    }

    public RmiException(String message) {
        super(message);
    }

    public RmiException(String message, Throwable cause) {
        super(message, cause);
    }

    public RmiException(Throwable cause) {
        super(cause);
    }
}
