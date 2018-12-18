/***********************************************************************
 * $ PerfStatOLTCycle.java,v1.0 2011-11-21 8:22:12 $
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
 * @created @2011-11-21-8:22:12
 */
public class PerfStatCycle implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * OLT performace statistic time interval. unit: seconds
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.1.1.0", writable = true, type = "Integer32")
    private Integer topPerfStatOLTCycle;
    /**
     * ONU performace statistic time interval. unit: seconds
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.1.2.0", writable = true, type = "Integer32")
    private Integer topPerfStatONUCycle;
    /**
     * OLT temperature sampling time interval. unit: seconds
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.1.3.0", writable = true, type = "Integer32")
    private Integer topPerfOLTTemperatureCycle;
    /**
     * ONU temperature sampling time interval. unit: seconds
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.1.4.0", writable = true, type = "Integer32")
    private Integer topPerfONUTemperatureCycle;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopPerfOLTTemperatureCycle() {
        return topPerfOLTTemperatureCycle;
    }

    public void setTopPerfOLTTemperatureCycle(Integer topPerfOLTTemperatureCycle) {
        this.topPerfOLTTemperatureCycle = topPerfOLTTemperatureCycle;
    }

    public Integer getTopPerfONUTemperatureCycle() {
        return topPerfONUTemperatureCycle;
    }

    public void setTopPerfONUTemperatureCycle(Integer topPerfONUTemperatureCycle) {
        this.topPerfONUTemperatureCycle = topPerfONUTemperatureCycle;
    }

    public Integer getTopPerfStatOLTCycle() {
        return topPerfStatOLTCycle;
    }

    public void setTopPerfStatOLTCycle(Integer topPerfStatOLTCycle) {
        this.topPerfStatOLTCycle = topPerfStatOLTCycle;
    }

    public Integer getTopPerfStatONUCycle() {
        return topPerfStatONUCycle;
    }

    public void setTopPerfStatONUCycle(Integer topPerfStatONUCycle) {
        this.topPerfStatONUCycle = topPerfStatONUCycle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfStatCycle");
        sb.append("{entityId=").append(entityId);
        sb.append(", topPerfStatOLTCycle=").append(topPerfStatOLTCycle);
        sb.append(", topPerfStatONUCycle=").append(topPerfStatONUCycle);
        sb.append(", topPerfOLTTemperatureCycle=").append(topPerfOLTTemperatureCycle);
        sb.append(", topPerfONUTemperatureCycle=").append(topPerfONUTemperatureCycle);
        sb.append('}');
        return sb.toString();
    }
}
