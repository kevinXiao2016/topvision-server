/***********************************************************************
 * $Id: OnuCpeStatusPerfResult.java,v1.0 2012-7-17 上午10:33:43 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.util.List;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2012-7-17-上午10:33:43
 * 
 */
public class OnuCpeStatusPerfResult extends PerformanceResult<OperClass> {
    /**
     * @param domain
     */
    public OnuCpeStatusPerfResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private List<OnuUniCpeList> onuCpes;
    private Boolean arrayEmpty = false;
    private long dt;

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

    public List<OnuUniCpeList> getOnuCpes() {
        return onuCpes;
    }

    public void setOnuCpes(List<OnuUniCpeList> onuCpes) {
        this.onuCpes = onuCpes;
    }

    public Boolean getArrayEmpty() {
        return arrayEmpty;
    }

    public void setArrayEmpty(Boolean arrayEmpty) {
        this.arrayEmpty = arrayEmpty;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuCpeStatusPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", onuCpes=");
        builder.append(onuCpes);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }
}
