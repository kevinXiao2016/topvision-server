/***********************************************************************
 * $Id: SniTrunkConfigException.java,v1.0 2011-10-28 下午03:18:59 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * Sni口Trunk配置中出现可预见错误
 * 
 * @author jay
 * @created @2011-10-28-下午03:18:59
 * 
 */
public class SniTrunkConfigException extends ServiceException {
    private static final long serialVersionUID = -2096292645426212164L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public SniTrunkConfigException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     */
    public SniTrunkConfigException(String message) {
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
    public SniTrunkConfigException(String message, Throwable cause) {
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
    public SniTrunkConfigException(Throwable cause) {
        super(cause);
    }
}