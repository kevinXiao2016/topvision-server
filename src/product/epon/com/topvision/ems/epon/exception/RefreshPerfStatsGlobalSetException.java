/***********************************************************************
 * $ RefreshPerfStatsGlobalSetException.java,v1.0 2011-11-21 16:59:02 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-21-16:59:02
 */
public class RefreshPerfStatsGlobalSetException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshPerfStatsGlobalSetException() {
    }

    public RefreshPerfStatsGlobalSetException(String s) {
        super(s);
    }

    public RefreshPerfStatsGlobalSetException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshPerfStatsGlobalSetException(Throwable throwable) {
        super(throwable);
    }
}
