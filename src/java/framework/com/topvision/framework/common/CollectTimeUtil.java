/***********************************************************************
 * $ CollectTimeUtil.java,v1.0 2013-6-20 20:13:16 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

import com.topvision.framework.exception.service.NoSuchCollectTimeUtilException;

/**
 * @author jay
 * @created @2013-6-20-20:13:16
 */
public class CollectTimeUtil implements Serializable {
    private static final long serialVersionUID = -2928229279330963179L;
    public static String CmStatus = "cmCollectPeriod";
    public static String CpeStatus = "cpeCollectPeriod";
    private static Map<String, CollectTimeUtil> configMap = Collections
            .synchronizedMap(new HashMap<String, CollectTimeUtil>());
    private long period;
    private long startTime;

    public CollectTimeRange getCollectTimeRange() {
        long now = System.currentTimeMillis();
        return getCollectTimeRange(now);
    }

    public CollectTimeRange getCollectTimeRange(long now) {
        long range = now - startTime;
        long offset = range % period;
        CollectTimeRange collectTimeRange = new CollectTimeRange();
        collectTimeRange.setStartTimeLong(now - offset - period - 1000L);
        collectTimeRange.setEndTimeLong(now - offset);
        return collectTimeRange;
    }

    public CollectTimeRange getCollectTimeRange(long dt, int i) {
        long now = dt - period * i;
        return getCollectTimeRange(now);
    }

    public Long getCollectTime() {
        long now = System.currentTimeMillis();
        return getCollectTime(now);
    }

    public Long getCollectTime(long now) {
        long range = now - startTime;
        long offset = range % period;
        long st = now - offset - period - 1000L;
        return st + period / 2;
    }

    public static CollectTimeUtil createCollectTimeUtil(String name, Long startTime, Long period) {
        CollectTimeUtil collectTimeUtil = new CollectTimeUtil();
        collectTimeUtil.setStartTime(startTime);
        collectTimeUtil.setPeriod(period);
        configMap.put(name, collectTimeUtil);
        return collectTimeUtil;
    }

    public static CollectTimeUtil getCollectTimeUtil(String name) {
        CollectTimeUtil collectTimeUtil = configMap.get(name);
        if (collectTimeUtil == null) {
            throw new NoSuchCollectTimeUtilException();
        }
        return collectTimeUtil;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CollectTimeUtil");
        sb.append("{period=").append(period);
        sb.append(", startTime=").append(startTime);
        sb.append('}');
        return sb.toString();
    }
}
