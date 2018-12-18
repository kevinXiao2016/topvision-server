/***********************************************************************
 * $ RefreshAclPortACLListException.java,v1.0 2011-11-24 10:02:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-10:02:39
 */
public class RefreshAclPortACLListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshAclPortACLListException() {
    }

    public RefreshAclPortACLListException(String s) {
        super(s);
    }

    public RefreshAclPortACLListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshAclPortACLListException(Throwable throwable) {
        super(throwable);
    }
}
