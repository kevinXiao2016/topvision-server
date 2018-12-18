/***********************************************************************
 * $ AddIgmpMvlanException.java,v1.0 2011-11-24 17:09:03 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:09:03
 */
public class AddIgmpMvlanException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddIgmpMvlanException() {
    }

    public AddIgmpMvlanException(String s) {
        super(s);
    }

    public AddIgmpMvlanException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddIgmpMvlanException(Throwable throwable) {
        super(throwable);
    }
}
