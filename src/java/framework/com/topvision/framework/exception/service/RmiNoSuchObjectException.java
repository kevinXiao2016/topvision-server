/***********************************************************************
 * $Id: RmiNoSuchObjectException.java,v1.0 2011-4-11 下午02:19:34 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.service;

/**
 * @author Victor
 * @created @2011-4-11-下午02:19:34
 * 
 */
public class RmiNoSuchObjectException extends RmiException {
    private static final long serialVersionUID = 4289491843248039220L;

    public RmiNoSuchObjectException() {
        super();
    }

    public RmiNoSuchObjectException(String message) {
        super(message);
    }

    public RmiNoSuchObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RmiNoSuchObjectException(Throwable cause) {
        super(cause);
    }
}
