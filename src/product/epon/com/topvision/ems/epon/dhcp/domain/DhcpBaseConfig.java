/***********************************************************************
 * $Id: DhcpBaseConfig.java,v1.0 2012-4-8 下午01:58:09 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author huqiao
 * @created @2012-4-8-下午01:58:09
 * 
 */
public class DhcpBaseConfig implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4343033988058259401L;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.1.0", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpRelayMode;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.0", writable = true, type = "Integer32")
    private Integer topCcmtsGiAddrOverwrite;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.3.0", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpDynIpMacBind;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.4.0", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpPreciseRsp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.6.0", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpGiAddrPolicy;

    /**
     * @return the topCcmtsDhcpRelayMode
     */
    public Integer getTopCcmtsDhcpRelayMode() {
        return topCcmtsDhcpRelayMode;
    }

    /**
     * @param topCcmtsDhcpRelayMode
     *            the topCcmtsDhcpRelayMode to set
     */
    public void setTopCcmtsDhcpRelayMode(Integer topCcmtsDhcpRelayMode) {
        this.topCcmtsDhcpRelayMode = topCcmtsDhcpRelayMode;
    }

    /**
     * @return the topCcmtsGiAddrOverwrite
     */
    public Integer getTopCcmtsGiAddrOverwrite() {
        return topCcmtsGiAddrOverwrite;
    }

    /**
     * @param topCcmtsGiAddrOverwrite
     *            the topCcmtsGiAddrOverwrite to set
     */
    public void setTopCcmtsGiAddrOverwrite(Integer topCcmtsGiAddrOverwrite) {
        this.topCcmtsGiAddrOverwrite = topCcmtsGiAddrOverwrite;
    }

    /**
     * @return the topCcmtsDhcpDynIpMacBind
     */
    public Integer getTopCcmtsDhcpDynIpMacBind() {
        return topCcmtsDhcpDynIpMacBind;
    }

    /**
     * @param topCcmtsDhcpDynIpMacBind
     *            the topCcmtsDhcpDynIpMacBind to set
     */
    public void setTopCcmtsDhcpDynIpMacBind(Integer topCcmtsDhcpDynIpMacBind) {
        this.topCcmtsDhcpDynIpMacBind = topCcmtsDhcpDynIpMacBind;
    }

    /**
     * @return the topCcmtsDhcpPreciseRsp
     */
    public Integer getTopCcmtsDhcpPreciseRsp() {
        return topCcmtsDhcpPreciseRsp;
    }

    /**
     * @param topCcmtsDhcpPreciseRsp
     *            the topCcmtsDhcpPreciseRsp to set
     */
    public void setTopCcmtsDhcpPreciseRsp(Integer topCcmtsDhcpPreciseRsp) {
        this.topCcmtsDhcpPreciseRsp = topCcmtsDhcpPreciseRsp;
    }

    /**
     * @return the topCcmtsDhcpGiAddrPolicy
     */
    public Integer getTopCcmtsDhcpGiAddrPolicy() {
        return topCcmtsDhcpGiAddrPolicy;
    }

    /**
     * @param topCcmtsDhcpGiAddrPolicy
     *            the topCcmtsDhcpGiAddrPolicy to set
     */
    public void setTopCcmtsDhcpGiAddrPolicy(Integer topCcmtsDhcpGiAddrPolicy) {
        this.topCcmtsDhcpGiAddrPolicy = topCcmtsDhcpGiAddrPolicy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DhcpBaseConfig [topCcmtsDhcpRelayMode=");
        builder.append(topCcmtsDhcpRelayMode);
        builder.append(", topCcmtsGiAddrOverwrite=");
        builder.append(topCcmtsGiAddrOverwrite);
        builder.append(", topCcmtsDhcpDynIpMacBind=");
        builder.append(topCcmtsDhcpDynIpMacBind);
        builder.append(", topCcmtsDhcpPreciseRsp=");
        builder.append(topCcmtsDhcpPreciseRsp);
        builder.append(", topCcmtsDhcpGiAddrPolicy=");
        builder.append(topCcmtsDhcpGiAddrPolicy);
        builder.append("]");
        return builder.toString();
    }

}
