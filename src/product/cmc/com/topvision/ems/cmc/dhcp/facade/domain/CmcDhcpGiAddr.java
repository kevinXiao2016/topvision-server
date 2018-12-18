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
@Alias("cmcDhcpGiAddr")
public class CmcDhcpGiAddr implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7528638038989142040L;
    private Long cmcId;
    private Long entityId;
    private Long giAddrId;
    public static final int DEVICETYPE_CM = 1;
    public static final int DEVICETYPE_HOST = 2;
    public static final int DEVICETYPE_MTA = 3;
    public static final int DEVICETYPE_STB = 4;
    public static final int STATUS_CREATEANDGO = 4;
    public static final int STATUS_DROP = 6;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.2.1.1", index = true)
    private String topCcmtsDhcpBundleInterface;    
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.4.1.1", index = true)
    private Integer topCcmtsDhcpGiAddrDeviceType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.4.1.2", writable = true, type = "IpAddress")
    private String topCcmtsDhcpGiAddress;
    private Long topCcmtsDhcpGiAddressLong;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.3.4.1.3", writable = true, type = "Integer32")
    private Integer topCcmtsDhcpGiAddrStatus;
    private String topCcmtsDhcpGiAddrMask;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpGiAddr [cmcId=");
        builder.append(cmcId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", topCcmtsDhcpBundleInterface=");
        builder.append(topCcmtsDhcpBundleInterface);
        builder.append(", topCcmtsDhcpGiAddrIndex=");
        builder.append(topCcmtsDhcpGiAddrDeviceType);
        builder.append(", topCcmtsDhcpGiAddress=");
        builder.append(topCcmtsDhcpGiAddress);
        builder.append(", topCcmtsDhcpGiAddressLong=");
        builder.append(topCcmtsDhcpGiAddressLong);
        builder.append(", topCcmtsDhcpGiAddrMask=");
        builder.append(", topCcmtsDhcpGiAddrMaskLong=");
        builder.append(", topCcmtsDhcpGiAddrStatus=");
        builder.append(topCcmtsDhcpGiAddrStatus);
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

    public Long getGiAddrId() {
        return giAddrId;
    }

    public void setGiAddrId(Long giAddrId) {
        this.giAddrId = giAddrId;
    }

    /**
     * @return the topCcmtsDhcpGiAddrDeviceType
     */
    public Integer getTopCcmtsDhcpGiAddrDeviceType() {
        return topCcmtsDhcpGiAddrDeviceType;
    }

    /**
     * @param topCcmtsDhcpGiAddrDeviceType the topCcmtsDhcpGiAddrDeviceType to set
     */
    public void setTopCcmtsDhcpGiAddrDeviceType(Integer topCcmtsDhcpGiAddrDeviceType) {
        this.topCcmtsDhcpGiAddrDeviceType = topCcmtsDhcpGiAddrDeviceType;
    }

    /**
     * @return the topCcmtsDhcpGiAddress
     */
    public String getTopCcmtsDhcpGiAddress() {
        return topCcmtsDhcpGiAddress;
    }

    /**
     * @param topCcmtsDhcpGiAddress
     *            the topCcmtsDhcpGiAddress to set
     */
    public void setTopCcmtsDhcpGiAddress(String topCcmtsDhcpGiAddress) {
        this.topCcmtsDhcpGiAddress = topCcmtsDhcpGiAddress;
    }

    /**
     * @return the topCcmtsDhcpGiAddressLong
     */
    public Long getTopCcmtsDhcpGiAddressLong() {
        return topCcmtsDhcpGiAddressLong;
    }

    /**
     * @param topCcmtsDhcpGiAddressLong
     *            the topCcmtsDhcpGiAddressLong to set
     */
    public void setTopCcmtsDhcpGiAddressLong(Long topCcmtsDhcpGiAddressLong) {
        this.topCcmtsDhcpGiAddressLong = topCcmtsDhcpGiAddressLong;
    }

    /**
     * @return the topCcmtsDhcpGiAddrStatus
     */
    public Integer getTopCcmtsDhcpGiAddrStatus() {
        return topCcmtsDhcpGiAddrStatus;
    }

    /**
     * @param topCcmtsDhcpGiAddrStatus
     *            the topCcmtsDhcpGiAddrStatus to set
     */
    public void setTopCcmtsDhcpGiAddrStatus(Integer topCcmtsDhcpGiAddrStatus) {
        this.topCcmtsDhcpGiAddrStatus = topCcmtsDhcpGiAddrStatus;
    }

    public String getTopCcmtsDhcpBundleInterface() {
        return topCcmtsDhcpBundleInterface;
    }

    public void setTopCcmtsDhcpBundleInterface(String topCcmtsDhcpBundleInterface) {
        this.topCcmtsDhcpBundleInterface = topCcmtsDhcpBundleInterface;
    }


    /**
     * @return the topCcmtsDhcpGiAddrMask
     */
    public String getTopCcmtsDhcpGiAddrMask() {
        return topCcmtsDhcpGiAddrMask;
    }

    /**
     * @param topCcmtsDhcpGiAddrMask the topCcmtsDhcpGiAddrMask to set
     */
    public void setTopCcmtsDhcpGiAddrMask(String topCcmtsDhcpGiAddrMask) {
        this.topCcmtsDhcpGiAddrMask = topCcmtsDhcpGiAddrMask;
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

}
