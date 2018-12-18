/***********************************************************************
 * $Id: OltDhcpIpMacStatic.java,v1.0 2011-11-17 下午12:04:21 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-17-下午12:04:21
 * 
 */
public class OltDhcpIpMacStatic implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 6491967186085794379L;
    private Long entityId;
    private String onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.4.1.1", index = true)
    private Integer topOltDHCPIpMacIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.4.1.2", writable = true, type = "IpAddress")
    private String topOltDHCPIpAddr;
    private Long topOltDHCPIpAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.4.1.3", writable = true, type = "OctetString")
    private String topOltDHCPMacAddr;
    private Long topOltDHCPMacAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.4.1.4", writable = true, type = "OctetString")
    private String topOltDHCPOnuMacAddr;
    private Long topOltDHCPOnuMacAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.4.1.5", writable = true, type = "Integer32")
    private Integer topOltDHCPIpMacRowStatus;

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
     * @return the onuType
     */
    public String getOnuType() {
        return onuType;
    }

    /**
     * @param onuType
     *            the onuType to set
     */
    public void setOnuType(String onuType) {
        this.onuType = onuType;
    }

    /**
     * @return the topOltDHCPIpMacIdx
     */
    public Integer getTopOltDHCPIpMacIdx() {
        return topOltDHCPIpMacIdx;
    }

    /**
     * @param topOltDHCPIpMacIdx
     *            the topOltDHCPIpMacIdx to set
     */
    public void setTopOltDHCPIpMacIdx(Integer topOltDHCPIpMacIdx) {
        this.topOltDHCPIpMacIdx = topOltDHCPIpMacIdx;
    }

    /**
     * @return the topOltDHCPIpAddr
     */
    public String getTopOltDHCPIpAddr() {
        return topOltDHCPIpAddr;
    }

    /**
     * @param topOltDHCPIpAddr
     *            the topOltDHCPIpAddr to set
     */
    public void setTopOltDHCPIpAddr(String topOltDHCPIpAddr) {
        this.topOltDHCPIpAddr = topOltDHCPIpAddr;
        topOltDHCPIpAddrLong = new IpUtils(topOltDHCPIpAddr).longValue();
    }

    /**
     * @return the topOltDHCPMacAddr
     */
    public String getTopOltDHCPMacAddr() {
        return topOltDHCPMacAddr;
    }

    /**
     * @param topOltDHCPMacAddr
     *            the topOltDHCPMacAddr to set
     */
    public void setTopOltDHCPMacAddr(String topOltDHCPMacAddr) {
        this.topOltDHCPMacAddr = EponUtil.getMacStringFromNoISOControl(topOltDHCPMacAddr);
        topOltDHCPMacAddrLong = new MacUtils(this.topOltDHCPMacAddr).longValue();
    }

    /**
     * @return the topOltDHCPOnuMacAddr
     */
    public String getTopOltDHCPOnuMacAddr() {
        return topOltDHCPOnuMacAddr;
    }

    /**
     * @param topOltDHCPOnuMacAddr
     *            the topOltDHCPOnuMacAddr to set
     */
    public void setTopOltDHCPOnuMacAddr(String topOltDHCPOnuMacAddr) {
        this.topOltDHCPOnuMacAddr = EponUtil.getMacStringFromNoISOControl(topOltDHCPOnuMacAddr);
        topOltDHCPOnuMacAddrLong = new MacUtils(this.topOltDHCPOnuMacAddr).longValue();
    }

    /**
     * @return the topOltDHCPIpMacRowStatus
     */
    public Integer getTopOltDHCPIpMacRowStatus() {
        return topOltDHCPIpMacRowStatus;
    }

    /**
     * @param topOltDHCPIpMacRowStatus
     *            the topOltDHCPIpMacRowStatus to set
     */
    public void setTopOltDHCPIpMacRowStatus(Integer topOltDHCPIpMacRowStatus) {
        this.topOltDHCPIpMacRowStatus = topOltDHCPIpMacRowStatus;
    }

    /**
     * @return the topOltDHCPIpAddrLong
     */
    public Long getTopOltDHCPIpAddrLong() {
        return topOltDHCPIpAddrLong;
    }

    /**
     * @param topOltDHCPIpAddrLong
     *            the topOltDHCPIpAddrLong to set
     */
    public void setTopOltDHCPIpAddrLong(Long topOltDHCPIpAddrLong) {
        this.topOltDHCPIpAddrLong = topOltDHCPIpAddrLong;
        topOltDHCPIpAddr = new IpUtils(topOltDHCPIpAddrLong).toString();
    }

    /**
     * @return the topOltDHCPMacAddrLong
     */
    public Long getTopOltDHCPMacAddrLong() {
        return topOltDHCPMacAddrLong;
    }

    /**
     * @param topOltDHCPMacAddrLong
     *            the topOltDHCPMacAddrLong to set
     */
    public void setTopOltDHCPMacAddrLong(Long topOltDHCPMacAddrLong) {
        this.topOltDHCPMacAddrLong = topOltDHCPMacAddrLong;
        topOltDHCPMacAddr = new MacUtils(topOltDHCPMacAddrLong).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /**
     * @return the topOltDHCPOnuMacAddrLong
     */
    public Long getTopOltDHCPOnuMacAddrLong() {
        return topOltDHCPOnuMacAddrLong;
    }

    /**
     * @param topOltDHCPOnuMacAddrLong
     *            the topOltDHCPOnuMacAddrLong to set
     */
    public void setTopOltDHCPOnuMacAddrLong(Long topOltDHCPOnuMacAddrLong) {
        this.topOltDHCPOnuMacAddrLong = topOltDHCPOnuMacAddrLong;
        topOltDHCPOnuMacAddr = new MacUtils(topOltDHCPOnuMacAddrLong).toString(MacUtils.MAOHAO).toUpperCase();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDhcpIpMacStatic [entityId=");
        builder.append(entityId);
        builder.append(", onuType=");
        builder.append(onuType);
        builder.append(", topOltDHCPIpMacIdx=");
        builder.append(topOltDHCPIpMacIdx);
        builder.append(", topOltDHCPIpAddr=");
        builder.append(topOltDHCPIpAddr);
        builder.append(", topOltDHCPMacAddr=");
        builder.append(topOltDHCPMacAddr);
        builder.append(", topOltDHCPOnuMacAddr=");
        builder.append(topOltDHCPOnuMacAddr);
        builder.append(", topOltDHCPIpMacRowStatus=");
        builder.append(topOltDHCPIpMacRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
