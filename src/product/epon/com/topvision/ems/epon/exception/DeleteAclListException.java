/***********************************************************************
 * $ DeleteAclListException.java,v1.0 2011-11-24 9:24:22 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:24:22
 */
public class DeleteAclListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public DeleteAclListException() {
    }

    public DeleteAclListException(String s) {
        super(s);
    }

    public DeleteAclListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteAclListException(Throwable throwable) {
        super(throwable);
    }
}
