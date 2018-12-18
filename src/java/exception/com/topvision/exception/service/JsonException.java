/***********************************************************************
 * $ NotCollectionException.java,v1.0 2012-7-15 10:38:40 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2012-7-15-10:38:40
 */
public class JsonException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public JsonException() {
    }

    public JsonException(Throwable cause) {
        super(cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
