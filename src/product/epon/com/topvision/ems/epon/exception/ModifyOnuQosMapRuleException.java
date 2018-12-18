/***********************************************************************
 * $ ModifyOnuQosMapRuleException.java,v1.0 2011-11-24 16:00:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-16:00:00
 */
public class ModifyOnuQosMapRuleException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyOnuQosMapRuleException() {
    }

    public ModifyOnuQosMapRuleException(String s) {
        super(s);
    }

    public ModifyOnuQosMapRuleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyOnuQosMapRuleException(Throwable throwable) {
        super(throwable);
    }
}
