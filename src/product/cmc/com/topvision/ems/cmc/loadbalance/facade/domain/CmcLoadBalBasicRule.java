/***********************************************************************
 * $Id: CmcLoadBalBasicRule.java,v1.0 2013-4-23 下午5:30:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalBasicRuleTpl;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-4-23-下午5:30:06
 *
 */
@Alias("cmcLoadBalBasicRule")
public class CmcLoadBalBasicRule implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = 4292713047742319177L;
    private Long ruleId;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.2.1.1", index = true)
    private Long docsLoadBalBasicRuleId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.2.1.2", writable = true, type = "Integer32")
    private Integer docsLoadBalBasicRuleEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.2.1.3", writable = true, type = "Gauge32")
    private Long docsLoadBalBasicRuleDisStart;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.2.1.4", writable = true, type = "Gauge32")
    private Long docsLoadBalBasicRuleDisPeriod;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.2.1.5", writable = true, type = "Integer32")
    private Integer docsLoadBalBasicRuleRowStatus;

    private String startTime;
    private String endTime;

    public Long getRuleId() {
        return ruleId;
    }
    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    public Long getEntityId() {
        return entityId;
    }
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    public Long getDocsLoadBalBasicRuleId() {
        return docsLoadBalBasicRuleId;
    }
    public void setDocsLoadBalBasicRuleId(Long docsLoadBalBasicRuleId) {
        this.docsLoadBalBasicRuleId = docsLoadBalBasicRuleId;
    }
    public Integer getDocsLoadBalBasicRuleEnable() {
        return docsLoadBalBasicRuleEnable;
    }
    public void setDocsLoadBalBasicRuleEnable(Integer docsLoadBalBasicRuleEnable) {
        this.docsLoadBalBasicRuleEnable = docsLoadBalBasicRuleEnable;
    }
    public Long getDocsLoadBalBasicRuleDisStart() {
        return docsLoadBalBasicRuleDisStart;
    }
    public void setDocsLoadBalBasicRuleDisStart(Long docsLoadBalBasicRuleDisStart) {
        this.docsLoadBalBasicRuleDisStart = docsLoadBalBasicRuleDisStart;
    }
    public Long getDocsLoadBalBasicRuleDisPeriod() {
        return docsLoadBalBasicRuleDisPeriod;
    }
    public void setDocsLoadBalBasicRuleDisPeriod(Long docsLoadBalBasicRuleDisPeriod) {
        this.docsLoadBalBasicRuleDisPeriod = docsLoadBalBasicRuleDisPeriod;
    }
    public Integer getDocsLoadBalBasicRuleRowStatus() {
        return docsLoadBalBasicRuleRowStatus;
    }
    public void setDocsLoadBalBasicRuleRowStatus(Integer docsLoadBalBasicRuleRowStatus) {
        this.docsLoadBalBasicRuleRowStatus = docsLoadBalBasicRuleRowStatus;
    }
    public Long getCmcId() {
        return cmcId;
    }
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        if (docsLoadBalBasicRuleDisStart != null) {
            return CmcLoadBalBasicRuleTpl.toFmtTime(docsLoadBalBasicRuleDisStart.intValue());
        }
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        if (docsLoadBalBasicRuleDisStart != null && docsLoadBalBasicRuleDisPeriod != null) {
            return CmcLoadBalBasicRuleTpl.toFmtTime(docsLoadBalBasicRuleDisStart.intValue()
                    + docsLoadBalBasicRuleDisPeriod.intValue());
        }
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLoadBalBasicRule [ruleId=");
        builder.append(ruleId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", docsLoadBalBasicRuleId=");
        builder.append(docsLoadBalBasicRuleId);
        builder.append(", docsLoadBalBasicRuleEnable=");
        builder.append(docsLoadBalBasicRuleEnable);
        builder.append(", docsLoadBalBasicRuleDisStart=");
        builder.append(docsLoadBalBasicRuleDisStart);
        builder.append(", docsLoadBalBasicRuleDisPeriod=");
        builder.append(docsLoadBalBasicRuleDisPeriod);
        builder.append(", docsLoadBalBasicRuleRowStatus=");
        builder.append(docsLoadBalBasicRuleRowStatus);
        builder.append("]");
        return builder.toString();
    }
}
