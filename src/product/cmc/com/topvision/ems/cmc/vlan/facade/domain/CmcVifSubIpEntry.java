package com.topvision.ems.cmc.vlan.facade.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

@Alias("cmcVifSubIpEntry")
public class CmcVifSubIpEntry implements AliasesSuperType{
    private static final long serialVersionUID = -5311380020771719649L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.4.1.1", writable = true, index = true)
    private Integer topCcmtsVifSubIpVlanIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.4.1.2", writable = true, index = true)
    private Integer topCcmtsVifSubIpSeqIdx;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.4.1.3", writable = true, type = "IpAddress")
    private String topCcmtsVifSubIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.4.1.4", writable = true, type = "IpAddress")
    private String topCcmtsVifSubIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.4.1.5", writable = true, type = "Integer32")
    private Integer topCcmtsVifSubIpStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getTopCcmtsVifSubIpVlanIdx() {
        return topCcmtsVifSubIpVlanIdx;
    }

    public void setTopCcmtsVifSubIpVlanIdx(Integer topCcmtsVifSubIpVlanIdx) {
        this.topCcmtsVifSubIpVlanIdx = topCcmtsVifSubIpVlanIdx;
    }

    public Integer getTopCcmtsVifSubIpSeqIdx() {
        return topCcmtsVifSubIpSeqIdx;
    }

    public void setTopCcmtsVifSubIpSeqIdx(Integer topCcmtsVifSubIpSeqIdx) {
        this.topCcmtsVifSubIpSeqIdx = topCcmtsVifSubIpSeqIdx;
    }

    public String getTopCcmtsVifSubIpAddr() {
        return topCcmtsVifSubIpAddr;
    }

    public void setTopCcmtsVifSubIpAddr(String topCcmtsVifSubIpAddr) {
        this.topCcmtsVifSubIpAddr = topCcmtsVifSubIpAddr;
    }

    public String getTopCcmtsVifSubIpMask() {
        return topCcmtsVifSubIpMask;
    }

    public void setTopCcmtsVifSubIpMask(String topCcmtsVifSubIpMask) {
        this.topCcmtsVifSubIpMask = topCcmtsVifSubIpMask;
    }

    public Integer getTopCcmtsVifSubIpStatus() {
        return topCcmtsVifSubIpStatus;
    }

    public void setTopCcmtsVifSubIpStatus(Integer topCcmtsVifSubIpStatus) {
        this.topCcmtsVifSubIpStatus = topCcmtsVifSubIpStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVifSubIpEntry [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsVifSubIpVlanIdx=");
        builder.append(topCcmtsVifSubIpVlanIdx);
        builder.append(", topCcmtsVifSubIpSeqIdx=");
        builder.append(topCcmtsVifSubIpSeqIdx);
        builder.append(", topCcmtsVifSubIpAddr=");
        builder.append(topCcmtsVifSubIpAddr);
        builder.append(", topCcmtsVifSubIpMask=");
        builder.append(topCcmtsVifSubIpMask);
        builder.append(", topCcmtsVifSubIpStatus=");
        builder.append(topCcmtsVifSubIpStatus);
        builder.append("]");
        return builder.toString();
    }

}
