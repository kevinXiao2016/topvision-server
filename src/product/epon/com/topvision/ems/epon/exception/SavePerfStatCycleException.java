/***********************************************************************
 * $ SavePerfStatCycleException.java,v1.0 2011-11-21 11:57:49 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-21-11:57:49
 */
public class SavePerfStatCycleException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public SavePerfStatCycleException() {
    }

    public SavePerfStatCycleException(String s) {
        super(s);
    }

    public SavePerfStatCycleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SavePerfStatCycleException(Throwable throwable) {
        super(throwable);
    }
}
