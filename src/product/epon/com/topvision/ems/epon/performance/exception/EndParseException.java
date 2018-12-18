/***********************************************************************
 * $ AddAclListException.java,v1.0 2011-11-24 9:12:33 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:12:33
 */
public class EndParseException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public EndParseException() {
    }

    public EndParseException(String s) {
        super(s);
    }

    public EndParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EndParseException(Throwable throwable) {
        super(throwable);
    }
}
