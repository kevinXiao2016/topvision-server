/***********************************************************************
 * $ NoisePerfResult.java,v1.0 2012-5-3 17:17:57 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.Map;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2012-5-3-17:17:57
 */
public class NoisePerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = 7342180206202447869L;

    // private long entityId;
    private long cmcId;
    private Map<Long, Integer> noises;
    private long dt;

    public NoisePerfResult(OperClass domain) {
        super(domain);
    }

    public long getCmcId() {
        return cmcId;
    }

    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    /*
     * public long getEntityId() { return entityId; }
     * 
     * public void setEntityId(long entityId) { this.entityId = entityId; }
     */

    public Map<Long, Integer> getNoises() {
        return noises;
    }

    public void setNoises(Map<Long, Integer> noises) {
        this.noises = noises;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NoisePerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", noises=");
        builder.append(noises);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
