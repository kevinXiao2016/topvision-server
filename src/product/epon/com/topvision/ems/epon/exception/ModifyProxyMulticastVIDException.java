/***********************************************************************
 * $ ModifyProxyMulticastVIDException.java,v1.0 2011-11-24 17:41:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:41:52
 */
public class ModifyProxyMulticastVIDException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyProxyMulticastVIDException() {
    }

    public ModifyProxyMulticastVIDException(String s) {
        super(s);
    }

    public ModifyProxyMulticastVIDException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyProxyMulticastVIDException(Throwable throwable) {
        super(throwable);
    }
}
