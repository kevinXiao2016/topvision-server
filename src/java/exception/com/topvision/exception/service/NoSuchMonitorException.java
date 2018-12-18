/***********************************************************************
 * $ NoSushScheduleException.java,v1.0 2012-5-2 17:33:06 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2012-5-2-17:33:06
 */
public class NoSuchMonitorException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public NoSuchMonitorException() {
    }

    public NoSuchMonitorException(Throwable cause) {
        super(cause);
    }

    public NoSuchMonitorException(String message) {
        super(message);
    }

    public NoSuchMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}