/***********************************************************************
 * $Id: SetDhcpRelayFailException.java,v1.0 2013-8-21 下午1:37:55 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.exception;

import java.util.Map;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author dosion
 * @created @2013-8-21-下午1:37:55
 *
 */
public class SetDhcpRelayFailException extends ServiceException {
    private static final long serialVersionUID = -6418839773156422334L;
    private Map<String, Object> result;
    
    public SetDhcpRelayFailException() {
        super();
    }

    public SetDhcpRelayFailException(String message) {
        super(message);
    }

    public SetDhcpRelayFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetDhcpRelayFailException(Throwable cause) {
        super(cause);
    }
    
    public SetDhcpRelayFailException(String message, Map<String, Object> result) {
        super(message);
        this.result = result;
    }

    public SetDhcpRelayFailException(String message, Throwable cause, Map<String, Object> result) {
        super(message, cause);
        this.result = result;
    }

    public SetDhcpRelayFailException(Throwable cause, Map<String, Object> result) {
        super(cause);
        this.result = result;
    }

    public Map<String, Object> getResult() {
        return result;
    }    
    
}
