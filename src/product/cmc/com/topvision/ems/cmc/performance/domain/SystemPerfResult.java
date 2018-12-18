/***********************************************************************
 * $Id: SystemPerfResult.java,v1.0 2012-7-11 下午02:02:27 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author dosion
 * @created @2012-7-11-下午02:02:27
 * 
 */
public class SystemPerfResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -1217281354367361858L;

    private long cmcId;
    private CmcAttribute systemPerf;
    private long dt;

    public SystemPerfResult(OperClass domain) {
        super(domain);
    }

    /**
     * @return the cmcId
     */
    public long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the systemPerf
     */
    public CmcAttribute getSystemPerf() {
        return systemPerf;
    }

    /**
     * @param systemPerf
     *            the systemPerf to set
     */
    public void setSystemPerf(CmcAttribute systemPerf) {
        this.systemPerf = systemPerf;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", systemPerf=");
        builder.append(systemPerf);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
