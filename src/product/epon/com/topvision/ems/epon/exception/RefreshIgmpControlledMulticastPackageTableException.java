/***********************************************************************
 * $ RefreshIgmpControlledMulticastPackageTableException.java,v1.0 2011-11-24 13:25:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:25:33
 */
public class RefreshIgmpControlledMulticastPackageTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpControlledMulticastPackageTableException() {
    }

    public RefreshIgmpControlledMulticastPackageTableException(String s) {
        super(s);
    }

    public RefreshIgmpControlledMulticastPackageTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpControlledMulticastPackageTableException(Throwable throwable) {
        super(throwable);
    }
}
