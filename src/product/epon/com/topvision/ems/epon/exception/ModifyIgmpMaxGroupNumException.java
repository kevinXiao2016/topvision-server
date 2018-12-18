/***********************************************************************
 * $ ModifyIgmpMaxGroupNumException.java,v1.0 2011-11-24 17:26:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-17:26:32
 */
public class ModifyIgmpMaxGroupNumException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public ModifyIgmpMaxGroupNumException() {
    }

    public ModifyIgmpMaxGroupNumException(String s) {
        super(s);
    }

    public ModifyIgmpMaxGroupNumException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModifyIgmpMaxGroupNumException(Throwable throwable) {
        super(throwable);
    }
}
