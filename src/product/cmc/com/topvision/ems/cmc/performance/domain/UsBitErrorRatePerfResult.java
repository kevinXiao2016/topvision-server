/***********************************************************************
 * $Id: UsBitErrorRatePerfResult.java,v1.0 2012-7-12 下午04:07:34 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author loyal
 * @created @2012-7-12-下午04:07:34
 * 
 */
public class UsBitErrorRatePerfResult extends PerformanceResult<OperClass> {
    /**
     */
    public UsBitErrorRatePerfResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = 7839241277065524824L;
    private Long cmcId;
    private List<UsBitErrorRatePerfDomain> usBitErrorRatePerfDomains;
    private long dt;

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
     * @return the cmcUpChannelSignalQualityInfos
     */
    public List<UsBitErrorRatePerfDomain> getUsBitErrorRatePerfDomains() {
        return usBitErrorRatePerfDomains;
    }

    /**
     * @param usBitErrorRatePerfDomains
     *            the cmcUpChannelSignalQualityInfos to set
     */
    public void setUsBitErrorRatePerfDomains(List<UsBitErrorRatePerfDomain> usBitErrorRatePerfDomains) {
        this.usBitErrorRatePerfDomains = usBitErrorRatePerfDomains;
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
     * 添加 信道质量信息
     * 
     * @param usBitErrorRatePerfDomain
     *            信道质量信息
     */
    public void addUsBitErrorRatePerfDomain(UsBitErrorRatePerfDomain usBitErrorRatePerfDomain) {
        if (usBitErrorRatePerfDomains == null) {
            usBitErrorRatePerfDomains = new ArrayList<UsBitErrorRatePerfDomain>();
        }
        usBitErrorRatePerfDomains.add(usBitErrorRatePerfDomain);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsBitErrorRatePerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", usBitErrorRatePerfDomains=");
        builder.append(usBitErrorRatePerfDomains);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
