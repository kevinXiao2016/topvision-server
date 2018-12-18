/***********************************************************************
 * $Id: AbstractMonitorParam.java,v 1.1 May 30, 2008 4:50:30 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date May 30, 2008 4:50:30 PM
 * 
 * @author kelers
 * 
 */
public class AbstractMonitorParam implements java.io.Serializable {
    private static final long serialVersionUID = -131202266407873130L;
    protected transient Logger logger = LoggerFactory.getLogger(getClass());
    private String error;

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
