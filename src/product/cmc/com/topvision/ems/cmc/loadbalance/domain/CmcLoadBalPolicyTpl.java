/***********************************************************************
 * $Id: CmcLoadBalPolicyTpl.java,v1.0 2013-4-24 下午3:02:36 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.domain.TimeSection;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * CMC负载均衡策略模板
 * @author dengl
 * @created @2013-4-24-下午3:02:36
 *
 */
@Alias("cmcLoadBalPolicyTpl")
public class CmcLoadBalPolicyTpl implements AliasesSuperType{
    private static final long serialVersionUID = -1689823896541728749L;
    private Long policyTplId;
    private String policyName;
    /**
     * 是否进行负载均衡
     * enabled(1) disabled(2) disabledPeriod(3) 
     */
    private Integer policyType;
    private List<CmcLoadBalBasicRuleTpl> rules = new ArrayList<CmcLoadBalBasicRuleTpl>();

    public Long getPolicyTplId() {
        return policyTplId;
    }

    public void setPolicyTplId(Long policyTplId) {
        this.policyTplId = policyTplId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public List<CmcLoadBalBasicRuleTpl> getRules() {
        return rules;
    }

    public void setRules(List<CmcLoadBalBasicRuleTpl> rules) {
        this.rules = rules;
        if (this.rules != null && rules.size() > 0) {
            this.policyType = this.rules.get(0).getDocsLoadBalBasicRuleEnable();
        }
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
     * @return
     */
    public String getDisSections() {
        StringBuffer ret = new StringBuffer();
        if (this.policyType != null && this.policyType.equals(CmcLoadBalBasicRuleTpl.RULE_DISABLED_PERIOD)) {
            List<TimeSection> timeSections = new ArrayList<TimeSection>();
            for (CmcLoadBalBasicRuleTpl r : this.rules) {
                TimeSection ts = new TimeSection();
                ts.setStartTime(r.getDisStartTime());
                ts.setEndTime(r.getDisEndTime());
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
            CmcLoadBalBasicRuleTpl r = this.rules.get(i);
            ret.append(r.getDisStartTime() + "-" + r.getDisEndTime());
            if (i != len - 1) {
                ret.append(",");
            }
        }
        return ret.toString();
    }
}
