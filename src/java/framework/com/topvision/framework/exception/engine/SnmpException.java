/***********************************************************************
 * $Id: SnmpException.java,v1.0 2011-3-31 下午05:05:09 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.exception.engine;

/**
 * @author Victor
 * 
 */
public class SnmpException extends EngineException {
    private static final long serialVersionUID = -7955479332316978276L;
    private String errorIpAddress;
    private String errorStatusText;
    private Integer errorCode;

    public SnmpException() {
        super();
    }

    public SnmpException(String message) {
        super(message);
    }

    public SnmpException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnmpException(Throwable cause) {
        super(cause);
    }

    public SnmpException(String errorStatusText, Integer errorCode) {
        super(errorStatusText);
        this.errorStatusText = errorStatusText;
        this.errorCode = errorCode;
    }

    
    /**
     * @param errorIpAddress
     * @param errorStatusText
     * @param errorCode
     */
    public SnmpException(String errorIpAddress, String errorStatusText, Integer errorCode) {
        super(errorStatusText);
        this.errorIpAddress = errorIpAddress;
        this.errorStatusText = errorStatusText;
        this.errorCode = errorCode;
    }

    /**
     * @return the errorStatusText
     */
    public String getErrorStatusText() {
        return errorStatusText;
    }

    /**
     * @param errorStatusText
     *            the errorStatusText to set
     */
    public void setErrorStatusText(String errorStatusText) {
        this.errorStatusText = errorStatusText;
    }

    /**
     * @return the errorCode
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorIpAddress
     */
    public String getErrorIpAddress() {
        return errorIpAddress;
    }

    /**
     * @param errorIpAddress the errorIpAddress to set
     */
    public void setErrorIpAddress(String errorIpAddress) {
        this.errorIpAddress = errorIpAddress;
    }

    
}
