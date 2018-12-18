/***********************************************************************
 * $Id: CmRefreshSignalException.java,v1.0 2016年12月12日 下午2:03:26 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.exception;

import com.topvision.framework.exception.service.ServiceException;

/**
 * @author vanzand
 * @created @2016年12月12日-下午2:03:26
 *
 */
public class CmListSizeOverSpecificationException extends ServiceException {

    private static final long serialVersionUID = -8178739691780751046L;

    public CmListSizeOverSpecificationException() {
    }

    public CmListSizeOverSpecificationException(String s) {
        super(s);
    }

    public CmListSizeOverSpecificationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CmListSizeOverSpecificationException(Throwable throwable) {
        super(throwable);
    }

}
