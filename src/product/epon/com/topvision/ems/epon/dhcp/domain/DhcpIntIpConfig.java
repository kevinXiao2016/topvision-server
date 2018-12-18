/***********************************************************************
 * $Id: CmcDhcpServerConfig.java,v1.0 2012-2-13 下午05:03:28 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午05:03:28
 * 
 */
@Alias(value = "dhcpIntIp")
public class DhcpIntIpConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8762097046415998433L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.6.1.1", index = true)
    private Integer topCcmtsDhcpIntIpIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.6.1.2", writable = true, type = "IpAddress")
    private String topCcmtsDhcpIntIpAddr;
    private Long topCcmtsDhcpIntIpAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.6.1.3", writable = true, type = "IpAddress")
    private String topCcmtsDhcpIntIpMask;
    private Long topCcmtsDhcpIntIpMaskLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.6.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpIntIpStatus;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpIntIp [");
        builder.append("entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsDhcpIfIndex=");
        builder.append(topCcmtsDhcpBundleInterface);
        builder.append(", topCcmtsDhcpIntIpIndex=");
        builder.append(topCcmtsDhcpIntIpIndex);
        builder.append(", topCcmtsDhcpIntIpAddr=");
        builder.append(topCcmtsDhcpIntIpAddr);
        builder.append(", topCcmtsDhcpIntIpAddrLong=");
        builder.append(topCcmtsDhcpIntIpAddrLong);
        builder.append(", topCcmtsDhcpIntIpMask=");
        builder.append(topCcmtsDhcpIntIpMask);
        builder.append(", topCcmtsDhcpIntIpMaskLong=");
        builder.append(topCcmtsDhcpIntIpMaskLong);
        builder.append(", topCcmtsDhcpIntIpStatus=");
        builder.append(topCcmtsDhcpIntIpStatus);
        builder.append("]");
        return builder.toString();
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

    /**
     * @return the topCcmtsDhcpBundleInterface
     */
    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    /**
     * @param topCcmtsDhcpBundleInterface the topCcmtsDhcpBundleInterface to set
     */
    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }

    /**
     * @return the topCcmtsDhcpIntIpIndex
     */
    public Integer getTopCcmtsDhcpIntIpIndex() {
        return topCcmtsDhcpIntIpIndex;
    }

    /**
     * @param topCcmtsDhcpIntIpIndex
     *            the topCcmtsDhcpIntIpIndex to set
     */
    public void setTopCcmtsDhcpIntIpIndex(Integer topCcmtsDhcpIntIpIndex) {
        this.topCcmtsDhcpIntIpIndex = topCcmtsDhcpIntIpIndex;
    }

    /**
     * @return the topCcmtsDhcpIntIpAddr
     */
    public String getTopCcmtsDhcpIntIpAddr() {
        return topCcmtsDhcpIntIpAddr;
    }

    /**
     * @param topCcmtsDhcpIntIpAddr
     *            the topCcmtsDhcpIntIpAddr to set
     */
    public void setTopCcmtsDhcpIntIpAddr(String topCcmtsDhcpIntIpAddr) {
        this.topCcmtsDhcpIntIpAddr = topCcmtsDhcpIntIpAddr;
    }

    /**
     * @return the topCcmtsDhcpIntIpAddrLong
     */
    public Long getTopCcmtsDhcpIntIpAddrLong() {
        return topCcmtsDhcpIntIpAddrLong;
    }

    /**
     * @param topCcmtsDhcpIntIpAddrLong
     *            the topCcmtsDhcpIntIpAddrLong to set
     */
    public void setTopCcmtsDhcpIntIpAddrLong(Long topCcmtsDhcpIntIpAddrLong) {
        this.topCcmtsDhcpIntIpAddrLong = topCcmtsDhcpIntIpAddrLong;
    }

    /**
     * @return the topCcmtsDhcpIntIpMask
     */
    public String getTopCcmtsDhcpIntIpMask() {
        return topCcmtsDhcpIntIpMask;
    }

    /**
     * @param topCcmtsDhcpIntIpMask
     *            the topCcmtsDhcpIntIpMask to set
     */
    public void setTopCcmtsDhcpIntIpMask(String topCcmtsDhcpIntIpMask) {
        this.topCcmtsDhcpIntIpMask = topCcmtsDhcpIntIpMask;
    }

    /**
     * @return the topCcmtsDhcpIntIpMaskLong
     */
    public Long getTopCcmtsDhcpIntIpMaskLong() {
        return topCcmtsDhcpIntIpMaskLong;
    }

    /**
     * @param topCcmtsDhcpIntIpMaskLong
     *            the topCcmtsDhcpIntIpMaskLong to set
     */
    public void setTopCcmtsDhcpIntIpMaskLong(Long topCcmtsDhcpIntIpMaskLong) {
        this.topCcmtsDhcpIntIpMaskLong = topCcmtsDhcpIntIpMaskLong;
    }

    /**
     * @return the topCcmtsDhcpIntIpStatus
     */
    public Integer getTopCcmtsDhcpIntIpStatus() {
        return topCcmtsDhcpIntIpStatus;
    }

    /**
     * @param topCcmtsDhcpIntIpStatus
     *            the topCcmtsDhcpIntIpStatus to set
     */
    public void setTopCcmtsDhcpIntIpStatus(Integer topCcmtsDhcpIntIpStatus) {
        this.topCcmtsDhcpIntIpStatus = topCcmtsDhcpIntIpStatus;
    }

}
