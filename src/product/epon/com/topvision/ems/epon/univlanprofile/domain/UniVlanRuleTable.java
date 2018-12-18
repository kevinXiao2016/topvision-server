/***********************************************************************
* $Id: UniVlanRuleTable.java,v1.0 2013-11-28 上午10:20:26 $
* 
* @author: flack
* 
* (c)Copyright 2011 Topvision All rights reserved.
***********************************************************************/
package com.topvision.ems.epon.univlanprofile.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2013-11-28-上午10:20:26
 *
 */
public class UniVlanRuleTable implements AliasesSuperType {
    private static final long serialVersionUID = -98554871396075692L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.1", index = true)
    private Integer ruleProfileIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.2", index = true)
    private Integer ruleIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.3", writable = true, type = "Integer32")
    private Integer ruleMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.4", writable = true, type = "OctetString")
    private String ruleCvlan;
    private List<Integer> cVlanList;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.5", writable = true, type = "Integer32")
    private Integer ruleSvlan;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.7.2.1.6", writable = true, type = "Integer32")
    private Integer ruleRowstatus;
    //用于维护在网管端数据库和前台页面数据展示
    private String cVlanData;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getRuleProfileIndex() {
        return ruleProfileIndex;
    }

    public void setRuleProfileIndex(Integer ruleProfileIndex) {
        this.ruleProfileIndex = ruleProfileIndex;
    }

    public Integer getRuleIndex() {
        return ruleIndex;
    }

    public void setRuleIndex(Integer ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    public Integer getRuleMode() {
        return ruleMode;
    }

    public void setRuleMode(Integer ruleMode) {
        this.ruleMode = ruleMode;
    }

    public String getRuleCvlan() {
        return ruleCvlan;
    }

    public void setRuleCvlan(String ruleCvlan) {
        this.ruleCvlan = ruleCvlan;
        cVlanList = EponUtil.getTwiceBitValueFromOcterString(ruleCvlan);
        StringBuilder sBuilder = new StringBuilder();
        for (Integer vlan : cVlanList) {
            if (vlan.intValue() != 0) {
                sBuilder.append(vlan).append(",");
            } else {
                break;
            }
        }
        cVlanData = sBuilder.substring(0, sBuilder.length() - 1);
    }

    public Integer getRuleSvlan() {
        return ruleSvlan;
    }

    public void setRuleSvlan(Integer ruleSvlan) {
        this.ruleSvlan = ruleSvlan;
    }

    public Integer getRuleRowstatus() {
        return ruleRowstatus;
    }

    public void setRuleRowstatus(Integer ruleRowstatus) {
        this.ruleRowstatus = ruleRowstatus;
    }

    public List<Integer> getcVlanList() {
        return cVlanList;
    }

    public void setcVlanList(List<Integer> cVlanList) {
        this.cVlanList = cVlanList;
        this.ruleCvlan = this.getOcterStringFormatCvlan(cVlanList);
    }

    //将cVlan转换为16字节表示法
    private String getOcterStringFormatCvlan(List<Integer> list) {
        while (list.size() < 8) {
            list.add(0);
        }
        StringBuilder sBuilder = new StringBuilder();
        for (Integer i : list) {
            String s = EponUtil.flushLeft('0', 4, Integer.toHexString(i));
            sBuilder.append(s.substring(0, 2)).append(":").append(s.substring(2, 4)).append(":");

        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    public String getcVlanData() {
        return cVlanData;
    }

    public void setcVlanData(String cVlanData) {
        this.cVlanData = cVlanData;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UniVlanRuleTable [entityId=");
        builder.append(entityId);
        builder.append(", ruleProfileIndex=");
        builder.append(ruleProfileIndex);
        builder.append(", ruleIndex=");
        builder.append(ruleIndex);
        builder.append(", ruleMode=");
        builder.append(ruleMode);
        builder.append(", ruleCvlan=");
        builder.append(ruleCvlan);
        builder.append(", cVlanList=");
        builder.append(cVlanList);
        builder.append(", ruleSvlan=");
        builder.append(ruleSvlan);
        builder.append(", ruleRowstatus=");
        builder.append(ruleRowstatus);
        builder.append(", cVlanData=");
        builder.append(cVlanData);
        builder.append("]");
        return builder.toString();
    }

}
