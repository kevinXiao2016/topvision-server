/***********************************************************************
 * $ AddIgmpVlanTransException.java,v1.0 2011-11-24 17:10:42 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:10:42
 */
public class AddIgmpVlanTransException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public AddIgmpVlanTransException() {
    }

    public AddIgmpVlanTransException(String s) {
        super(s);
    }

    public AddIgmpVlanTransException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AddIgmpVlanTransException(Throwable throwable) {
        super(throwable);
    }
}
