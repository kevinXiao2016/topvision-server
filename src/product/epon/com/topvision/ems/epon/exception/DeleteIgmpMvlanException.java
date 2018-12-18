/***********************************************************************
 * $ DeleteIgmpMvlanException.java,v1.0 2011-11-24 17:15:12 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:15:12
 */
public class DeleteIgmpMvlanException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public DeleteIgmpMvlanException() {
    }

    public DeleteIgmpMvlanException(String s) {
        super(s);
    }

    public DeleteIgmpMvlanException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DeleteIgmpMvlanException(Throwable throwable) {
        super(throwable);
    }
}
