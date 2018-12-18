/***********************************************************************
 * $ RefreshQosDeviceBaseQosMapTableException.java,v1.0 2011-11-24 11:49:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-11:49:44
 */
public class RefreshQosDeviceBaseQosMapTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshQosDeviceBaseQosMapTableException() {
    }

    public RefreshQosDeviceBaseQosMapTableException(String s) {
        super(s);
    }

    public RefreshQosDeviceBaseQosMapTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshQosDeviceBaseQosMapTableException(Throwable throwable) {
        super(throwable);
    }
}
