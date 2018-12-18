/***********************************************************************
 * $Id: VersionConvertException.java,v1.0 2012-8-6 下午02:30:16 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author RodJohn
 * @created @2012-8-6-下午02:30:16
 * 
 */
public class VersionConvertException extends ServiceException {
    private static final long serialVersionUID = 7247096902651845104L;
    private int[] error;

    /**
     * 
     */
    public VersionConvertException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public VersionConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public VersionConvertException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public VersionConvertException(Throwable cause) {
        super(cause);
    }

    public VersionConvertException(String message, int[] error) {
        super(message);
        this.error = error;
    }

    /**
     * @return the error
     */
    public int[] getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(int[] error) {
        this.error = error;
    }

}
