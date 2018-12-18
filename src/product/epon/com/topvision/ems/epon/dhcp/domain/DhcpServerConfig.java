/***********************************************************************
 * $Id: CmcDhcpServerConfig.java,v1.0 2012-2-13 下午05:03:28 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午05:03:28
 * 
 */
public class DhcpServerConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4117485331544784225L;
    public static String[] DEVICE_TYPE = {"", "CM", "HOST", "MTA", "STB", "ALL"};
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
    private String deviceTypeStr;

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
                result = result + "." + (int) str.charAt(i);
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
        if(topCcmtsDhcpHelperDeviceType != null && topCcmtsDhcpHelperDeviceType >= 1 &&
                topCcmtsDhcpHelperDeviceType <= 5){
            this.deviceTypeStr = DEVICE_TYPE[topCcmtsDhcpHelperDeviceType];
        }
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

    public String getDeviceTypeStr() {
        return deviceTypeStr;
    }

    public void setDeviceTypeStr(String deviceTypeStr) {
        this.deviceTypeStr = deviceTypeStr;
        if(defaultDeviceValue(deviceTypeStr) != -1){
            this.topCcmtsDhcpHelperDeviceType = defaultDeviceValue(deviceTypeStr);
        }
    }

    private int defaultDeviceValue(String device){
        for(int i = 1; i < DEVICE_TYPE.length; i++){
            if(DEVICE_TYPE[i].equalsIgnoreCase(device)){
                return i;
            }
        }
        return -1;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpServerConfig [entityId=");
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
        builder.append("]");
        return builder.toString();
    }
}
