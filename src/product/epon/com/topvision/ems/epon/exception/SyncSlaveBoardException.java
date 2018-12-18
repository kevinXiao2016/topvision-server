/***********************************************************************
 * $ SyncSlaveBoardException.java,v1.0 2012-05-15-14:24:33 $
 *
 * @author: lizongtian
 *
 * (c)Copyright 2012 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author lizongtian
 * @created @2012-05-15-14:24:33
 */
public class SyncSlaveBoardException extends ServiceException {

    private static final long serialVersionUID = -4910986626136045345L;

    public SyncSlaveBoardException() {
    }

    public SyncSlaveBoardException(String s) {
        super(s);
    }

    public SyncSlaveBoardException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SyncSlaveBoardException(Throwable throwable) {
        super(throwable);
    }

}
