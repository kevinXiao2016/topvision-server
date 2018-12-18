/***********************************************************************
 * $ RefreshQosPortBaseQosMapTableException.java,v1.0 2011-11-24 12:02:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author jay
 * @created @2011-11-24-12:02:46
 */
public class RefreshQosPortBaseQosMapTableException extends ServiceException {
    private static final long serialVersionUID = 7342180206202447869L;

    public RefreshQosPortBaseQosMapTableException() {
    }

    public RefreshQosPortBaseQosMapTableException(String s) {
        super(s);
    }

    public RefreshQosPortBaseQosMapTableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RefreshQosPortBaseQosMapTableException(Throwable throwable) {
        super(throwable);
    }
}
