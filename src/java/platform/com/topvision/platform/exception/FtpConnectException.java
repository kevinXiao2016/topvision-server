/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 *
 */
public class FtpConnectException extends ServiceException {
    private static final long serialVersionUID = -4413969392867563681L;
    
    public FtpConnectException(){
    }
    
    public FtpConnectException(String s) {
        super(s);
    }

    public FtpConnectException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FtpConnectException(Throwable throwable) {
        super(throwable);
    }
}
