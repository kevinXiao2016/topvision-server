package com.topvision.ems.cmc.vlan.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcVlanDhcpAllocEntry")
public class CmcVlanDhcpAllocEntry implements AliasesSuperType {
    private static final long serialVersionUID = 3226637486348071669L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.1", writable = true, index = true)
    private Integer topCcmtsVlanIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.5.1.1", writable = true, type = "Integer32")
    private Integer topCcmtsVlanDhcpAlloc;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.5.1.2", writable = true, type = "OctetString")
    private String topCcmtsOption60;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.5.1.3", writable = true, type = "IpAddress")
    private String topCcmtsVlanDhcpAllocIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.5.1.4", writable = true, type = "IpAddress")
    private String topCcmtsVlanDhcpAllocIpMask;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsVlanDhcpAlloc() {
        return topCcmtsVlanDhcpAlloc;
    }

    public void setTopCcmtsVlanDhcpAlloc(Integer topCcmtsVlanDhcpAlloc) {
        this.topCcmtsVlanDhcpAlloc = topCcmtsVlanDhcpAlloc;
    }

    public String getTopCcmtsOption60() {
        return topCcmtsOption60;
    }

    public void setTopCcmtsOption60(String topCcmtsOption60) {
        this.topCcmtsOption60 = topCcmtsOption60;
    }

    public String getTopCcmtsVlanDhcpAllocIpAddr() {
        return topCcmtsVlanDhcpAllocIpAddr;
    }

    public void setTopCcmtsVlanDhcpAllocIpAddr(String topCcmtsVlanDhcpAllocIpAddr) {
        this.topCcmtsVlanDhcpAllocIpAddr = topCcmtsVlanDhcpAllocIpAddr;
    }

    public String getTopCcmtsVlanDhcpAllocIpMask() {
        return topCcmtsVlanDhcpAllocIpMask;
    }

    public void setTopCcmtsVlanDhcpAllocIpMask(String topCcmtsVlanDhcpAllocIpMask) {
        this.topCcmtsVlanDhcpAllocIpMask = topCcmtsVlanDhcpAllocIpMask;
    }

    public Integer getTopCcmtsVlanIndex() {
        return topCcmtsVlanIndex;
    }

    public void setTopCcmtsVlanIndex(Integer topCcmtsVlanIndex) {
        this.topCcmtsVlanIndex = topCcmtsVlanIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVlanDhcpAllocEntry [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsVlanIndex=");
        builder.append(topCcmtsVlanIndex);
        builder.append(", topCcmtsVlanDhcpAlloc=");
        builder.append(topCcmtsVlanDhcpAlloc);
        builder.append(", topCcmtsOption60=");
        builder.append(topCcmtsOption60);
        builder.append(", topCcmtsVlanDhcpAllocIpAddr=");
        builder.append(topCcmtsVlanDhcpAllocIpAddr);
        builder.append(", topCcmtsVlanDhcpAllocIpMask=");
        builder.append(topCcmtsVlanDhcpAllocIpMask);
        builder.append("]");
        return builder.toString();
    }
}
