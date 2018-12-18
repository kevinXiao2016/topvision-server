/***********************************************************************
 * $Id: UnknownSnmpSetException.java,v1.0 2011-3-31 下午05:13:08 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * 
 */
public class UnknownSnmpSetException extends SnmpException {
    private static final long serialVersionUID = 6108520651624154822L;

    public UnknownSnmpSetException() {
        super();
    }

    public UnknownSnmpSetException(String message) {
        super(message);
    }

    public UnknownSnmpSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownSnmpSetException(Throwable cause) {
        super(cause);
    }
}
