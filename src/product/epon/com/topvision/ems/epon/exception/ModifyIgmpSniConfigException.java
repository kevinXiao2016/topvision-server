/***********************************************************************
 * $ ModifyIgmpSniConfigException.java,v1.0 2011-11-24 17:31:43 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:31:43
 */
public class ModifyIgmpSniConfigException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpSniConfigException() {
    }

    public ModifyIgmpSniConfigException(String s) {
        super(s);
    }

    public ModifyIgmpSniConfigException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpSniConfigException(Throwable throwable) {
        super(throwable);
    }
}
