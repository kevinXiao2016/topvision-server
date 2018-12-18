/***********************************************************************
 * $ RefreshIgmpMcOnuVlanTransTableException.java,v1.0 2011-11-24 13:38:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:38:54
 */
public class RefreshIgmpMcOnuVlanTransTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpMcOnuVlanTransTableException() {
    }

    public RefreshIgmpMcOnuVlanTransTableException(String s) {
        super(s);
    }

    public RefreshIgmpMcOnuVlanTransTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpMcOnuVlanTransTableException(Throwable throwable) {
        super(throwable);
    }
}
