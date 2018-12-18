/***********************************************************************
 * $ RefreshIgmpMcOnuTableException.java,v1.0 2011-11-24 13:37:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:37:44
 */
public class RefreshIgmpMcOnuTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpMcOnuTableException() {
    }

    public RefreshIgmpMcOnuTableException(String s) {
        super(s);
    }

    public RefreshIgmpMcOnuTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpMcOnuTableException(Throwable throwable) {
        super(throwable);
    }
}
