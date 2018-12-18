/***********************************************************************
 * $Id: ReousrceKeyNotFoundException.java,v1.0 2013-3-27 下午4:04:29 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.exception;

/**
 * 国际化Key没有找到
 * @author Bravin
 * @created @2013-3-27-下午4:04:29
 *
 */
public class ReousrceKeyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 4760150914450147696L;

    public ReousrceKeyNotFoundException() {
        super();
    }

    public ReousrceKeyNotFoundException(String message) {
        super(message);
    }

    public ReousrceKeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReousrceKeyNotFoundException(Throwable cause) {
        super(cause);
    }

}
