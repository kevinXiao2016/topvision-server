/***********************************************************************
 * $Id: BoardPreConfigChangedException.java,v1.0 2011-12-18 上午09:15:13 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * 板卡预配置类型已改变
 * 
 * @author Administrator
 * @created @2011-12-18-上午09:15:13
 * 
 */
public class BoardPreConfigChangedException extends ServiceException {
    private static final long serialVersionUID = 208417724764959017L;

    /**
     * Constructs a new runtime exception with <code>null</code> as its detail message. The cause is
     * not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public BoardPreConfigChangedException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     * 
     * @param message
     *            the detail message. The detail message is saved for later retrieval by the
     *            {@link #getMessage()} method.
     */
    public BoardPreConfigChangedException(String message) {
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
    public BoardPreConfigChangedException(String message, Throwable cause) {
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
    public BoardPreConfigChangedException(Throwable cause) {
        super(cause);
    }
}
