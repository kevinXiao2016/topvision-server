/***********************************************************************
 * $Id: SnmpNoSuchObjectException.java,v1.0 2014-1-9 下午3:40:57 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Rod John
 * @created @2014-1-9-下午3:40:57
 * 
 */
public class SnmpNoSuchObjectException extends SnmpException {

    private static final long serialVersionUID = 6953693715342255114L;

    public SnmpNoSuchObjectException() {
        super();
    }

    public SnmpNoSuchObjectException(String message) {
        super(message);
    }

    public SnmpNoSuchObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpNoSuchObjectException(Throwable cause) {
        super(cause);
    }
}
