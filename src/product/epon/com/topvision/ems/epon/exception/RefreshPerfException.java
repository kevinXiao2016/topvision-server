/***********************************************************************
 * $ RefreshPerfException.java,v1.0 2011-11-21 16:38:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-21-16:38:48
 */
public class RefreshPerfException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshPerfException() {
    }

    public RefreshPerfException(String s) {
        super(s);
    }

    public RefreshPerfException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshPerfException(Throwable throwable) {
        super(throwable);
    }
}
