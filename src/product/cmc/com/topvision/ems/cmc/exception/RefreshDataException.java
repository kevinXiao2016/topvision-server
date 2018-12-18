/**
 * 
 */
package com.topvision.ems.cmc.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author dosion
 * 
 */
public class RefreshDataException extends ServiceException {
    private static final long serialVersionUID = -8396937960817542175L;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LoadBalException []";
    }

    /**
     * 
     */
    public RefreshDataException() {
    }

    /**
     * @param message
     * @param cause
     */
    public RefreshDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public RefreshDataException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public RefreshDataException(Throwable cause) {
        super(cause);
    }

}
