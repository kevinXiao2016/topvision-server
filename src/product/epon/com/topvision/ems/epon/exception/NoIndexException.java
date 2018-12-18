/***********************************************************************
 * $ NoIndexException.java,v1.0 2012-12-4 09:00:15 $
 *
 * @author: lizongtian
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author lizongtian
 * @created @2012-12-4-09:00:15
 */
public class NoIndexException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public NoIndexException() {
    }

    public NoIndexException(String s) {
        super(s);
    }

    public NoIndexException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NoIndexException(Throwable throwable) {
        super(throwable);
    }
}
