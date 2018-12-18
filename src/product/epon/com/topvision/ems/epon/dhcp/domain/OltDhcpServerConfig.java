/***********************************************************************
 * $Id: OltDhcpServerConfig.java,v1.0 2011-11-16 上午09:44:48 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-16-上午09:44:48
 * 
 */
public class OltDhcpServerConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -5042216997882737081L;
    private Long entityId;
    private String onuType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.2.1.1", index = true)
    private Integer topOltDHCPServerIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.2.1.2", writable = true, type = "Integer32")
    private Integer topOltDHCPServerVid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.2.1.3", writable = true, type = "IpAddress")
    private String topOltDHCPServerIpAddr;
    private Long topOltDHCPServerIpAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.2.1.4", writable = true, type = "IpAddress")
    private String topOltDHCPServerIpMask;
    private Long topOltDHCPServerIpMaskLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.11.1.2.1.5", writable = true, type = "Integer32")
    private Integer topOltDHCPServRowStatus;

    /**
     * @return the topOltDHCPServerIndex
     */
    public Integer getTopOltDHCPServerIndex() {
        return topOltDHCPServerIndex;
    }

    /**
     * @param topOltDHCPServerIndex
     *            the topOltDHCPServerIndex to set
     */
    public void setTopOltDHCPServerIndex(Integer topOltDHCPServerIndex) {
        this.topOltDHCPServerIndex = topOltDHCPServerIndex;
        if (topOltDHCPServerIndex >= 0 && topOltDHCPServerIndex <= 1) {
            onuType = "HOST";
        } else if (topOltDHCPServerIndex >= 2 && topOltDHCPServerIndex <= 3) {
            onuType = "CM";
        } else if (topOltDHCPServerIndex >= 4 && topOltDHCPServerIndex <= 5) {
            onuType = "STB";
        } else if (topOltDHCPServerIndex >= 6 && topOltDHCPServerIndex <= 7) {
            onuType = "MTA";
        } else {
            onuType = "DEFAULT";
        }

    }

    /**
     * @return the topOltDHCPServerVid
     */
    public Integer getTopOltDHCPServerVid() {
        return topOltDHCPServerVid;
    }

    /**
     * @param topOltDHCPServerVid
     *            the topOltDHCPServerVid to set
     */
    public void setTopOltDHCPServerVid(Integer topOltDHCPServerVid) {
        this.topOltDHCPServerVid = topOltDHCPServerVid;
    }

    /**
     * @return the topOltDHCPServerIpAddr
     */
    public String getTopOltDHCPServerIpAddr() {
        return topOltDHCPServerIpAddr;
    }

    /**
     * @param topOltDHCPServerIpAddr
     *            the topOltDHCPServerIpAddr to set
     */
    public void setTopOltDHCPServerIpAddr(String topOltDHCPServerIpAddr) {
        this.topOltDHCPServerIpAddr = topOltDHCPServerIpAddr;
        topOltDHCPServerIpAddrLong = new IpUtils(topOltDHCPServerIpAddr).longValue();
    }

    /**
     * @return the topOltDHCPServerIpMask
     */
    public String getTopOltDHCPServerIpMask() {
        return topOltDHCPServerIpMask;
    }

    /**
     * @param topOltDHCPServerIpMask
     *            the topOltDHCPServerIpMask to set
     */
    public void setTopOltDHCPServerIpMask(String topOltDHCPServerIpMask) {
        this.topOltDHCPServerIpMask = topOltDHCPServerIpMask;
        topOltDHCPServerIpMaskLong = new IpUtils(topOltDHCPServerIpMask).longValue();
    }

    /**
     * @return the topOltDHCPServRowStatus
     */
    public Integer getTopOltDHCPServRowStatus() {
        return topOltDHCPServRowStatus;
    }

    /**
     * @param topOltDHCPServRowStatus
     *            the topOltDHCPServRowStatus to set
     */
    public void setTopOltDHCPServRowStatus(Integer topOltDHCPServRowStatus) {
        this.topOltDHCPServRowStatus = topOltDHCPServRowStatus;
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
     * @return the topOltDHCPServerIpAddrLong
     */
    public Long getTopOltDHCPServerIpAddrLong() {
        return topOltDHCPServerIpAddrLong;
    }

    /**
     * @param topOltDHCPServerIpAddrLong
     *            the topOltDHCPServerIpAddrLong to set
     */
    public void setTopOltDHCPServerIpAddrLong(Long topOltDHCPServerIpAddrLong) {
        this.topOltDHCPServerIpAddrLong = topOltDHCPServerIpAddrLong;
        topOltDHCPServerIpAddr = new IpUtils(topOltDHCPServerIpAddrLong).toString();
    }

    /**
     * @return the topOltDHCPServerIpMaskLong
     */
    public Long getTopOltDHCPServerIpMaskLong() {
        return topOltDHCPServerIpMaskLong;
    }

    /**
     * @param topOltDHCPServerIpMaskLong
     *            the topOltDHCPServerIpMaskLong to set
     */
    public void setTopOltDHCPServerIpMaskLong(Long topOltDHCPServerIpMaskLong) {
        this.topOltDHCPServerIpMaskLong = topOltDHCPServerIpMaskLong;
        topOltDHCPServerIpMask = new IpUtils(topOltDHCPServerIpMaskLong).toString();
    }

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

    public boolean equalsForIndex(OltDhcpServerConfig config) {
        if (this.getOnuType().equals(config.getOnuType())
                && this.getTopOltDHCPServerIndex().equals(config.getTopOltDHCPServerIndex())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltDhcpServerConfig [entityId=");
        builder.append(entityId);
        builder.append(", onuType=");
        builder.append(onuType);
        builder.append(", topOltDHCPServerIndex=");
        builder.append(topOltDHCPServerIndex);
        builder.append(", topOltDHCPServerVid=");
        builder.append(topOltDHCPServerVid);
        builder.append(", topOltDHCPServerIpAddr=");
        builder.append(topOltDHCPServerIpAddr);
        builder.append(", topOltDHCPServerIpAddrLong=");
        builder.append(topOltDHCPServerIpAddrLong);
        builder.append(", topOltDHCPServerIpMask=");
        builder.append(topOltDHCPServerIpMask);
        builder.append(", topOltDHCPServerIpMaskLong=");
        builder.append(topOltDHCPServerIpMaskLong);
        builder.append(", topOltDHCPServRowStatus=");
        builder.append(topOltDHCPServRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
