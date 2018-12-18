/***********************************************************************
 * $ CollectTimeRange.java,v1.0 2013-6-20 20:23:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2013-6-20-20:23:13
 */
@Alias("collectTimeRange")
public class CollectTimeRange implements AliasesSuperType{
    private Long startTimeLong;
    private Long endTimeLong;

    public Long getEndTimeLong() {
        return endTimeLong;
    }

    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }

    public Long getStartTimeLong() {
        return startTimeLong;
    }

    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }

    public Timestamp getEndTime() {
        return new Timestamp(endTimeLong);
    }

    public void setEndTime(Timestamp endTime) {
        this.endTimeLong = endTime.getTime();
    }

    public Timestamp getStartTime() {
        return new Timestamp(startTimeLong);
    }

    public void setStartTime(Timestamp startTime) {
        this.startTimeLong = startTime.getTime();
    }
}
