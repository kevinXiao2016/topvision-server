/***********************************************************************
 * $Id: PonVlanConfigException.java,v1.0 2011-10-30 上午08:55:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * Sni口VLAN配置异常
 * 
 * @author haojie
 * @created @2011-10-30-上午08:55:59
 * 
 */
public class SniVlanConfigException extends ServiceException {
    private static final long serialVersionUID = -3298086078052332735L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public SniVlanConfigException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     */
    public SniVlanConfigException(String message) {
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
    public SniVlanConfigException(String message, Throwable cause) {
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
    public SniVlanConfigException(Throwable cause) {
        super(cause);
    }

}
