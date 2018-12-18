/***********************************************************************
 * $Id: SendConfigResultNotFound.java,v1.0 2014-7-21 下午5:09:25 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2014-7-21-下午5:09:25
 *
 */
public class SendConfigException extends ServiceException {

    public SendConfigException() {
        super();
    }

    public SendConfigException(String message) {
        super(message);
    }

    public SendConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendConfigException(Throwable cause) {
        super(cause);
    }

}
