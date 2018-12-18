/***********************************************************************
 * $ SavePerfStatsGlobalSetException.java,v1.0 2011-11-21 11:59:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-21-11:59:39
 */
public class SavePerfStatsGlobalSetException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public SavePerfStatsGlobalSetException() {
    }

    public SavePerfStatsGlobalSetException(String s) {
        super(s);
    }

    public SavePerfStatsGlobalSetException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SavePerfStatsGlobalSetException(Throwable throwable) {
        super(throwable);
    }
}
