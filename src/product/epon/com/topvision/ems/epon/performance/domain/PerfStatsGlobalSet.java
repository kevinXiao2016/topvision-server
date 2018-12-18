/***********************************************************************
 * $ PerfStatsGlobalSet.java,v1.0 2011-11-21 8:35:15 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-8:35:15
 */
public class PerfStatsGlobalSet implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 15分钟性能统计的最大记录数, 不能是0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.4.1.0", writable = true, type = "Integer32")
    private Integer perfStats15MinMaxRecord;
    /**
     * 24小时性能统计的最大记录数， 不能是0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.4.2.0", writable = true, type = "Integer32")
    private Integer perfStats24HourMaxRecord;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getPerfStats15MinMaxRecord() {
        return perfStats15MinMaxRecord;
    }

    public void setPerfStats15MinMaxRecord(Integer perfStats15MinMaxRecord) {
        this.perfStats15MinMaxRecord = perfStats15MinMaxRecord;
    }

    public Integer getPerfStats24HourMaxRecord() {
        return perfStats24HourMaxRecord;
    }

    public void setPerfStats24HourMaxRecord(Integer perfStats24HourMaxRecord) {
        this.perfStats24HourMaxRecord = perfStats24HourMaxRecord;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfStatsGlobalSet");
        sb.append("{entityId=").append(entityId);
        sb.append(", perfStats15MinMaxRecord=").append(perfStats15MinMaxRecord);
        sb.append(", perfStats24HourMaxRecord=").append(perfStats24HourMaxRecord);
        sb.append('}');
        return sb.toString();
    }
}
