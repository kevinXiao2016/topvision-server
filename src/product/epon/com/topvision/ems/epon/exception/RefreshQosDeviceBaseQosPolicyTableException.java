/***********************************************************************
 * $ RefreshQosDeviceBaseQosPolicyTableException.java,v1.0 2011-11-24 11:51:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-11:51:33
 */
public class RefreshQosDeviceBaseQosPolicyTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshQosDeviceBaseQosPolicyTableException() {
    }

    public RefreshQosDeviceBaseQosPolicyTableException(String s) {
        super(s);
    }

    public RefreshQosDeviceBaseQosPolicyTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshQosDeviceBaseQosPolicyTableException(Throwable throwable) {
        super(throwable);
    }
}
