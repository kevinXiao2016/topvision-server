/***********************************************************************
 * $ ModifyAclRuleListException.java,v1.0 2011-11-24 9:24:59 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-9:24:59
 */
public class ModifyAclRuleListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyAclRuleListException() {
    }

    public ModifyAclRuleListException(String s) {
        super(s);
    }

    public ModifyAclRuleListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyAclRuleListException(Throwable throwable) {
        super(throwable);
    }
}
