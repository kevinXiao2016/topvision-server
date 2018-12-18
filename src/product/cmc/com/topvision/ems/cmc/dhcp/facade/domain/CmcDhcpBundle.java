/***********************************************************************
 * $Id: CmcDhcpBundle.java,v1.0 2012-8-27 下午20:30:28 $
 * 
 * @author: leo
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author leo
 * @created @2012-8-27-下午20:30:28
 * 
 */
@Alias("cmcDhcpBundle")
public class CmcDhcpBundle implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8762097046415998433L;
    private Long cmcId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.2", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpBundlePolicy;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.3", writable = true, type = "Integer32")
    private Integer cableSourceVerify;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.4", writable = true, type = "IpAddress")
    private String virtualPrimaryIpAddr;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.5", writable = true, type = "IpAddress")
    private String virtualPrimaryIpMask;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.6", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpBundleStatus;
    private String cmcDhcpBundleInterface;
    private String bundleInterface;

    public String convertBundleInterfaceToString(String bundleInterface) {
        String result = "";
        if (bundleInterface == null) {
            return result;
        }
        int i = 0;
        for (String s : bundleInterface.split("\\.")) {
            if (i != 0) {
                result += (char) Integer.parseInt(s);
            }
            i++;
        }
        return result;
    }

    public String convertStringToBundleInterface(String str) {
        String result = "";
        if (str.length() > 0) {
            result = result + str.length();
            for (int i = 0; i < str.length(); i++) {
                result = result + Symbol.POINT + (int) str.charAt(i);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpBundle [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsDhcpBundleInterface=");
        builder.append(topCcmtsDhcpBundleInterface);
        builder.append(", topCcmtsDhcpBundlePolicy=");
        builder.append(topCcmtsDhcpBundlePolicy);
        builder.append(", topCcmtsDhcpBundleStatus=");
        builder.append(topCcmtsDhcpBundleStatus);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
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
     * @param topCcmtsDhcpBundleInterface
     *            the topCcmtsDhcpBundleInterface to set
     */
    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
        this.cmcDhcpBundleInterface = convertStringToBundleInterface(topCcmtsDhcpBundleInterface);
        this.bundleInterface = topCcmtsDhcpBundleInterface.replace(CmcConstants.BUNDLE_PRE, "");
    }

    /**
     * @return the topCcmtsDhcpBundlePolicy
     */
    public Integer getTopCcmtsDhcpBundlePolicy() {
        return topCcmtsDhcpBundlePolicy;
    }

    /**
     * @param topCcmtsDhcpBundlePolicy
     *            the topCcmtsDhcpBundlePolicy to set
     */
    public void setTopCcmtsDhcpBundlePolicy(Integer topCcmtsDhcpBundlePolicy) {
        this.topCcmtsDhcpBundlePolicy = topCcmtsDhcpBundlePolicy;
    }

    /**
     * @return the topCcmtsDhcpBundleStatus
     */
    public Integer getTopCcmtsDhcpBundleStatus() {
        return topCcmtsDhcpBundleStatus;
    }

    /**
     * @param topCcmtsDhcpBundleStatus
     *            the topCcmtsDhcpBundleStatus to set
     */
    public void setTopCcmtsDhcpBundleStatus(Integer topCcmtsDhcpBundleStatus) {
        this.topCcmtsDhcpBundleStatus = topCcmtsDhcpBundleStatus;
    }

    public String getCmcDhcpBundleInterface() {
        return cmcDhcpBundleInterface;
    }

    public void setCmcDhcpBundleInterface(String cmcDhcpBundleInterface) {
        this.cmcDhcpBundleInterface = cmcDhcpBundleInterface;
        this.topCcmtsDhcpBundleInterface = convertBundleInterfaceToString(cmcDhcpBundleInterface);
    }

    /**
     * @return the cableSourceVerify
     */
    public Integer getCableSourceVerify() {
        return cableSourceVerify;
    }

    /**
     * @param cableSourceVerify
     *            the cableSourceVerify to set
     */
    public void setCableSourceVerify(Integer cableSourceVerify) {
        this.cableSourceVerify = cableSourceVerify;
    }

    /**
     * @return the virtualPrimaryIpAddr
     */
    public String getVirtualPrimaryIpAddr() {
        return virtualPrimaryIpAddr;
    }

    /**
     * @param virtualPrimaryIpAddr
     *            the virtualPrimaryIpAddr to set
     */
    public void setVirtualPrimaryIpAddr(String virtualPrimaryIpAddr) {
        this.virtualPrimaryIpAddr = virtualPrimaryIpAddr;
    }

    /**
     * @return the virtualPrimaryIpMask
     */
    public String getVirtualPrimaryIpMask() {
        return virtualPrimaryIpMask;
    }

    /**
     * @param virtualPrimaryIpMask
     *            the virtualPrimaryIpMask to set
     */
    public void setVirtualPrimaryIpMask(String virtualPrimaryIpMask) {
        this.virtualPrimaryIpMask = virtualPrimaryIpMask;
    }

    /**
     * @return the bundleInterface
     */
    public String getBundleInterface() {
        return bundleInterface;
    }

    /**
     * @param bundleInterface
     *            the bundleInterface to set
     */
    public void setBundleInterface(String bundleInterface) {
        this.bundleInterface = bundleInterface;
        this.topCcmtsDhcpBundleInterface = CmcConstants.BUNDLE_PRE + bundleInterface;
    }

}