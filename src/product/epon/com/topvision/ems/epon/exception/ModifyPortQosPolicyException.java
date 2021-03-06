/***********************************************************************
 * $ ModifyPortQosPolicyException.java,v1.0 2011-11-24 16:26:06 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-16:26:06
 */
public class ModifyPortQosPolicyException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyPortQosPolicyException() {
    }

    public ModifyPortQosPolicyException(String s) {
        super(s);
    }

    public ModifyPortQosPolicyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyPortQosPolicyException(Throwable throwable) {
        super(throwable);
    }
}
