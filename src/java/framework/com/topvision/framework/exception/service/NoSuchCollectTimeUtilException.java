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
public class NoSuchCollectTimeUtilException extends RmiException {
    private static final long serialVersionUID = 4289491843248039220L;

    public NoSuchCollectTimeUtilException() {
        super();
    }

    public NoSuchCollectTimeUtilException(String message) {
        super(message);
    }

    public NoSuchCollectTimeUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCollectTimeUtilException(Throwable cause) {
        super(cause);
    }
}