/***********************************************************************
 * $Id: TopOltDhcpStaticIp.java,v1.0 2017年11月16日 下午4:09:40 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.IpsAddress;

/**
 * @author haojie
 * @created @2017年11月16日-下午4:09:40
 *
 */
public class TopOltDhcpStaticIp implements AliasesSuperType {
    private static final long serialVersionUID = -3561607386597495092L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.1", index = true)
    private String topOltDhcpStaticIpVrfNameIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.2", index = true)
    private IpsAddress topOltDhcpStaticIpIndex;
    private String ipIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.3", index = true)
    private IpsAddress topOltDhcpStaticIpMaskIndex;
    private String maskIndex;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.4", writable = true, type = "Integer32")
    private Integer topOltDhcpStaticIpSlot;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.5", writable = true, type = "Integer32")
    private Integer topOltDhcpStaticIpPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.6", writable = true, type = "Integer32")
    private Integer topOltDhcpStaticIpOnu;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.7.6.1.7", writable = true, type = "Integer32")
    private Integer topOltDhcpStaticIpRowStatus;
    private Long typeId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public IpsAddress getTopOltDhcpStaticIpIndex() {
        return topOltDhcpStaticIpIndex;
    }

    public void setTopOltDhcpStaticIpIndex(IpsAddress topOltDhcpStaticIpIndex) {
        this.topOltDhcpStaticIpIndex = topOltDhcpStaticIpIndex;
        this.ipIndex = topOltDhcpStaticIpIndex.toString();
    }

    public IpsAddress getTopOltDhcpStaticIpMaskIndex() {
        return topOltDhcpStaticIpMaskIndex;
    }

    public void setTopOltDhcpStaticIpMaskIndex(IpsAddress topOltDhcpStaticIpMaskIndex) {
        this.topOltDhcpStaticIpMaskIndex = topOltDhcpStaticIpMaskIndex;
        this.maskIndex = topOltDhcpStaticIpMaskIndex.toString();
    }

    public Integer getTopOltDhcpStaticIpSlot() {
        return topOltDhcpStaticIpSlot;
    }

    public void setTopOltDhcpStaticIpSlot(Integer topOltDhcpStaticIpSlot) {
        this.topOltDhcpStaticIpSlot = topOltDhcpStaticIpSlot;
    }

    public Integer getTopOltDhcpStaticIpPort() {
        return topOltDhcpStaticIpPort;
    }

    public void setTopOltDhcpStaticIpPort(Integer topOltDhcpStaticIpPort) {
        this.topOltDhcpStaticIpPort = topOltDhcpStaticIpPort;
    }

    public Integer getTopOltDhcpStaticIpOnu() {
        return topOltDhcpStaticIpOnu;
    }

    public void setTopOltDhcpStaticIpOnu(Integer topOltDhcpStaticIpOnu) {
        this.topOltDhcpStaticIpOnu = topOltDhcpStaticIpOnu;
    }

    public Integer getTopOltDhcpStaticIpRowStatus() {
        return topOltDhcpStaticIpRowStatus;
    }

    public void setTopOltDhcpStaticIpRowStatus(Integer topOltDhcpStaticIpRowStatus) {
        this.topOltDhcpStaticIpRowStatus = topOltDhcpStaticIpRowStatus;
    }

    public String getTopOltDhcpStaticIpVrfNameIndex() {
        return topOltDhcpStaticIpVrfNameIndex;
    }

    public void setTopOltDhcpStaticIpVrfNameIndex(String topOltDhcpStaticIpVrfNameIndex) {
        this.topOltDhcpStaticIpVrfNameIndex = topOltDhcpStaticIpVrfNameIndex;
    }

    public String getIpIndex() {
        //返回 网络地址
        if (ipIndex != null && ipIndex != "" && maskIndex != null && maskIndex != "") {
            return IpUtils.getNetworkSegFromIpMask(ipIndex, maskIndex);
        }
        return "";
    }

    public void setIpIndex(String ipIndex) {
        this.ipIndex = ipIndex;
        this.topOltDhcpStaticIpIndex = new IpsAddress(ipIndex);
    }

    public String getMaskIndex() {
        return maskIndex;
    }

    public void setMaskIndex(String maskIndex) {
        this.maskIndex = maskIndex;
        this.topOltDhcpStaticIpMaskIndex = new IpsAddress(maskIndex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopOltDhcpStaticIp [entityId=");
        builder.append(entityId);
        builder.append(", topOltDhcpStaticIpVrfNameIndex=");
        builder.append(topOltDhcpStaticIpVrfNameIndex);
        builder.append(", topOltDhcpStaticIpIndex=");
        builder.append(topOltDhcpStaticIpIndex);
        builder.append(", ipIndex=");
        builder.append(ipIndex);
        builder.append(", topOltDhcpStaticIpMaskIndex=");
        builder.append(topOltDhcpStaticIpMaskIndex);
        builder.append(", maskIndex=");
        builder.append(maskIndex);
        builder.append(", topOltDhcpStaticIpSlot=");
        builder.append(topOltDhcpStaticIpSlot);
        builder.append(", topOltDhcpStaticIpPort=");
        builder.append(topOltDhcpStaticIpPort);
        builder.append(", topOltDhcpStaticIpOnu=");
        builder.append(topOltDhcpStaticIpOnu);
        builder.append(", topOltDhcpStaticIpRowStatus=");
        builder.append(topOltDhcpStaticIpRowStatus);
        builder.append("]");
        return builder.toString();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
