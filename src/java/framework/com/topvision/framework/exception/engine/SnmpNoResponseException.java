/***********************************************************************
 * $Id: SnmpNoResponseException.java,v1.0 2012-4-12 上午11:28:04 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author huqiao
 * @created @2012-4-12-上午11:28:04
 * 
 */
public class SnmpNoResponseException extends SnmpException {
    /**
     * 
     */
    private static final long serialVersionUID = 8428352619933668497L;

    public SnmpNoResponseException() {
        super();
    }

    public SnmpNoResponseException(String message) {
        super(message);
        // @Modify by Rod 保证SnmpException都具有errorStatusText
        setErrorStatusText(message);
    }

    public SnmpNoResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpNoResponseException(Throwable cause) {
        super(cause);
    }

}
