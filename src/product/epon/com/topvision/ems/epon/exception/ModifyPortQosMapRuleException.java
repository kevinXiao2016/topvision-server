/***********************************************************************
 * $ ModifyPortQosMapRuleException.java,v1.0 2011-11-24 16:26:26 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-16:26:26
 */
public class ModifyPortQosMapRuleException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyPortQosMapRuleException() {
    }

    public ModifyPortQosMapRuleException(String s) {
        super(s);
    }

    public ModifyPortQosMapRuleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyPortQosMapRuleException(Throwable throwable) {
        super(throwable);
    }
}
