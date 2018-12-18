/***********************************************************************
 * $ AddAclListException.java,v1.0 2011-11-24 9:12:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:12:33
 */
public class AddAclListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddAclListException() {
    }

    public AddAclListException(String s) {
        super(s);
    }

    public AddAclListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddAclListException(Throwable throwable) {
        super(throwable);
    }
}
