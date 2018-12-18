/***********************************************************************
 * $Id: EntityRefreshException.java,v1.0 2012-4-10 下午04:08:25 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception;

/**
 * @author huqiao
 * @created @2012-4-10-下午04:08:25
 *
 */
public class EntityRefreshException extends TopvisionRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8860578681106186972L;

    public EntityRefreshException() {
        super();
    }

    public EntityRefreshException(String message) {
        super(message);
    }

    public EntityRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityRefreshException(Throwable cause) {
        super(cause);
    }
}
