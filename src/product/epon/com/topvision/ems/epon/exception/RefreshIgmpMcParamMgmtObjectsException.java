/***********************************************************************
 * $ RefreshIgmpMcParamMgmtObjectsException.java,v1.0 2011-11-24 13:39:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:39:57
 */
public class RefreshIgmpMcParamMgmtObjectsException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpMcParamMgmtObjectsException() {
    }

    public RefreshIgmpMcParamMgmtObjectsException(String s) {
        super(s);
    }

    public RefreshIgmpMcParamMgmtObjectsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpMcParamMgmtObjectsException(Throwable throwable) {
        super(throwable);
    }
}
