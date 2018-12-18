/***********************************************************************
 * $ DeleteIgmpVlanTransException.java,v1.0 2011-11-24 17:19:59 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:19:59
 */
public class DeleteIgmpVlanTransException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public DeleteIgmpVlanTransException() {
    }

    public DeleteIgmpVlanTransException(String s) {
        super(s);
    }

    public DeleteIgmpVlanTransException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteIgmpVlanTransException(Throwable throwable) {
        super(throwable);
    }
}
