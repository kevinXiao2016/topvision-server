package com.topvision.ems.cmc.vlan.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
@Alias("cmcVlanPrimaryInterface")
public class CmcVlanPrimaryInterface implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5786292319189575764L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.1.1.0", writable = true)
    private Integer vlanPrimaryInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.1.2.0", writable = true)
    private String vlanPrimaryDefaultRoute;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.1.3.0")
    private String vlanPrimaryDefaultIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.7.1.4.0")
    private String vlanPrimaryDefaultIpMask;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Integer getVlanPrimaryInterface() {
        return vlanPrimaryInterface;
    }

    public void setVlanPrimaryInterface(Integer vlanPrimaryInterface) {
        this.vlanPrimaryInterface = vlanPrimaryInterface;
    }

    public String getVlanPrimaryDefaultRoute() {
        return vlanPrimaryDefaultRoute;
    }

    public void setVlanPrimaryDefaultRoute(String vlanPrimaryDefaultRoute) {
        this.vlanPrimaryDefaultRoute = vlanPrimaryDefaultRoute;
    }   

    public String getVlanPrimaryDefaultIpAddr() {
        return vlanPrimaryDefaultIpAddr;
    }

    public void setVlanPrimaryDefaultIpAddr(String vlanPrimaryDefaultIpAddr) {
        this.vlanPrimaryDefaultIpAddr = vlanPrimaryDefaultIpAddr;
    }

    public String getVlanPrimaryDefaultIpMask() {
        return vlanPrimaryDefaultIpMask;
    }

    public void setVlanPrimaryDefaultIpMask(String vlanPrimaryDefaultIpMask) {
        this.vlanPrimaryDefaultIpMask = vlanPrimaryDefaultIpMask;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcVlanPrimaryInterface [cmcId=");
        builder.append(cmcId);
        builder.append(", vlanPrimaryInterface=");
        builder.append(vlanPrimaryInterface);
        builder.append(", vlanPrimaryDefaultRoute=");
        builder.append(vlanPrimaryDefaultRoute);
        builder.append(", vlanPrimaryDefaultIpAddr=");
        builder.append(vlanPrimaryDefaultIpAddr);
        builder.append(", vlanPrimaryDefaultIpMask=");
        builder.append(vlanPrimaryDefaultIpMask);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
