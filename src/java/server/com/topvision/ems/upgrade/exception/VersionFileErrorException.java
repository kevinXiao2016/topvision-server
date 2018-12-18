/***********************************************************************
 * $Id: SendConfigResultNotFound.java,v1.0 2014-7-21 下午5:09:25 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2014-7-21-下午5:09:25
 * 
 */
public class VersionFileErrorException extends ServiceException {
    private static final long serialVersionUID = -4673107471539549964L;

    public VersionFileErrorException() {
        super();
    }

    public VersionFileErrorException(String message) {
        super(message);
    }

    public VersionFileErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionFileErrorException(Throwable cause) {
        super(cause);
    }

}
