/***********************************************************************
 * $Id: ExceedMaxTelnetNumber.java,v1.0 2014年11月17日 上午9:41:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.exception;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * @author loyal
 * @created @2014年11月17日-上午9:41:02
 * 
 */
public class ExceedMaxTelnetNumberException extends TopvisionRuntimeException {
    private static final long serialVersionUID = -2441378971992517237L;

    public ExceedMaxTelnetNumberException() {
        super();
    }

    public ExceedMaxTelnetNumberException(String message) {
        super(message);
    }

    public ExceedMaxTelnetNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceedMaxTelnetNumberException(Throwable cause) {
        super(cause);
    }
}
