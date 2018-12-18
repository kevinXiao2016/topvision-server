/***********************************************************************
 * $Id: CmStatusPerfResult.java,v1.0 2012-7-17 上午10:33:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author jay
 * @created @2012-7-17-上午10:33:43
 *
 */
public class CpeStatusPerfResult extends PerformanceResult<OperClass> {
    /**
     * @param domain domain
     */
    public CpeStatusPerfResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private Long entityId;
    private List<TopCpeAttribute> cpeAttributes;
    private long dt;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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
     * 添加 信道cm统计信息
     *
     * @param cpeAttribute
     *            信道统计信息
     */
    public void addCmAttribute(TopCpeAttribute cpeAttribute) {
        if (cpeAttributes == null) {
            cpeAttributes = new ArrayList<TopCpeAttribute>();
        }
        cpeAttributes.add(cpeAttribute);
    }

    public List<TopCpeAttribute> getCpeAttributes() {
        return cpeAttributes;
    }

    public void setCpeAttributes(List<TopCpeAttribute> cpeAttributes) {
        this.cpeAttributes = cpeAttributes;
    }/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmStatusPerfResult [entityId=");
        builder.append(entityId);
        builder.append(", cpeAttributes=");
        builder.append(cpeAttributes);
        builder.append(", dt=");
        builder.append(dt);
        builder.append("]");
        return builder.toString();
    }

}