/***********************************************************************
 * $ ModifyIgmpMcUniConfigException.java,v1.0 2011-11-24 17:28:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:28:15
 */
public class ModifyIgmpMcUniConfigException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpMcUniConfigException() {
    }

    public ModifyIgmpMcUniConfigException(String s) {
        super(s);
    }

    public ModifyIgmpMcUniConfigException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpMcUniConfigException(Throwable throwable) {
        super(throwable);
    }
}
