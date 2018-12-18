/***********************************************************************
 * $Id: DeviceNotExistException.java,v 1.1 Oct 18, 2008 1:39:29 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.exception.facade;

import com.topvision.framework.exception.facade.FacadeException;

/**
 * @Create Date Oct 18, 2008 1:39:29 PM
 * 
 * @author kelers
 * 
 */
public class DeviceNotExistException extends FacadeException {
    private static final long serialVersionUID = 2684861985923068124L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public DeviceNotExistException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     */
    public DeviceNotExistException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically
     * incorporated in this runtime exception's detail message.
     * 
     * @param message
     *            the detail message (which is saved for later retrieval by the
     *            {@link #getMessage()} method).
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *            (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent
     *            or unknown.)
     * @since 1.4
     */
    public DeviceNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and
     * detail message of <tt>cause</tt>). This constructor is useful for runtime exceptions that are
     * little more than wrappers for other throwables.
     * 
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #getCause()} method).
     *            (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent
     *            or unknown.)
     * @since 1.4
     */
    public DeviceNotExistException(Throwable cause) {
        super(cause);
    }
}