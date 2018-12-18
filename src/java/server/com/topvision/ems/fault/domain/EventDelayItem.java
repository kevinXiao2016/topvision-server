/***********************************************************************
 * $Id: EventDelayItem.java,v1.0 2016-4-6 上午11:46:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.domain;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author flack
 * @created @2016-4-6-上午11:46:09
 *
 */
public class EventDelayItem implements Delayed {

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
    public EventDelayItem(long delayTime, Event event) {
        this.delayTime = delayTime + System.currentTimeMillis();
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDelayItem that = (EventDelayItem) o;
        if (event == null || that.event == null) {return false;}
        if (event.getEntityId() != null ? !event.getEntityId().equals(that.event.getEntityId()) : that.event.getEntityId() != null) return false;
        if (event.getTypeId() != null ? !event.getTypeId().equals(that.event.getTypeId()) : that.event.getEntityId() != null) return false;
        if (event.getSource() != null ? !event.getSource().equals(that.event.getSource()) : that.event.getSource() != null) return false;

        return true;
    }

    public int hashCode() {
        if (event == null) {return 0;}
        long entityId = event.getEntityId();
        int typeId = event.getTypeId();
        String source = event.getSource();
        int result = (int) (entityId ^ (entityId >>> 32));
        result = 31 * result + typeId;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
