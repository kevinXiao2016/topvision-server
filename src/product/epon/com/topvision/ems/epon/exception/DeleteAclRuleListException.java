/***********************************************************************
 * $ DeleteAclRuleListException.java,v1.0 2011-11-24 9:25:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:25:16
 */
public class DeleteAclRuleListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public DeleteAclRuleListException() {
    }

    public DeleteAclRuleListException(String s) {
        super(s);
    }

    public DeleteAclRuleListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteAclRuleListException(Throwable throwable) {
        super(throwable);
    }
}
