/***********************************************************************
 * $ NoSushScheduleException.java,v1.0 2012-5-2 17:33:06 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.engine;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2012-5-2-17:33:06
 */
public class NoSuchScheduleException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public NoSuchScheduleException() {
    }

    public NoSuchScheduleException(Throwable cause) {
        super(cause);
    }

    public NoSuchScheduleException(String message) {
        super(message);
    }

    public NoSuchScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
