/***********************************************************************
 * $Id: TopvisionDataException.java,v1.0 2015-7-13 下午4:03:13 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author Administrator
 * @created @2015-7-13-下午4:03:13
 *
 */
public class TopvisionDataException extends ServiceException {

    /**
     * 
     */
    private static final long serialVersionUID = 3699254152829985849L;

    public TopvisionDataException() {
        super();
    }

    public TopvisionDataException(String message) {
        super(message);
    }

    public TopvisionDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public TopvisionDataException(Throwable cause) {
        super(cause);
    }

}
