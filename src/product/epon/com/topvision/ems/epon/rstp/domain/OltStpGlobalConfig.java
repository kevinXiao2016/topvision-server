/***********************************************************************
 * $Id: OltStpGlobalConfig.java,v1.0 2013-10-28 下午05:08:15 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2013-10-28-下午05:08:15
 *
 */
public class OltStpGlobalConfig implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -1920123955411873046L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.2.16777216", writable = true, type = "Integer32")
    private Integer version;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.3.16777216", writable = true, type = "Integer32")
    private Integer priority;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.4.16777216", type = "Integer32")
    private Long timeSinceTopologyChange;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.5.16777216", type = "Integer32")
    private Long topChanges;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.6.16777216", type = "OctetString")
    private String designatedRoot;
    // 位图处理 8个字节 前两个表示优先级 后六个表示MAC地址
    private String stpGlobalSetDesginatedRootString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.7.16777216", type = "Integer32")
    private Integer rootCost;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.8.16777216", type = "OctetString")
    private String rootPort;
    private String rootPortString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.9.16777216", type = "Integer32")
    private Integer maxAge;
    private String stpGlobalSetMaxAgeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.10.16777216", type = "Integer32")
    private Integer helloTime;
    private String stpGlobalSetHelloTimeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.11.16777216", type = "Integer32")
    private Integer holdTime;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.12.16777216", type = "Integer32")
    private Integer forwardDelay;
    private String stpGlobalSetForwardDelayString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.13.16777216", writable = true, type = "Integer32")
    private Integer bridgeMaxAge;
    private String stpGlobalSetBridgeMaxAgeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.14.16777216", writable = true, type = "Integer32")
    private Integer bridgeHelloTime;
    private String stpGlobalSetBridgeHelloTimeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.15.16777216", writable = true, type = "Integer32")
    private Integer bridgeForwardDelay;
    private String stpGlobalSetBridgeForwardDelayString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.16.16777216", writable = true, type = "Integer32")
    private Integer rstpTxHoldCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.1.1.17.16777216", writable = true, type = "Integer32")
    private Integer enable;

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
     * @return the stpGlobalSetVersion
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     *            the stpGlobalSetVersion to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the stpGlobalSetPriority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            the stpGlobalSetPriority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return the stpGlobalSetTimeSinceTopologyChange
     */
    public Long getTimeSinceTopologyChange() {
        return timeSinceTopologyChange;
    }

    /**
     * @param timeSinceTopologyChange
     *            the stpGlobalSetTimeSinceTopologyChange to set
     */
    public void setTimeSinceTopologyChange(Long timeSinceTopologyChange) {
        this.timeSinceTopologyChange = timeSinceTopologyChange;
    }

    /**
     * @return the stpGlobalSetTopChanges
     */
    public Long getTopChanges() {
        return topChanges;
    }

    /**
     * @param topChanges
     *            the stpGlobalSetTopChanges to set
     */
    public void setTopChanges(Long topChanges) {
        this.topChanges = topChanges;
    }

    /**
     * @return the stpGlobalSetDesignatedRoot
     */
    public String getDesignatedRoot() {
        return designatedRoot;
    }

    /**
     * @param designatedRoot
     *            the stpGlobalSetDesignatedRoot to set
     */
    public void setDesignatedRoot(String designatedRoot) {
        this.designatedRoot = designatedRoot;
        stpGlobalSetDesginatedRootString = EponUtil.getStpRootFromMibString(designatedRoot);
    }

    /**
     * @return the stpGlobalSetDesginatedRootString
     */
    public String getStpGlobalSetDesginatedRootString() {
        return stpGlobalSetDesginatedRootString;
    }

    /**
     * @param stpGlobalSetDesginatedRootString
     *            the stpGlobalSetDesginatedRootString to set
     */
    public void setStpGlobalSetDesginatedRootString(String stpGlobalSetDesginatedRootString) {
        this.stpGlobalSetDesginatedRootString = stpGlobalSetDesginatedRootString;
    }

    /**
     * @return the stpGlobalSetRootCost
     */
    public Integer getRootCost() {
        return rootCost;
    }

    /**
     * @param rootCost
     *            the stpGlobalSetRootCost to set
     */
    public void setRootCost(Integer rootCost) {
        this.rootCost = rootCost;
    }

    /**
     * @return the stpGlobalSetRootPort
     */
    public String getRootPort() {
        return rootPort;
    }

    /**
     * @param rootPort
     *            the stpGlobalSetRootPort to set
     */
    public void setRootPort(String rootPort) {
        this.rootPort = rootPort;
        rootPortString = EponUtil.getRootPort(rootPort);
    }

    /**
     * @return the stpGlobalSetMaxAge
     */
    public Integer getMaxAge() {
        return maxAge;
    }

    /**
     * @param maxAge
     *            the stpGlobalSetMaxAge to set
     */
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
        stpGlobalSetMaxAgeString = String.valueOf(maxAge / 100);
    }

    /**
     * @return the stpGlobalSetHelloTime
     */
    public Integer getHelloTime() {
        return helloTime;
    }

    /**
     * @param helloTime
     *            the stpGlobalSetHelloTime to set
     */
    public void setHelloTime(Integer helloTime) {
        this.helloTime = helloTime;
        stpGlobalSetHelloTimeString = String.valueOf(helloTime / 100);
    }

    /**
     * @return the stpGlobalSetHoldTime
     */
    public Integer getHoldTime() {
        return holdTime;
    }

    /**
     * @param holdTime
     *            the stpGlobalSetHoldTime to set
     */
    public void setHoldTime(Integer holdTime) {
        this.holdTime = holdTime;
    }

    /**
     * @return the stpGlobalSetForwardDelay
     */
    public Integer getForwardDelay() {
        return forwardDelay;
    }

    /**
     * @param forwardDelay
     *            the stpGlobalSetForwardDelay to set
     */
    public void setForwardDelay(Integer forwardDelay) {
        this.forwardDelay = forwardDelay;
        stpGlobalSetForwardDelayString = String.valueOf(forwardDelay / 100);
    }

    /**
     * @return the stpGlobalSetBridgeMaxAge
     */
    public Integer getBridgeMaxAge() {
        return bridgeMaxAge;
    }

    /**
     * @param bridgeMaxAge
     *            the stpGlobalSetBridgeMaxAge to set
     */
    public void setBridgeMaxAge(Integer bridgeMaxAge) {
        this.bridgeMaxAge = bridgeMaxAge;
        stpGlobalSetBridgeMaxAgeString = String.valueOf(bridgeMaxAge / 100);
    }

    /**
     * @return the stpGlobalSetBridgeForwardDelay
     */
    public Integer getBridgeForwardDelay() {
        return bridgeForwardDelay;
    }

    /**
     * @param bridgeForwardDelay
     *            the stpGlobalSetBridgeForwardDelay to set
     */
    public void setBridgeForwardDelay(Integer bridgeForwardDelay) {
        this.bridgeForwardDelay = bridgeForwardDelay;
        stpGlobalSetBridgeForwardDelayString = String.valueOf(bridgeForwardDelay / 100);
    }

    /**
     * @return the stpGlobalSetRstpTxHoldCount
     */
    public Integer getRstpTxHoldCount() {
        return rstpTxHoldCount;
    }

    /**
     * @param rstpTxHoldCount
     *            the stpGlobalSetRstpTxHoldCount to set
     */
    public void setRstpTxHoldCount(Integer rstpTxHoldCount) {
        this.rstpTxHoldCount = rstpTxHoldCount;
    }

    /**
     * @return the stpGlobalSetEnable
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * @param enable
     *            the stpGlobalSetEnable to set
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * @return the stpGlobalSetBridgeHelloTime
     */
    public Integer getBridgeHelloTime() {
        return bridgeHelloTime;
    }

    /**
     * @param bridgeHelloTime
     *            the stpGlobalSetBridgeHelloTime to set
     */
    public void setBridgeHelloTime(Integer bridgeHelloTime) {
        this.bridgeHelloTime = bridgeHelloTime;
        stpGlobalSetBridgeHelloTimeString = String.valueOf(bridgeHelloTime / 100);
    }

    public String getStpGlobalSetMaxAgeString() {
        return stpGlobalSetMaxAgeString;
    }

    public void setStpGlobalSetMaxAgeString(String stpGlobalSetMaxAgeString) {
        this.stpGlobalSetMaxAgeString = stpGlobalSetMaxAgeString;
    }

    public String getStpGlobalSetHelloTimeString() {
        return stpGlobalSetHelloTimeString;
    }

    public void setStpGlobalSetHelloTimeString(String stpGlobalSetHelloTimeString) {
        this.stpGlobalSetHelloTimeString = stpGlobalSetHelloTimeString;
    }

    public String getStpGlobalSetForwardDelayString() {
        return stpGlobalSetForwardDelayString;
    }

    public void setStpGlobalSetForwardDelayString(String stpGlobalSetForwardDelayString) {
        this.stpGlobalSetForwardDelayString = stpGlobalSetForwardDelayString;
    }

    public String getStpGlobalSetBridgeMaxAgeString() {
        return stpGlobalSetBridgeMaxAgeString;
    }

    public void setStpGlobalSetBridgeMaxAgeString(String stpGlobalSetBridgeMaxAgeString) {
        this.stpGlobalSetBridgeMaxAgeString = stpGlobalSetBridgeMaxAgeString;
    }

    public String getStpGlobalSetBridgeHelloTimeString() {
        return stpGlobalSetBridgeHelloTimeString;
    }

    public void setStpGlobalSetBridgeHelloTimeString(String stpGlobalSetBridgeHelloTimeString) {
        this.stpGlobalSetBridgeHelloTimeString = stpGlobalSetBridgeHelloTimeString;
    }

    public String getStpGlobalSetBridgeForwardDelayString() {
        return stpGlobalSetBridgeForwardDelayString;
    }

    public void setStpGlobalSetBridgeForwardDelayString(String stpGlobalSetBridgeForwardDelayString) {
        this.stpGlobalSetBridgeForwardDelayString = stpGlobalSetBridgeForwardDelayString;
    }

    /**
     * @return the rootPortString
     */
    public String getRootPortString() {
        return rootPortString;
    }

    /**
     * @param rootPortString
     *            the rootPortString to set
     */
    public void setRootPortString(String rootPortString) {
        this.rootPortString = rootPortString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltStpGlobalConfig [entityId=");
        builder.append(entityId);
        builder.append(", version=");
        builder.append(version);
        builder.append(", priority=");
        builder.append(priority);
        builder.append(", timeSinceTopologyChange=");
        builder.append(timeSinceTopologyChange);
        builder.append(", topChanges=");
        builder.append(topChanges);
        builder.append(", designatedRoot=");
        builder.append(designatedRoot);
        builder.append(", rootCost=");
        builder.append(rootCost);
        builder.append(", rootPort=");
        builder.append(rootPort);
        builder.append(", maxAge=");
        builder.append(maxAge);
        builder.append(", helloTime=");
        builder.append(helloTime);
        builder.append(", holdTime=");
        builder.append(holdTime);
        builder.append(", forwardDelay=");
        builder.append(forwardDelay);
        builder.append(", bridgeMaxAge=");
        builder.append(bridgeMaxAge);
        builder.append(", bridgeHelloTime=");
        builder.append(bridgeHelloTime);
        builder.append(", bridgeForwardDelay=");
        builder.append(bridgeForwardDelay);
        builder.append(", rstpTxHoldCount=");
        builder.append(rstpTxHoldCount);
        builder.append(", enable=");
        builder.append(enable);
        builder.append("]");
        return builder.toString();
    }
}
