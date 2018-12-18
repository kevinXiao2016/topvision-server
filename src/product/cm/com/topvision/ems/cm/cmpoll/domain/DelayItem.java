/***********************************************************************
 * $ OnuOnlineDelayObject.java,v1.0 2012-1-13 12:04:21 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.domain;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 一个延时对象，用于延时队列，延时单位毫秒
 * 
 * @author jay
 * @created @2012-1-13-12:04:21
 */
public class DelayItem<T> implements Delayed, Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    private long time;
    private T item;

    public DelayItem(T submit, long timeout) {
        this.time = now() + timeout;
        this.item = submit;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public T getItem() {
        return this.item;
    }

    public long getDelay(TimeUnit unit) {
        return time - now();
    }

    public int compareTo(Delayed o) {
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DelayItem");
        sb.append("{item=").append(item);
        sb.append('}');
        return sb.toString();
    }
}
