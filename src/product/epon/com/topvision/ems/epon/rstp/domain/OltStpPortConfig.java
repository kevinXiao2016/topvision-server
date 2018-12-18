/***********************************************************************
 * $Id: OltStpPortConfig.java,v1.0 2011-12-1 下午03:05:39 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-12-1-下午03:05:39
 * 
 */
public class OltStpPortConfig implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 8288123478891219067L;
    private Long entityId;
    private Long sniIndex;
    private Long sniId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.1", index = true)
    private Long stpPortStpIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.2", index = true)
    private Integer stpPortCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.3", index = true)
    private Integer stpPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.4", type = "Integer32")
    private Integer stpPortStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.5", writable = true, type = "Integer32")
    private Integer stpPortPriority;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.6", writable = true, type = "Integer32")
    private Integer stpPortPathCost;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.7", type = "OctetString")
    private String stpPortDesignatedRoot;
    // 位图处理 8个字节 前两个表示优先级 后六个表示MAC地址
    private String stpPortDesignatedRootString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.8", type = "Integer32")
    private Integer stpPortDesignatedCost;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.9", type = "OctetString")
    private String stpPortDesignatedBridge;
    // 位图处理 8个字节 前两个表示优先级 后六个表示MAC地址
    private String stpPortDesignatedBridgeString;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.10", type = "Integer32")
    private Long stpPortDesignatedPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.11", type = "Integer32")
    private Long stpPortForwardTransitions;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.12", writable = true, type = "Integer32")
    private Integer stpPortRstpProtocolMigration;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.13", writable = true, type = "Integer32")
    private Integer stpPortRstpAdminEdgePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.14", type = "Integer32")
    private Integer stpPortRstpOperEdgePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.15", writable = true, type = "Integer32")
    private Integer stpPortPointToPointAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.16", type = "Integer32")
    private Integer stpPortPointToPointOperStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.9.2.1.17", writable = true, type = "Integer32")
    private Integer stpPortEnabled;

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
     * @return the sniIndex
     */
    public Long getSniIndex() {
        if (sniIndex == null) {
            sniIndex = EponIndex.getPonIndexByMibDeviceIndex(stpPortStpIndex);
        }
        return sniIndex;
    }

    /**
     * @param sniIndex
     *            the sniIndex to set
     */
    public void setSniIndex(Long sniIndex) {
        this.sniIndex = sniIndex;
        // 该方法同样适用于业务口的MIB Index
        stpPortStpIndex = EponIndex.getOnuMibIndexByIndex(sniIndex);
        stpPortCardIndex = 1;
        stpPortIndex = 0;
    }

    /**
     * @return the sniId
     */
    public Long getSniId() {
        return sniId;
    }

    /**
     * @param sniId
     *            the sniId to set
     */
    public void setSniId(Long sniId) {
        this.sniId = sniId;
    }

    /**
     * @return the stpPortStpIndex
     */
    public Long getStpPortStpIndex() {
        return stpPortStpIndex;
    }

    /**
     * @param stpPortStpIndex
     *            the stpPortStpIndex to set
     */
    public void setStpPortStpIndex(Long stpPortStpIndex) {
        this.stpPortStpIndex = stpPortStpIndex;
    }

    /**
     * @return the stpPortCardIndex
     */
    public Integer getStpPortCardIndex() {
        return stpPortCardIndex;
    }

    /**
     * @param stpPortCardIndex
     *            the stpPortCardIndex to set
     */
    public void setStpPortCardIndex(Integer stpPortCardIndex) {
        this.stpPortCardIndex = stpPortCardIndex;
    }

    /**
     * @return the stpPortIndex
     */
    public Integer getStpPortIndex() {
        return stpPortIndex;
    }

    /**
     * @param stpPortIndex
     *            the stpPortIndex to set
     */
    public void setStpPortIndex(Integer stpPortIndex) {
        this.stpPortIndex = stpPortIndex;
    }

    /**
     * @return the stpPortStatus
     */
    public Integer getStpPortStatus() {
        return stpPortStatus;
    }

    /**
     * @param stpPortStatus
     *            the stpPortStatus to set
     */
    public void setStpPortStatus(Integer stpPortStatus) {
        this.stpPortStatus = stpPortStatus;
    }

    /**
     * @return the stpPortPriority
     */
    public Integer getStpPortPriority() {
        return stpPortPriority;
    }

    /**
     * @param stpPortPriority
     *            the stpPortPriority to set
     */
    public void setStpPortPriority(Integer stpPortPriority) {
        this.stpPortPriority = stpPortPriority;
    }

    /**
     * @return the stpPortPathCost
     */
    public Integer getStpPortPathCost() {
        return stpPortPathCost;
    }

    /**
     * @param stpPortPathCost
     *            the stpPortPathCost to set
     */
    public void setStpPortPathCost(Integer stpPortPathCost) {
        this.stpPortPathCost = stpPortPathCost;
    }

    /**
     * @return the stpPortDesignatedRoot
     */
    public String getStpPortDesignatedRoot() {
        return stpPortDesignatedRoot;
    }

    /**
     * @param stpPortDesignatedRoot
     *            the stpPortDesignatedRoot to set
     */
    public void setStpPortDesignatedRoot(String stpPortDesignatedRoot) {
        this.stpPortDesignatedRoot = stpPortDesignatedRoot;
        stpPortDesignatedRootString = EponUtil.getStpRootFromMibString(stpPortDesignatedRoot);
    }

    /**
     * @return the stpPortDesignatedCost
     */
    public Integer getStpPortDesignatedCost() {
        return stpPortDesignatedCost;
    }

    /**
     * @param stpPortDesignatedCost
     *            the stpPortDesignatedCost to set
     */
    public void setStpPortDesignatedCost(Integer stpPortDesignatedCost) {
        this.stpPortDesignatedCost = stpPortDesignatedCost;
    }

    /**
     * @return the stpPortDesignatedBridge
     */
    public String getStpPortDesignatedBridge() {
        return stpPortDesignatedBridge;
    }

    /**
     * @param stpPortDesignatedBridge
     *            the stpPortDesignatedBridge to set
     */
    public void setStpPortDesignatedBridge(String stpPortDesignatedBridge) {
        this.stpPortDesignatedBridge = stpPortDesignatedBridge;
        stpPortDesignatedBridgeString = EponUtil.getStpRootFromMibString(stpPortDesignatedBridge);
    }

    /**
     * @return the stpPortDesignatedPort
     */
    public Long getStpPortDesignatedPort() {
        return stpPortDesignatedPort;
    }

    /**
     * @param stpPortDesignatedPort
     *            the stpPortDesignatedPort to set
     */
    public void setStpPortDesignatedPort(Long stpPortDesignatedPort) {
        this.stpPortDesignatedPort = stpPortDesignatedPort;
    }

    /**
     * @return the stpPortForwardTransitions
     */
    public Long getStpPortForwardTransitions() {
        return stpPortForwardTransitions;
    }

    /**
     * @param stpPortForwardTransitions
     *            the stpPortForwardTransitions to set
     */
    public void setStpPortForwardTransitions(Long stpPortForwardTransitions) {
        this.stpPortForwardTransitions = stpPortForwardTransitions;
    }

    /**
     * @return the stpPortRstpProtocolMigration
     */
    public Integer getStpPortRstpProtocolMigration() {
        return stpPortRstpProtocolMigration;
    }

    /**
     * @param stpPortRstpProtocolMigration
     *            the stpPortRstpProtocolMigration to set
     */
    public void setStpPortRstpProtocolMigration(Integer stpPortRstpProtocolMigration) {
        this.stpPortRstpProtocolMigration = stpPortRstpProtocolMigration;
    }

    /**
     * @return the stpPortRstpAdminEdgePort
     */
    public Integer getStpPortRstpAdminEdgePort() {
        return stpPortRstpAdminEdgePort;
    }

    /**
     * @param stpPortRstpAdminEdgePort
     *            the stpPortRstpAdminEdgePort to set
     */
    public void setStpPortRstpAdminEdgePort(Integer stpPortRstpAdminEdgePort) {
        this.stpPortRstpAdminEdgePort = stpPortRstpAdminEdgePort;
    }

    /**
     * @return the stpPortRstpOperEdgePort
     */
    public Integer getStpPortRstpOperEdgePort() {
        return stpPortRstpOperEdgePort;
    }

    /**
     * @param stpPortRstpOperEdgePort
     *            the stpPortRstpOperEdgePort to set
     */
    public void setStpPortRstpOperEdgePort(Integer stpPortRstpOperEdgePort) {
        this.stpPortRstpOperEdgePort = stpPortRstpOperEdgePort;
    }

    /**
     * @return the stpPortPointToPointAdminStatus
     */
    public Integer getStpPortPointToPointAdminStatus() {
        return stpPortPointToPointAdminStatus;
    }

    /**
     * @param stpPortPointToPointAdminStatus
     *            the stpPortPointToPointAdminStatus to set
     */
    public void setStpPortPointToPointAdminStatus(Integer stpPortPointToPointAdminStatus) {
        this.stpPortPointToPointAdminStatus = stpPortPointToPointAdminStatus;
    }

    /**
     * @return the stpPortPointToPointOperStatus
     */
    public Integer getStpPortPointToPointOperStatus() {
        return stpPortPointToPointOperStatus;
    }

    /**
     * @param stpPortPointToPointOperStatus
     *            the stpPortPointToPointOperStatus to set
     */
    public void setStpPortPointToPointOperStatus(Integer stpPortPointToPointOperStatus) {
        this.stpPortPointToPointOperStatus = stpPortPointToPointOperStatus;
    }

    /**
     * @return the stpPortEnabled
     */
    public Integer getStpPortEnabled() {
        return stpPortEnabled;
    }

    /**
     * @param stpPortEnabled
     *            the stpPortEnabled to set
     */
    public void setStpPortEnabled(Integer stpPortEnabled) {
        this.stpPortEnabled = stpPortEnabled;
    }

    /**
     * @return the stpPortDesignatedRootString
     */
    public String getStpPortDesignatedRootString() {
        return stpPortDesignatedRootString;
    }

    /**
     * @param stpPortDesignatedRootString
     *            the stpPortDesignatedRootString to set
     */
    public void setStpPortDesignatedRootString(String stpPortDesignatedRootString) {
        this.stpPortDesignatedRootString = stpPortDesignatedRootString;
    }

    /**
     * @return the stpPortDesignatedBridgeString
     */
    public String getStpPortDesignatedBridgeString() {
        return stpPortDesignatedBridgeString;
    }

    /**
     * @param stpPortDesignatedBridgeString
     *            the stpPortDesignatedBridgeString to set
     */
    public void setStpPortDesignatedBridgeString(String stpPortDesignatedBridgeString) {
        this.stpPortDesignatedBridgeString = stpPortDesignatedBridgeString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltStpPortConfig [entityId=");
        builder.append(entityId);
        builder.append(", sniIndex=");
        builder.append(sniIndex);
        builder.append(", sniId=");
        builder.append(sniId);
        builder.append(", stpPortStpIndex=");
        builder.append(stpPortStpIndex);
        builder.append(", stpPortCardIndex=");
        builder.append(stpPortCardIndex);
        builder.append(", stpPortIndex=");
        builder.append(stpPortIndex);
        builder.append(", stpPortStatus=");
        builder.append(stpPortStatus);
        builder.append(", stpPortPriority=");
        builder.append(stpPortPriority);
        builder.append(", stpPortPathCost=");
        builder.append(stpPortPathCost);
        builder.append(", stpPortDesignatedRoot=");
        builder.append(stpPortDesignatedRoot);
        builder.append(", stpPortDesignatedCost=");
        builder.append(stpPortDesignatedCost);
        builder.append(", stpPortDesignatedBridge=");
        builder.append(stpPortDesignatedBridge);
        builder.append(", stpPortDesignatedPort=");
        builder.append(stpPortDesignatedPort);
        builder.append(", stpPortForwardTransitions=");
        builder.append(stpPortForwardTransitions);
        builder.append(", stpPortRstpProtocolMigration=");
        builder.append(stpPortRstpProtocolMigration);
        builder.append(", stpPortRstpAdminEdgePort=");
        builder.append(stpPortRstpAdminEdgePort);
        builder.append(", stpPortRstpOperEdgePort=");
        builder.append(stpPortRstpOperEdgePort);
        builder.append(", stpPortPointToPointAdminStatus=");
        builder.append(stpPortPointToPointAdminStatus);
        builder.append(", stpPortPointToPointOperStatus=");
        builder.append(stpPortPointToPointOperStatus);
        builder.append(", stpPortEnabled=");
        builder.append(stpPortEnabled);
        builder.append("]");
        return builder.toString();
    }

}
