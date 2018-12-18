/***********************************************************************
 * $ AddAclPortACLListException.java,v1.0 2011-11-24 9:23:49 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:23:49
 */
public class AddAclPortACLListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddAclPortACLListException() {
    }

    public AddAclPortACLListException(String s) {
        super(s);
    }

    public AddAclPortACLListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddAclPortACLListException(Throwable throwable) {
        super(throwable);
    }
}
