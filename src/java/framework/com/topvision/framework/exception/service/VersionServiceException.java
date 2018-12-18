/***********************************************************************
 * $Id: VersionServiceException.java,v1.0 2011-5-25 上午11:44:51 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

/**
 * @author Victor
 * @created @2011-5-25-上午11:44:51
 * 
 */
public class VersionServiceException extends ServiceException {
    private static final long serialVersionUID = -2954929559132061126L;

    public VersionServiceException() {
        super();
    }

    public VersionServiceException(String message) {
        super(message);
    }

    public VersionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionServiceException(Throwable cause) {
        super(cause);
    }
}
