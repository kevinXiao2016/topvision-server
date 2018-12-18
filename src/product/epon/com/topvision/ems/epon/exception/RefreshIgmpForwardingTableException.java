/***********************************************************************
 * $ RefreshIgmpForwardingTableException.java,v1.0 2011-11-24 13:36:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:36:48
 */
public class RefreshIgmpForwardingTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpForwardingTableException() {
    }

    public RefreshIgmpForwardingTableException(String s) {
        super(s);
    }

    public RefreshIgmpForwardingTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpForwardingTableException(Throwable throwable) {
        super(throwable);
    }
}
