/***********************************************************************
 * $ AddAclRuleListException.java,v1.0 2011-11-24 9:24:40 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:24:40
 */
public class AddAclRuleListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddAclRuleListException() {
    }

    public AddAclRuleListException(String s) {
        super(s);
    }

    public AddAclRuleListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddAclRuleListException(Throwable throwable) {
        super(throwable);
    }
}
