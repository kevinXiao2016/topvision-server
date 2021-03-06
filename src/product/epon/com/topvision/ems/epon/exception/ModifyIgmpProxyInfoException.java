/***********************************************************************
 * $ ModifyIgmpProxyInfoException.java,v1.0 2011-11-24 17:29:41 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:29:41
 */
public class ModifyIgmpProxyInfoException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpProxyInfoException() {
    }

    public ModifyIgmpProxyInfoException(String s) {
        super(s);
    }

    public ModifyIgmpProxyInfoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpProxyInfoException(Throwable throwable) {
        super(throwable);
    }
}
