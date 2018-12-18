/***********************************************************************
 * $Id: CmcLoadBalPolicy.java,v1.0 2013-4-23 下午5:34:08 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.domain.TimeSection;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalBasicRuleTpl;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-4-23-下午5:34:08
 *
 */
@Alias("cmcLoadBalPolicy")
public class CmcLoadBalPolicy implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1319918762490340293L;
    private Long policyId;
    /**
     * 是否进行负载均衡
     * enabled(1) disabled(2) disabledPeriod(3) 
     */
    private Integer policyType;
    private Long entityId;
    private Long cmcId;
    private Long ruleId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.1.1.1", index = true)
    private Long docsLoadBalPolicyId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.1.1.2", index = true)
    private Long docsLoadBalPolicyRuleId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.4.1.1.5", writable = true, type = "Integer32")
    private Integer docsLoadBalPolicyRowStatus;
    private List<CmcLoadBalBasicRule> rules = new ArrayList<CmcLoadBalBasicRule>();
    private Integer LBEnabled;
    private Long topLoadBalPolicyId;

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getDocsLoadBalPolicyId() {
        return docsLoadBalPolicyId;
    }

    public void setDocsLoadBalPolicyId(Long docsLoadBalPolicyId) {
        this.docsLoadBalPolicyId = docsLoadBalPolicyId;
    }

    public Long getDocsLoadBalPolicyRuleId() {
        return docsLoadBalPolicyRuleId;
    }

    public void setDocsLoadBalPolicyRuleId(Long docsLoadBalPolicyRuleId) {
        this.docsLoadBalPolicyRuleId = docsLoadBalPolicyRuleId;
    }

    public Integer getDocsLoadBalPolicyRowStatus() {
        return docsLoadBalPolicyRowStatus;
    }

    public void setDocsLoadBalPolicyRowStatus(Integer docsLoadBalPolicyRowStatus) {
        this.docsLoadBalPolicyRowStatus = docsLoadBalPolicyRowStatus;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the rules
     */
    public List<CmcLoadBalBasicRule> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<CmcLoadBalBasicRule> rules) {
        this.rules = rules;
        if (this.rules != null && this.rules.size() > 0) {
            this.policyType = this.rules.get(0).getDocsLoadBalBasicRuleEnable();
        }
    }

    /**
     * @return the lBEnabled
     */
    public Integer getLBEnabled() {
        return LBEnabled;
    }

    /**
     * @param lBEnabled the lBEnabled to set
     */
    public void setLBEnabled(Integer lBEnabled) {
        LBEnabled = lBEnabled;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    /**
     * 返回无效的时间段，格式为  08:30-09:30,11:30-15:00...
     * 主要用于在页面渲染时间段
     * 此数据经过了合并和去重叠操作
     * @return
     */
    public String getDisSections() {
        StringBuffer ret = new StringBuffer();
        if (this.policyType != null && this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
            List<TimeSection> timeSections = new ArrayList<TimeSection>();
            for (CmcLoadBalBasicRule r : this.rules) {
                TimeSection ts = new TimeSection();
                ts.setStartTime(r.getStartTime());
                ts.setEndTime(r.getEndTime());
                timeSections.add(ts);
            }

            timeSections = TimeSection.mergeTimeSections(timeSections);
            int len = timeSections.size();
            for (int i = 0; i < len; i++) {
                TimeSection ts = timeSections.get(i);
                ret.append(ts.getStartTime() + "-" + ts.getEndTime());

                if (i != len - 1) {
                    ret.append(",");
                }
            }
        }

        return ret.toString();
    }

    /**
     * 返回原始的无效时间段，未经过整理，格式 08:30-09:30,11:30-15:00...
     * @return
     */
    public String getOriginalDisSections() {
        StringBuffer ret = new StringBuffer();
        int len = this.rules != null ? this.rules.size() : 0;
        for (int i = 0; i < len; i++) {
            CmcLoadBalBasicRule r = this.rules.get(i);
            ret.append(r.getStartTime() + "-" + r.getEndTime());
            if (i != len - 1) {
                ret.append(",");
            }
        }
        return ret.toString();
    }

    public Long getTopLoadBalPolicyId() {
        return topLoadBalPolicyId;
    }

    public void setTopLoadBalPolicyId(Long topLoadBalPolicyId) {
        this.topLoadBalPolicyId = topLoadBalPolicyId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcLoadBalPolicy [policyId=");
        builder.append(policyId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", ruleId=");
        builder.append(ruleId);
        builder.append(", docsLoadBalPolicyId=");
        builder.append(docsLoadBalPolicyId);
        builder.append(", docsLoadBalPolicyRuleId=");
        builder.append(docsLoadBalPolicyRuleId);
        builder.append(", docsLoadBalPolicyRowStatus=");
        builder.append(docsLoadBalPolicyRowStatus);
        builder.append("]");
        return builder.toString();
    }
}
