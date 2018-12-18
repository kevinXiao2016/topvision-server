/***********************************************************************
 * $Id: TelnetConnectException.java,v1.0 2017年1月13日 下午2:39:17 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author vanzand
 * @created @2017年1月13日-下午2:39:17
 *
 */
public class TelnetClientException extends ServiceException {

    private static final long serialVersionUID = -8152766254250218819L;

    public TelnetClientException() {
        super();
    }

    public TelnetClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelnetClientException(String message) {
        super(message);
    }

    public TelnetClientException(Throwable cause) {
        super(cause);
    }

}
