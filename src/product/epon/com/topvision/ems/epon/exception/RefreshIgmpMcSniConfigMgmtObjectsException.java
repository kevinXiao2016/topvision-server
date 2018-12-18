/***********************************************************************
 * $ RefreshIgmpMcSniConfigMgmtObjectsException.java,v1.0 2011-11-24 13:40:49 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:40:49
 */
public class RefreshIgmpMcSniConfigMgmtObjectsException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpMcSniConfigMgmtObjectsException() {
    }

    public RefreshIgmpMcSniConfigMgmtObjectsException(String s) {
        super(s);
    }

    public RefreshIgmpMcSniConfigMgmtObjectsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpMcSniConfigMgmtObjectsException(Throwable throwable) {
        super(throwable);
    }
}
