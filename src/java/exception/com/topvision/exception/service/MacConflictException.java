/***********************************************************************
 * $Id: MacConflictException.java,v1.0 2017年9月21日 下午1:48:40 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.exception.service;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author vanzand
 * @created @2017年9月21日-下午1:48:40
 *
 */
public class MacConflictException extends ServiceException {

    private static final long serialVersionUID = -1947299280627021304L;

    public MacConflictException() {
        super();
    }

    public MacConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public MacConflictException(String message) {
        super(message);
    }

    public MacConflictException(Throwable cause) {
        super(cause);
    }

}
