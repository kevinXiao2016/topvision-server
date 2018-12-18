/***********************************************************************
 * $Id: SnmpGetException.java,v1.0 2011-7-10 下午04:46:32 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * @created @2011-7-10-下午04:46:32
 * 
 */
public class SnmpGetException extends SnmpException {
    private static final long serialVersionUID = -1260966278161361532L;

    public SnmpGetException() {
        super();
    }

    public SnmpGetException(String message) {
        super(message);
    }

    public SnmpGetException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpGetException(Throwable cause) {
        super(cause);
    }
}
