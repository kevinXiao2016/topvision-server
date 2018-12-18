package com.topvision.ems.cmc.vlan.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
@Alias("cmcVlanPrimaryIp")
public class CmcVlanPrimaryIp implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -410127526967824442L;
    private Long cmcId;
    private Long vlanAutoId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.1", index = true)
    private Integer topCcmtsVifPriIpVlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.2", writable = true, type = "IpAddress")
    private String topCcmtsVifPriIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.3", writable = true, type = "IpAddress")
    private String topCcmtsVifPriIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.3.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsVifPriIpStatus;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getVlanAutoId() {
        return vlanAutoId;
    }

    public void setVlanAutoId(Long vlanAutoId) {
        this.vlanAutoId = vlanAutoId;
    }

    public Integer getTopCcmtsVifPriIpVlanId() {
        return topCcmtsVifPriIpVlanId;
    }

    public void setTopCcmtsVifPriIpVlanId(Integer topCcmtsVifPriIpVlanId) {
        this.topCcmtsVifPriIpVlanId = topCcmtsVifPriIpVlanId;
    }

    public String getTopCcmtsVifPriIpAddr() {
        return topCcmtsVifPriIpAddr;
    }

    public void setTopCcmtsVifPriIpAddr(String topCcmtsVifPriIpAddr) {
        this.topCcmtsVifPriIpAddr = topCcmtsVifPriIpAddr;
    }

    public String getTopCcmtsVifPriIpMask() {
        return topCcmtsVifPriIpMask;
    }

    public void setTopCcmtsVifPriIpMask(String topCcmtsVifPriIpMask) {
        this.topCcmtsVifPriIpMask = topCcmtsVifPriIpMask;
    }

    public Integer getTopCcmtsVifPriIpStatus() {
        return topCcmtsVifPriIpStatus;
    }

    public void setTopCcmtsVifPriIpStatus(Integer topCcmtsVifPriIpStatus) {
        this.topCcmtsVifPriIpStatus = topCcmtsVifPriIpStatus;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVlanPrimaryIp [cmcId=");
        builder.append(cmcId);
        builder.append(", vlanAutoId=");
        builder.append(vlanAutoId);
        builder.append(", topCcmtsVifPriIpVlanId=");
        builder.append(topCcmtsVifPriIpVlanId);
        builder.append(", topCcmtsVifPriIpAddr=");
        builder.append(topCcmtsVifPriIpAddr);
        builder.append(", topCcmtsVifPriIpMask=");
        builder.append(topCcmtsVifPriIpMask);
        builder.append(", topCcmtsVifPriIpStatus=");
        builder.append(topCcmtsVifPriIpStatus);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
