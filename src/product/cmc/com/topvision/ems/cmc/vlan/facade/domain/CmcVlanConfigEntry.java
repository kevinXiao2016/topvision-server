package com.topvision.ems.cmc.vlan.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcVlanConfigEntry")
public class CmcVlanConfigEntry implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3839178541712712602L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.1", index = true)
    private Integer topCcmtsVlanIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.2", writable = true)
    private String topCcmtsVlanName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.3")
    private String topCcmtsTaggedPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.4")
    private String topCcmtsUntaggedPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.5")
    private Integer topCcmtsVlanFloodMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.2.1.6", writable = true, type = "Integer32")
    private Integer topCcmtsVlanStatus;

    private Integer priTag;
    private String ipAddr;
    private String ipMask;
    private Integer dhcpAlloc;
    private String option60;
    private String dhcpAllocIpAddr;
    private String dhcpAllocIpMask;
    private Integer priIpExist;
    private Integer secVidIndex;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsVlanIndex() {
        return topCcmtsVlanIndex;
    }

    public void setTopCcmtsVlanIndex(Integer topCcmtsVlanIndex) {
        this.topCcmtsVlanIndex = topCcmtsVlanIndex;
    }

    public String getTopCcmtsVlanName() {
        return topCcmtsVlanName;
    }

    public void setTopCcmtsVlanName(String topCcmtsVlanName) {
        this.topCcmtsVlanName = topCcmtsVlanName;
    }

    public String getTopCcmtsTaggedPort() {
        return topCcmtsTaggedPort;
    }

    public void setTopCcmtsTaggedPort(String topCcmtsTaggedPort) {
        this.topCcmtsTaggedPort = topCcmtsTaggedPort;
    }

    public String getTopCcmtsUntaggedPort() {
        return topCcmtsUntaggedPort;
    }

    public void setTopCcmtsUntaggedPort(String topCcmtsUntaggedPort) {
        this.topCcmtsUntaggedPort = topCcmtsUntaggedPort;
    }

    public Integer getTopCcmtsVlanStatus() {
        return topCcmtsVlanStatus;
    }

    public void setTopCcmtsVlanStatus(Integer topCcmtsVlanStatus) {
        this.topCcmtsVlanStatus = topCcmtsVlanStatus;
    }

    public Integer getTopCcmtsVlanFloodMode() {
        return topCcmtsVlanFloodMode;
    }

    public void setTopCcmtsVlanFloodMode(Integer topCcmtsVlanFloodMode) {
        this.topCcmtsVlanFloodMode = topCcmtsVlanFloodMode;
    }

    public Integer getPriTag() {
        return priTag;
    }

    public void setPriTag(Integer priTag) {
        this.priTag = priTag;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public Integer getDhcpAlloc() {
        return dhcpAlloc;
    }

    public void setDhcpAlloc(Integer dhcpAlloc) {
        this.dhcpAlloc = dhcpAlloc;
    }

    public String getOption60() {
        return option60;
    }

    public void setOption60(String option60) {
        this.option60 = option60;
    }

    public String getDhcpAllocIpAddr() {
        return dhcpAllocIpAddr;
    }

    public void setDhcpAllocIpAddr(String dhcpAllocIpAddr) {
        this.dhcpAllocIpAddr = dhcpAllocIpAddr;
    }

    public String getDhcpAllocIpMask() {
        return dhcpAllocIpMask;
    }

    public void setDhcpAllocIpMask(String dhcpAllocIpMask) {
        this.dhcpAllocIpMask = dhcpAllocIpMask;
    }

    public Integer getPriIpExist() {
        return priIpExist;
    }

    public void setPriIpExist(Integer priIpExist) {
        this.priIpExist = priIpExist;
    }

    public Integer getSecVidIndex() {
        return secVidIndex;
    }

    public void setSecVidIndex(Integer secVidIndex) {
        this.secVidIndex = secVidIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVlanConfigInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsVlanIndex=");
        builder.append(topCcmtsVlanIndex);
        builder.append(", topCcmtsVlanName=");
        builder.append(topCcmtsVlanName);
        builder.append(", topCcmtsTaggedPort=");
        builder.append(topCcmtsTaggedPort);
        builder.append(", topCcmtsUntaggedPort=");
        builder.append(topCcmtsUntaggedPort);
        builder.append(", topCcmtsVlanFloodMode=");
        builder.append(topCcmtsVlanFloodMode);
        builder.append(", topCcmtsVlanStatus=");
        builder.append(topCcmtsVlanStatus);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
