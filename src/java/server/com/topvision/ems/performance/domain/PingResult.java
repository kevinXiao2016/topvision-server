/***********************************************************************
 * $Id: PingResult.java,v1.0 2014-1-7 上午10:05:05 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author Rod John
 * @created @2014-1-7-上午10:05:05
 * 
 */
public class PingResult extends PerformanceResult<OperClass> implements Serializable {
    private static final long serialVersionUID = -4363132061685995577L;
    private Integer delay;
    private Timestamp collectTime;

    /**
     * @param domain
     */
    public PingResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the delay
     */
    public Integer getDelay() {
        return delay;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

}
