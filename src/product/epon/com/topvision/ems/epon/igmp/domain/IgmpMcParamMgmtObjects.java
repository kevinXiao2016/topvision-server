/***********************************************************************
 * $ IgmpMcParamMgmtObjects.java,v1.0 2011-11-23 9:29:53 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-9:29:53
 * @modify:2012-9-1 15:30:00
 * @modifyUser:lizongtian
 */
@TableProperty(tables = { "default" })
public class IgmpMcParamMgmtObjects implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * max multicast group quantity
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.1.0", writable = true, type = "Integer32")
    private Integer topMcMaxGroupNum;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.2.0", writable = true, type = "Integer32")
    private Long topMcMaxBw;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.3.0", writable = true, type = "Integer32")
    private Integer topMcSnoopingAgingTime;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.4.0", writable = true, type = "OctetString")
    private String topMcMVlan;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.5.0", writable = true, type = "Integer32")
    private Integer topMcVersion;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.6.0", writable = true, type = "Integer32")
    private Integer topMcV3QueryResponseTime;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.7.0", writable = true, type = "Integer32")
    private Integer topMcV2PresentTimeout;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.8.0", writable = true, type = "Integer32")
    private Integer topMcV2PresentTime;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.9.0", writable = true, type = "Integer32")
    private Integer topMcNegotiatedQueryInterval;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.10.0", writable = true, type = "Integer32")
    private Integer topMcNegotiatedQueryRobustness;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.11.0", writable = true, type = "Integer32")
    private Integer topMcCurrentRunningVersion;
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.12.0", writable = true, type = "Integer32")
    private String topMcCdrReportMinIntvl;
    private List<Integer> topMcMVlanList;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopMcMaxGroupNum() {
        return topMcMaxGroupNum;
    }

    public void setTopMcMaxGroupNum(Integer topMcMaxGroupNum) {
        this.topMcMaxGroupNum = topMcMaxGroupNum;
    }

    public Long getTopMcMaxBw() {
        return topMcMaxBw;
    }

    public void setTopMcMaxBw(Long topMcMaxBw) {
        this.topMcMaxBw = topMcMaxBw;
    }

    public Integer getTopMcSnoopingAgingTime() {
        return topMcSnoopingAgingTime;
    }

    public void setTopMcSnoopingAgingTime(Integer topMcSnoopingAgingTime) {
        this.topMcSnoopingAgingTime = topMcSnoopingAgingTime;
    }

    public String getTopMcMVlan() {
        return topMcMVlan;
    }

    public void setTopMcMVlan(String topMcMVlan) {
        this.topMcMVlan = topMcMVlan;
        if (topMcMVlan != null || "".equals(topMcMVlan)) {
            this.topMcMVlanList = EponUtil.getVlanListFromMib(topMcMVlan);
        }
    }

    public List<Integer> getTopMcMVlanList() {
        return topMcMVlanList;
    }

    public void setTopMcMVlanList(List<Integer> topMcMVlanList) {
        this.topMcMVlanList = topMcMVlanList;
        if (topMcMVlanList != null) {
            this.topMcMVlan = EponUtil.getVlanBitMapFormList(topMcMVlanList);
        }
    }

    public Integer getTopMcVersion() {
        return topMcVersion;
    }

    public void setTopMcVersion(Integer topMcVersion) {
        this.topMcVersion = topMcVersion;
    }

    public Integer getTopMcV3QueryResponseTime() {
        return topMcV3QueryResponseTime;
    }

    public void setTopMcV3QueryResponseTime(Integer topMcV3QueryResponseTime) {
        this.topMcV3QueryResponseTime = topMcV3QueryResponseTime;
    }

    public Integer getTopMcV2PresentTimeout() {
        return topMcV2PresentTimeout;
    }

    public void setTopMcV2PresentTimeout(Integer topMcV2PresentTimeout) {
        this.topMcV2PresentTimeout = topMcV2PresentTimeout;
    }

    public Integer getTopMcV2PresentTime() {
        return topMcV2PresentTime;
    }

    public void setTopMcV2PresentTime(Integer topMcV2PresentTime) {
        this.topMcV2PresentTime = topMcV2PresentTime;
    }

    public Integer getTopMcNegotiatedQueryInterval() {
        return topMcNegotiatedQueryInterval;
    }

    public void setTopMcNegotiatedQueryInterval(Integer topMcNegotiatedQueryInterval) {
        this.topMcNegotiatedQueryInterval = topMcNegotiatedQueryInterval;
    }

    public Integer getTopMcNegotiatedQueryRobustness() {
        return topMcNegotiatedQueryRobustness;
    }

    public void setTopMcNegotiatedQueryRobustness(Integer topMcNegotiatedQueryRobustness) {
        this.topMcNegotiatedQueryRobustness = topMcNegotiatedQueryRobustness;
    }

    public Integer getTopMcCurrentRunningVersion() {
        return topMcCurrentRunningVersion;
    }

    public void setTopMcCurrentRunningVersion(Integer topMcCurrentRunningVersion) {
        this.topMcCurrentRunningVersion = topMcCurrentRunningVersion;
    }

    public String getTopMcCdrReportMinIntvl() {
        return topMcCdrReportMinIntvl;
    }

    public void setTopMcCdrReportMinIntvl(String topMcCdrReportMinIntvl) {
        this.topMcCdrReportMinIntvl = topMcCdrReportMinIntvl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpMcParamMgmtObjects");
        sb.append("{entityId=").append(entityId);
        sb.append(", topMcMaxGroupNum=").append(topMcMaxGroupNum);
        sb.append(", topMcMaxBw=").append(topMcMaxBw);
        sb.append(", topMcSnoopingAgingTime=").append(topMcSnoopingAgingTime);
        sb.append(", topMcMVlan=").append(topMcMVlan);
        sb.append('}');
        return sb.toString();
    }
}
