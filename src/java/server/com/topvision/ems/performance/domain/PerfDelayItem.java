/***********************************************************************
 * $Id: PerfDelayItem.java,v1.0 2013-8-28 上午09:35:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.topvision.ems.fault.domain.Event;

/**
 * @author Rod John
 * @created @2013-8-28-上午09:35:01
 * 
 */
public class PerfDelayItem implements Delayed {
    private long delayTime;
    private Event event;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Delayed o) {
        long d = (this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return delayTime - System.currentTimeMillis();
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event
     *            the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @param delayTime
     * @param event
     */
    public PerfDelayItem(long delayTime, Event event) {
        this.delayTime = delayTime + System.currentTimeMillis();
        this.event = event;
    }

}
