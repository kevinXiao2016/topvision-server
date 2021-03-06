/***********************************************************************
 * $Id: CmcDhcpServerConfig.java,v1.0 2012-2-13 下午05:03:28 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午05:03:28
 * 
 */
@Alias("cmcDhcpServerConfig")
public class CmcDhcpServerConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4117485331544784225L;
    private Long cmcId;
    private Long entityId;
    private Long helperId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;    
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.3.1.1", index = true)
    private Integer topCcmtsDhcpHelperDeviceType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.3.1.2", index = true)
    private Integer topCcmtsDhcpHelperIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.3.1.3", writable = true, type = "IpAddress")
    private String topCcmtsDhcpHelperIpAddr;
    private Long topCcmtsDhcpHelperIpAddrLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.3.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpHelperStatus;

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

    public Long getHelperId() {
        return helperId;
    }

    public void setHelperId(Long helperId) {
        this.helperId = helperId;
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
    }

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

    public Integer getTopCcmtsDhcpHelperIndex() {
        return topCcmtsDhcpHelperIndex;
    }

    public void setTopCcmtsDhcpHelperIndex(Integer topCcmtsDhcpHelperIndex) {
        this.topCcmtsDhcpHelperIndex = topCcmtsDhcpHelperIndex;
    }

    /**
     * @return the topCcmtsDhcpHelperDeviceType
     */
    public Integer getTopCcmtsDhcpHelperDeviceType() {
        return topCcmtsDhcpHelperDeviceType;
    }

    /**
     * @param topCcmtsDhcpHelperDeviceType
     *            the topCcmtsDhcpHelperDeviceType to set
     */
    public void setTopCcmtsDhcpHelperDeviceType(Integer topCcmtsDhcpHelperDeviceType) {
        this.topCcmtsDhcpHelperDeviceType = topCcmtsDhcpHelperDeviceType;
    }

    /**
     * @return the topCcmtsDhcpHelperIpAddr
     */
    public String getTopCcmtsDhcpHelperIpAddr() {
        return topCcmtsDhcpHelperIpAddr;
    }

    /**
     * @param topCcmtsDhcpHelperIpAddr
     *            the topCcmtsDhcpHelperIpAddr to set
     */
    public void setTopCcmtsDhcpHelperIpAddr(String topCcmtsDhcpHelperIpAddr) {
        this.topCcmtsDhcpHelperIpAddr = topCcmtsDhcpHelperIpAddr;
    }

    /**
     * @return the topCcmtsDhcpHelperIpAddrLong
     */
    public Long getTopCcmtsDhcpHelperIpAddrLong() {
        return topCcmtsDhcpHelperIpAddrLong;
    }

    /**
     * @param topCcmtsDhcpHelperIpAddrLong
     *            the topCcmtsDhcpHelperIpAddrLong to set
     */
    public void setTopCcmtsDhcpHelperIpAddrLong(Long topCcmtsDhcpHelperIpAddrLong) {
        this.topCcmtsDhcpHelperIpAddrLong = topCcmtsDhcpHelperIpAddrLong;
    }

    /**
     * @return the topCcmtsDhcpHelperStatus
     */
    public Integer getTopCcmtsDhcpHelperStatus() {
        return topCcmtsDhcpHelperStatus;
    }

    /**
     * @param topCcmtsDhcpHelperStatus
     *            the topCcmtsDhcpHelperStatus to set
     */
    public void setTopCcmtsDhcpHelperStatus(Integer topCcmtsDhcpHelperStatus) {
        this.topCcmtsDhcpHelperStatus = topCcmtsDhcpHelperStatus;
    }
    
    public CmcDhcpServerConfig clone(){
        CmcDhcpServerConfig cl = new CmcDhcpServerConfig();
        cl.setCmcId(cmcId);
        cl.setEntityId(entityId);
        cl.setHelperId(helperId);
        cl.setTopCcmtsDhcpBundleInterface(topCcmtsDhcpBundleInterface);
        cl.setTopCcmtsDhcpHelperDeviceType(topCcmtsDhcpHelperDeviceType);
        cl.setTopCcmtsDhcpHelperIndex(topCcmtsDhcpHelperIndex);
        cl.setTopCcmtsDhcpHelperIpAddr(topCcmtsDhcpHelperIpAddr);
        cl.setTopCcmtsDhcpHelperIpAddrLong(topCcmtsDhcpHelperIpAddrLong);
        cl.setTopCcmtsDhcpHelperStatus(topCcmtsDhcpHelperStatus);
        return cl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpServerConfig [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsDhcpHelperIndex=");
        builder.append(topCcmtsDhcpHelperIndex);
        builder.append(", topCcmtsDhcpHelperDeviceType=");
        builder.append(topCcmtsDhcpHelperDeviceType);
        builder.append(", topCcmtsDhcpBundleInterface=");
        builder.append(topCcmtsDhcpBundleInterface);
        builder.append(", topCcmtsDhcpHelperIpAddr=");
        builder.append(topCcmtsDhcpHelperIpAddr);
        builder.append(", topCcmtsDhcpHelperIpAddrLong=");
        builder.append(topCcmtsDhcpHelperIpAddrLong);
        builder.append(", topCcmtsDhcpHelperStatus=");
        builder.append(topCcmtsDhcpHelperStatus);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
