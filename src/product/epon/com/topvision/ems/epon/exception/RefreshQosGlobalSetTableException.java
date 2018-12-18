/***********************************************************************
 * $ RefreshQosGlobalSetTableException.java,v1.0 2011-11-24 11:57:55 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-11:57:55
 */
public class RefreshQosGlobalSetTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshQosGlobalSetTableException() {
    }

    public RefreshQosGlobalSetTableException(String s) {
        super(s);
    }

    public RefreshQosGlobalSetTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshQosGlobalSetTableException(Throwable throwable) {
        super(throwable);
    }
}
