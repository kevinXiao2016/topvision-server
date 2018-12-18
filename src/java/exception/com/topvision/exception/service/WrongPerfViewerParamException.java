/***********************************************************************
 * $ WrongPerfViewerTypeException.java,v1.0 2012-7-15 16:12:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2012-7-15-16:12:16
 */
public class WrongPerfViewerParamException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public WrongPerfViewerParamException() {
    }

    public WrongPerfViewerParamException(Throwable cause) {
        super(cause);
    }

    public WrongPerfViewerParamException(String message) {
        super(message);
    }

    public WrongPerfViewerParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
