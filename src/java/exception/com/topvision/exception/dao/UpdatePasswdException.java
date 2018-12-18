package com.topvision.exception.dao;

import com.topvision.framework.exception.dao.DaoException;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-4-10 Time: 12:29:10 To change this
 * template use File | Settings | File Templates.
 */
public class UpdatePasswdException extends DaoException {
    private static final long serialVersionUID = -5225312374097304671L;

    public UpdatePasswdException() {
    }

    public UpdatePasswdException(String s) {
        super(s);
    }

    public UpdatePasswdException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UpdatePasswdException(Throwable throwable) {
        super(throwable);
    }
}
