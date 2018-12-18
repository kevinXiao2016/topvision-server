/***********************************************************************
 * $ ModifyIgmpMvlanInfoException.java,v1.0 2011-11-24 17:28:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:28:53
 */
public class ModifyIgmpMvlanInfoException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpMvlanInfoException() {
    }

    public ModifyIgmpMvlanInfoException(String s) {
        super(s);
    }

    public ModifyIgmpMvlanInfoException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpMvlanInfoException(Throwable throwable) {
        super(throwable);
    }
}
