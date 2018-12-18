/***********************************************************************
 * $Id: TimeCostRecord.java,v1.0 2017年9月9日 上午11:34:41 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

/**
 * @author vanzand
 * @created @2017年9月9日-上午11:34:41
 *
 */
public class TimeCostRecord {
    private long startTime;
    private long endTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCost() {
        return endTime - startTime;
    }
}
