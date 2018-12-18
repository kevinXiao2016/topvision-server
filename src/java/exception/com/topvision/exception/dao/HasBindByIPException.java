package com.topvision.exception.dao;

import com.topvision.framework.exception.dao.DaoException;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-4-10 Time: 12:34:56 To change this
 * template use File | Settings | File Templates.
 */
public class HasBindByIPException extends DaoException {
    private static final long serialVersionUID = -7628182303992043908L;

    public HasBindByIPException() {
    }

    public HasBindByIPException(String s) {
        super(s);
    }

    public HasBindByIPException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HasBindByIPException(Throwable throwable) {
        super(throwable);
    }
}
