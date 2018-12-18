/***********************************************************************
 * $Id: CmcDhcpServerConfig.java,v1.0 2012-2-13 下午05:03:28 $
 * 
 * @author: hds
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.dhcp.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-13-下午05:03:28
 * 
 */
@Alias(value = "dhcpGiAddr")
public class DhcpGiaddrConfig implements AliasesSuperType {
    private static final long serialVersionUID = 7528638038989142040L;
    private Long entityId;
    private Long giAddrId;
    public static String[] DEVICE_TYPE = {"", "CM", "HOST", "MTA", "STB"};
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
    private String deviceTypeStr;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcDhcpGiAddr [entityId=");
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
        builder.append("]");
        return builder.toString();
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
        if(topCcmtsDhcpGiAddrDeviceType != null && topCcmtsDhcpGiAddrDeviceType >= 0
                && topCcmtsDhcpGiAddrDeviceType <= 4){
            this.deviceTypeStr = DEVICE_TYPE[topCcmtsDhcpGiAddrDeviceType];
        }
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

    public String getDeviceTypeStr() {
        return deviceTypeStr;
    }

    public void setDeviceTypeStr(String deviceTypeStr) {
        this.deviceTypeStr = deviceTypeStr;
        if(defaultDeviceValue(deviceTypeStr) != -1){
            this.topCcmtsDhcpGiAddrDeviceType = defaultDeviceValue(deviceTypeStr);
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
                result = result + "." + (int) str.charAt(i);
            }
        }
        return result;
    }

}
