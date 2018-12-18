/***********************************************************************
 * $Id: LoadBalException.java,v1.0 2013-9-2 上午09:43:42 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author bryan
 * @created @2013-9-2-上午09:43:42
 *
 */
public class LoadBalException extends ServiceException {

    /**
     * 
     */
    private static final long serialVersionUID = 6668780800283138745L;

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoadBalException []";
    }

    /**
     * 
     */
    public LoadBalException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public LoadBalException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public LoadBalException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public LoadBalException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    
    

}
