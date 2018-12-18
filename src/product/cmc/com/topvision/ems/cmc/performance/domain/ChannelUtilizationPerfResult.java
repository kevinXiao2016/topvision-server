/***********************************************************************
 * $Id: ChannelUtilizationPerfResult.java,v1.0 2012-5-8 下午01:40:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.Map;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author loyal
 * @created @2012-5-8-下午01:40:45
 * 
 */
public class ChannelUtilizationPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -2464679791047372178L;
    private Long cmcId;
    private Map<Long, Integer> utilizations;
    private long dt;

    /**
     * @param domain
     */
    public ChannelUtilizationPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     * @return the channelUtilizations
     */
    /**
     * @return the utilizations
     */
    public Map<Long, Integer> getUtilizations() {
        return utilizations;
    }

    /**
     * @param utilizations
     *            the utilizations to set
     */
    public void setUtilizations(Map<Long, Integer> utilizations) {
        this.utilizations = utilizations;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelUtilizationPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", utilizations=");
        builder.append(utilizations);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
