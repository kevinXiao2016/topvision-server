/***********************************************************************
 * $Id: SnmpV3Exception.java,v1.0 2013-1-19 上午10:13:29 $
 * 
 * @author: RodJohn
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author RodJohn
 * @created @2013-1-19-上午10:13:29
 *
 */
public class SnmpV3Exception extends SnmpException {

    private static final long serialVersionUID = 6598386084742875103L;
    public SnmpV3Exception() {
        super();
    }

    public SnmpV3Exception(String message) {
        super(message);
    }

    public SnmpV3Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpV3Exception(Throwable cause) {
        super(cause);
    }

    public SnmpV3Exception(String errorStatusText, Integer errorCode) {
        super(errorStatusText, errorCode);
    }

    public SnmpV3Exception(String errorIpAddress, String errorStatusText, Integer errorCode) {
        super(errorIpAddress, errorStatusText, errorCode);
    }
}
