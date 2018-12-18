/***********************************************************************
 * $Id: UplinkSpeedStaticPerfResult.java,v1.0 2012-7-15 上午11:22:50 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.domain;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmts.facade.domain.UplinkSpeedPerf;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @2012-7-15-上午11:22:50
 */
public class UplinkSpeedStaticPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -6111725726747306725L;
    private Long entityId;
    private List<UplinkSpeedPerf> uplinkSpeedPerf;
    private long dt;

    /**
     * @param domain
     */
    public UplinkSpeedStaticPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the cmcId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcPortPerfs
     */
    public List<UplinkSpeedPerf> getUplinkSpeedPerf() {
        return uplinkSpeedPerf;
    }

    /**
     * @param uplinkSpeedPerf the cmcPortPerfs to set
     */
    public void setUplinkSpeedPerf(List<UplinkSpeedPerf> uplinkSpeedPerf) {
        this.uplinkSpeedPerf = uplinkSpeedPerf;
    }

    /**
     * @return the dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     * 添加 信道速率统计信息
     *
     * @param perf 信道统计信息
     */
    public void addUplinkSpeedPerf(UplinkSpeedPerf perf) {
        if (uplinkSpeedPerf == null) {
            uplinkSpeedPerf = new ArrayList<UplinkSpeedPerf>();
        }
        uplinkSpeedPerf.add(perf);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UplinkSpeedStaticPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", uplinkSpeedPerf=");
        builder.append(uplinkSpeedPerf);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}