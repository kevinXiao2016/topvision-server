/***********************************************************************
 * $ ModifyOnuQosPolicyException.java,v1.0 2011-11-24 16:00:24 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-16:00:24
 */
public class ModifyOnuQosPolicyException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyOnuQosPolicyException() {
    }

    public ModifyOnuQosPolicyException(String s) {
        super(s);
    }

    public ModifyOnuQosPolicyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyOnuQosPolicyException(Throwable throwable) {
        super(throwable);
    }
}
