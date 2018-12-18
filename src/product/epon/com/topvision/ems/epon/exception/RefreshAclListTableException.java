/***********************************************************************
 * $ RefreshAclListTableException.java,v1.0 2011-11-24 10:02:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-10:02:53
 */
public class RefreshAclListTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshAclListTableException() {
    }

    public RefreshAclListTableException(String s) {
        super(s);
    }

    public RefreshAclListTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshAclListTableException(Throwable throwable) {
        super(throwable);
    }
}
