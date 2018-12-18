/***********************************************************************
 * $Id: SetCmcSniStatusException.java,v1.0 2012-2-15 下午04:15:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author loyal
 * @created @2012-2-15-下午04:15:02
 * 
 */
public class SetCmcSniStatusException extends ServiceException {
    private static final long serialVersionUID = -2544074222513876577L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public SetCmcSniStatusException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     */
    public SetCmcSniStatusException(String message) {
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
    public SetCmcSniStatusException(String message, Throwable cause) {
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
    public SetCmcSniStatusException(Throwable cause) {
        super(cause);
    }
}
