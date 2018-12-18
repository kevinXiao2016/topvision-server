/***********************************************************************
 * $ DeleteAclPortACLListException.java,v1.0 2011-11-24 9:22:04 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:22:04
 */
public class DeleteAclPortACLListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public DeleteAclPortACLListException() {
    }

    public DeleteAclPortACLListException(String s) {
        super(s);
    }

    public DeleteAclPortACLListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteAclPortACLListException(Throwable throwable) {
        super(throwable);
    }
}
