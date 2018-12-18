/***********************************************************************
 * $Id: CmcForceReplaceException.java,v1.0 2016-5-24 下午4:04:39 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author Rod John
 * @created @2016-5-24-下午4:04:39
 *
 */
public class OnuForceReplaceException extends ServiceException {

    private static final long serialVersionUID = -1066674010274394666L;

    
    public OnuForceReplaceException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public OnuForceReplaceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public OnuForceReplaceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public OnuForceReplaceException(Throwable cause) {
        super(cause);
    }

}
