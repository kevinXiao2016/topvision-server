/***********************************************************************
 * $Id: SnmpSetException.java,v1.0 2011-7-10 下午03:30:08 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;


/**
 * @author Victor
 * @created @2011-7-10-下午03:30:08
 * 
 */
public class SnmpSetException extends SnmpException {
    private static final long serialVersionUID = 9140540223661016187L;
    
    public SnmpSetException() {
        super();
    }

    public SnmpSetException(String message) {
        super(message);
    }

    public SnmpSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpSetException(Throwable cause) {
        super(cause);
    }

    public SnmpSetException(String errorStatusText, Integer errorCode) {
        super(errorStatusText, errorCode);
    }

    public SnmpSetException(String errorIpAddress, String errorStatusText, Integer errorCode) {
        super(errorIpAddress, errorStatusText, errorCode);
    }
    
}
