/***********************************************************************
 * $ AddIgmpProxyException.java,v1.0 2011-11-24 17:09:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:09:48
 */
public class AddIgmpProxyException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddIgmpProxyException() {
    }

    public AddIgmpProxyException(String s) {
        super(s);
    }

    public AddIgmpProxyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddIgmpProxyException(Throwable throwable) {
        super(throwable);
    }
}
