/***********************************************************************
 * $Id: ReportException.java,v1.0 2014-12-4 下午3:14:24 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.exception;

import com.topvision.framework.exception.TopvisionRuntimeException;

/**
 * @author Rod John
 * @created @2014-12-4-下午3:14:24
 * 
 */
public class ReportException extends TopvisionRuntimeException {

    private static final long serialVersionUID = -1390275986380089083L;

    public ReportException() {
    }

    public ReportException(String s) {
        super(s);
    }

    public ReportException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ReportException(Throwable throwable) {
        super(throwable);
    }

}
