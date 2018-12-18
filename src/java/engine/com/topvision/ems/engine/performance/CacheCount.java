/***********************************************************************
 * $Id: CacheCount.java,v1.0 2016年10月9日 下午5:28:24 $
 * 
 * @author: Victorli
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import java.util.concurrent.atomic.AtomicLong;

import com.topvision.framework.common.DateUtils;

/**
 * @author Victorli
 * @created @2016年10月9日-下午5:28:24
 *
 */
public class CacheCount implements java.io.Serializable {
    private static final long serialVersionUID = 2377337345530061559L;
    private String key;
    private Long time;
    private AtomicLong count;

    public CacheCount(Long time) {
        this.time = time;
        key = DateUtils.hourFormat(time);
        count = new AtomicLong();
    }

    public void increment() {
        count.incrementAndGet();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CacheCount [");
        builder.append(DateUtils.format(time));
        builder.append(" - ");
        builder.append(count);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheCount) {
            return key.equals(((CacheCount) obj).getKey());
        } else {
            return false;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTime() {
        return time;
    }

    public String getDate() {
        return DateUtils.FULL_DATE_FORMAT.format(time);
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public AtomicLong getCount() {
        return count;
    }

    public void setCount(AtomicLong count) {
        this.count = count;
    }
}
