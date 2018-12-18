/***********************************************************************
 * $ ModifyOnuSlaListException.java,v1.0 2011-11-24 16:26:49 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-16:26:49
 */
public class ModifyOnuSlaListException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyOnuSlaListException() {
    }

    public ModifyOnuSlaListException(String s) {
        super(s);
    }

    public ModifyOnuSlaListException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyOnuSlaListException(Throwable throwable) {
        super(throwable);
    }
}
