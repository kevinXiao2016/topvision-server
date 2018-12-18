/***********************************************************************
 * $Id: AnnotationException.java,v1.0 2011-3-31 下午05:12:35 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception;

/**
 * @author Victor
 * 
 */
public class AnnotationException extends TopvisionRuntimeException {
    private static final long serialVersionUID = 6572926749546730135L;

    public AnnotationException() {
        super();
    }

    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationException(Throwable cause) {
        super(cause);
    }
}
