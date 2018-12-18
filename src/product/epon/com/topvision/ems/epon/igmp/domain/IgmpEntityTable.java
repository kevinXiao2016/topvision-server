/***********************************************************************
 * $ IgmpEntityTable.java,v1.0 2011-11-23 8:29:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-8:29:14
 */
public class IgmpEntityTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.1", index = true)
    private Long deviceIndex = 1L;
    /**
     * IGMP工作模式
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.2", writable = true, type = "Integer32")
    private Integer igmpMode;
    /**
     * 最大响应查询时间 单位：1/10秒 This is used for sending general query
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.3", writable = true, type = "Integer32")
    private Integer maxQueryResponseTime;
    /**
     * 健壮性变量 This is used for sending general query No less than 1
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.4", writable = true, type = "Integer32")
    private Integer robustVariable;
    /**
     * 发送查询报文的时间间隔 单位：秒 This is used for sending general query
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.5", writable = true, type = "Integer32")
    private Integer queryInterval;
    /**
     * Max Response Time inserted into Group-Specific Queries sent in response to Leave Group
     * messages, and is also the amount of time between Group-Specific Query messages
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.6", writable = true, type = "Integer32")
    private Integer lastMemberQueryInterval;
    /**
     * Number of Group-Specific Queries sent before the router assumes there are no local members
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.7", writable = true, type = "Integer32")
    private Integer lastMemberQueryCount;
    /**
     * IGMP版本
     */
    // TODO 特殊处理，该字段暂时不可设置
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.8", writable = true, type = "Integer32")
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.6.1.1.8")
    private Integer igmpVersion;

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpMode() {
        return igmpMode;
    }

    public void setIgmpMode(Integer igmpMode) {
        this.igmpMode = igmpMode;
    }

    public Integer getIgmpVersion() {
        return igmpVersion;
    }

    public void setIgmpVersion(Integer igmpVersion) {
        this.igmpVersion = igmpVersion;
    }

    public Integer getLastMemberQueryCount() {
        return lastMemberQueryCount;
    }

    public void setLastMemberQueryCount(Integer lastMemberQueryCount) {
        this.lastMemberQueryCount = lastMemberQueryCount;
    }

    public Integer getLastMemberQueryInterval() {
        return lastMemberQueryInterval;
    }

    public void setLastMemberQueryInterval(Integer lastMemberQueryInterval) {
        this.lastMemberQueryInterval = lastMemberQueryInterval;
    }

    public Integer getMaxQueryResponseTime() {
        return maxQueryResponseTime;
    }

    public void setMaxQueryResponseTime(Integer maxQueryResponseTime) {
        this.maxQueryResponseTime = maxQueryResponseTime;
    }

    public Integer getQueryInterval() {
        return queryInterval;
    }

    public void setQueryInterval(Integer queryInterval) {
        this.queryInterval = queryInterval;
    }

    public Integer getRobustVariable() {
        return robustVariable;
    }

    public void setRobustVariable(Integer robustVariable) {
        this.robustVariable = robustVariable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpEntityTable");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", igmpMode=").append(igmpMode);
        sb.append(", maxQueryResponseTime=").append(maxQueryResponseTime);
        sb.append(", robustVariable=").append(robustVariable);
        sb.append(", queryInterval=").append(queryInterval);
        sb.append(", lastMemberQueryInterval=").append(lastMemberQueryInterval);
        sb.append(", lastMemberQueryCount=").append(lastMemberQueryCount);
        sb.append(", igmpVersion=").append(igmpVersion);
        sb.append('}');
        return sb.toString();
    }
}
