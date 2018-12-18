/***********************************************************************
 * $Id: TelnetLoginException.java,v1.0 2015年11月10日 下午8:31:42 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.exception;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * @author loyal
 * @created @2015年11月10日-下午8:31:42
 *
 */
public class TelnetLoginException extends TopvisionRuntimeException {
    private static final long serialVersionUID = 2487269972942069966L;

    public TelnetLoginException() {
        super();
    }

    public TelnetLoginException(String message) {
        super(message);
    }

    public TelnetLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelnetLoginException(Throwable cause) {
        super(cause);
    }

}
