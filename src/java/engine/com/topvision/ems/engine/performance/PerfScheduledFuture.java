/***********************************************************************
 * $Id: PerfScheduledFuture.java,v1.0 2013-8-14 下午02:14:41 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

import com.topvision.ems.facade.domain.OperClass;

/**
 * @author Rod John
 * @created @2013-8-14-下午02:14:41
 * 
 */
public class PerfScheduledFuture implements Serializable {
    private static final long serialVersionUID = 1641555809580948339L;
    private ScheduledFuture<?> future;
    private OperClass operClass;

    /**
     * @return the future
     */
    public ScheduledFuture<?> getFuture() {
        return future;
    }

    /**
     * @param future
     *            the future to set
     */
    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    /**
     * @return the operClass
     */
    public OperClass getOperClass() {
        return operClass;
    }

    /**
     * @param operClass
     *            the operClass to set
     */
    public void setOperClass(OperClass operClass) {
        this.operClass = operClass;
    }

    /**
     * @param future
     * @param operClass
     */
    public PerfScheduledFuture(ScheduledFuture<?> future, OperClass operClass) {
        this.future = future;
        this.operClass = operClass;
    }

}
