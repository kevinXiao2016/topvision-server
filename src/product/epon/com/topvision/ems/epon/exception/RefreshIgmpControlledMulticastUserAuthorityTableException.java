/***********************************************************************
 * $ RefreshIgmpControlledMulticastUserAuthorityTableException.java,v1.0 2011-11-24 13:27:41 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-13:27:41
 */
public class RefreshIgmpControlledMulticastUserAuthorityTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshIgmpControlledMulticastUserAuthorityTableException() {
    }

    public RefreshIgmpControlledMulticastUserAuthorityTableException(String s) {
        super(s);
    }

    public RefreshIgmpControlledMulticastUserAuthorityTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshIgmpControlledMulticastUserAuthorityTableException(Throwable throwable) {
        super(throwable);
    }
}
