/***********************************************************************
 * $ ModifyMulticastUserAuthorityListException.java,v1.0 2011-11-27 12:42:48 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-27-12:42:48
 */
public class ModifyMulticastUserAuthorityListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyMulticastUserAuthorityListException() {
    }

    public ModifyMulticastUserAuthorityListException(String s) {
        super(s);
    }

    public ModifyMulticastUserAuthorityListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyMulticastUserAuthorityListException(Throwable throwable) {
        super(throwable);
    }
}
