/***********************************************************************
 * $ ModifyAclListException.java,v1.0 2011-11-24 9:24:05 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:24:05
 */
public class ModifyAclListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyAclListException() {
    }

    public ModifyAclListException(String s) {
        super(s);
    }

    public ModifyAclListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyAclListException(Throwable throwable) {
        super(throwable);
    }
}
