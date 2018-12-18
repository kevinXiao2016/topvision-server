/***********************************************************************
 * $ RefreshSlaTableException.java,v1.0 2011-11-24 13:20:08 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:20:08
 */
public class RefreshSlaTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshSlaTableException() {
    }

    public RefreshSlaTableException(String s) {
        super(s);
    }

    public RefreshSlaTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshSlaTableException(Throwable throwable) {
        super(throwable);
    }
}
