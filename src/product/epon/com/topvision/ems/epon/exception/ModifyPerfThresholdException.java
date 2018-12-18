/***********************************************************************
 * $ ModifyPerfThresholdException.java,v1.0 2011-11-21 10:25:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-21-10:25:47
 */
public class ModifyPerfThresholdException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyPerfThresholdException() {
    }

    public ModifyPerfThresholdException(String s) {
        super(s);
    }

    public ModifyPerfThresholdException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyPerfThresholdException(Throwable throwable) {
        super(throwable);
    }
}
