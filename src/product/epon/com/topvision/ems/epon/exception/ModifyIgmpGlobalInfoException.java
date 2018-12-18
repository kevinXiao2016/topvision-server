/***********************************************************************
 * $ ModifyIgmpGlobalInfoException.java,v1.0 2011-11-24 17:26:55 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:26:55
 */
public class ModifyIgmpGlobalInfoException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpGlobalInfoException() {
    }

    public ModifyIgmpGlobalInfoException(String s) {
        super(s);
    }

    public ModifyIgmpGlobalInfoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpGlobalInfoException(Throwable throwable) {
        super(throwable);
    }
}
