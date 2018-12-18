/***********************************************************************
 * $ RefreshIgmpProxyParaTableException.java,v1.0 2011-11-24 13:42:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:42:39
 */
public class RefreshIgmpProxyParaTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpProxyParaTableException() {
    }

    public RefreshIgmpProxyParaTableException(String s) {
        super(s);
    }

    public RefreshIgmpProxyParaTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpProxyParaTableException(Throwable throwable) {
        super(throwable);
    }
}
