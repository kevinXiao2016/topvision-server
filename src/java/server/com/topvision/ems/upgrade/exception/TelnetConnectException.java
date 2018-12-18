/***********************************************************************
 * $Id: TelnetConnectException.java,v1.0 2015年11月10日 下午8:30:55 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.exception;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * @author loyal
 * @created @2015年11月10日-下午8:30:55
 *
 */
public class TelnetConnectException extends TopvisionRuntimeException {

    private static final long serialVersionUID = -4787886653247615671L;

    public TelnetConnectException() {
        super();
    }

    public TelnetConnectException(String message) {
        super(message);
    }

    public TelnetConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelnetConnectException(Throwable cause) {
        super(cause);
    }
}
