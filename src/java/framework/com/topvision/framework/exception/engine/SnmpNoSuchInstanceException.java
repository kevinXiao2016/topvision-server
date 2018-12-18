/***********************************************************************
 * $Id: SnmpNoSuchInstanceException.java,v1.0 2011-12-18 上午10:33:05 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author huqiao
 * @created @2011-12-18-上午10:33:05
 * 
 */
public class SnmpNoSuchInstanceException extends SnmpException {

    private static final long serialVersionUID = -2575143836863575867L;

    public SnmpNoSuchInstanceException() {
        super();
    }

    public SnmpNoSuchInstanceException(String message) {
        super(message);
    }

    public SnmpNoSuchInstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpNoSuchInstanceException(Throwable cause) {
        super(cause);
    }
}
