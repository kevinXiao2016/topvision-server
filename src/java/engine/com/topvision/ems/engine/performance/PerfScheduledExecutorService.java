/***********************************************************************
 * $ PerfScheduledExecutorService.java,v1.0 2012-5-3 16:14:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.topvision.ems.facade.domain.OperClass;

/**
 * @author jay
 * @created @2012-5-3-16:14:13
 */
public class PerfScheduledExecutorService extends ScheduledThreadPoolExecutor {
    private OperClass operClass;

    public PerfScheduledExecutorService() {
        super(1);
    }

    public OperClass getOperClass() {
        return operClass;
    }

    public void setOperClass(OperClass operClass) {
        this.operClass = operClass;
    }
}
