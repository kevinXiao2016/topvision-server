/***********************************************************************
 * $Id: SnmpNoSuchNameException.java,v1.0 2011-12-16 下午01:31:40 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author huqiao
 * @created @2011-12-16-下午01:31:40
 *
 */
public class SnmpNoSuchNameException extends SnmpException {

    private static final long serialVersionUID = 5983951620057245135L;

    public SnmpNoSuchNameException() {
        super();
    }

    public SnmpNoSuchNameException(String message) {
        super(message);
    }

    public SnmpNoSuchNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpNoSuchNameException(Throwable cause) {
        super(cause);
    }
}
