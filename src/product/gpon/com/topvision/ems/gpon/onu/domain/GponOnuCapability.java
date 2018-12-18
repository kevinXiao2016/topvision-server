/***********************************************************************
 * $Id: GponOnuCapability.java,v1.0 2016年10月15日 下午4:38:12 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ********************************** *************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年10月15日-下午4:38:12
 *
 */
public class GponOnuCapability implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -1783094059660094058L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.1", index = true)
    private Long onuCapabilityDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.2")
    private Integer onuOMCCVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.3")
    private Integer onuTotalEthNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.4")
    private Integer onuTotalWlanNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.5")
    private Integer onuTotalCatvNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.6")
    private Integer onuTotalVeipNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.7")
    private Integer onuIpHostNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.8")
    private Integer onuTrafficMgmtOption;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.9")
    private Integer onuTotalGEMPortNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.10")
    private Integer onuTotalTContNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.11")
    private String onuConnectCapbility;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.5.1.12")
    private String onuQosFlexibility;
    //拓扑时从topGponOnuCapability中设置过来
    private Integer onuTotalPotsNum;
    private String onuCapabilityStr;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        this.onuCapabilityDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    /**
     * @return the onuCapabilityDeviceIndex
     */
    public Long getOnuCapabilityDeviceIndex() {
        return onuCapabilityDeviceIndex;
    }

    /**
     * @param onuCapabilityDeviceIndex the onuCapabilityDeviceIndex to set
     */
    public void setOnuCapabilityDeviceIndex(Long onuCapabilityDeviceIndex) {
        this.onuCapabilityDeviceIndex = onuCapabilityDeviceIndex;
        this.onuIndex = EponIndex.getOnuIndexByMibIndex(onuCapabilityDeviceIndex);
    }

    /**
     * @return the onuOMCCVersion
     */
    public Integer getOnuOMCCVersion() {
        return onuOMCCVersion;
    }

    /**
     * @param onuOMCCVersion the onuOMCCVersion to set
     */
    public void setOnuOMCCVersion(Integer onuOMCCVersion) {
        this.onuOMCCVersion = onuOMCCVersion;
    }

    /**
     * @return the onuTotalEthNum
     */
    public Integer getOnuTotalEthNum() {
        return onuTotalEthNum;
    }

    /**
     * @param onuTotalEthNum the onuTotalEthNum to set
     */
    public void setOnuTotalEthNum(Integer onuTotalEthNum) {
        this.onuTotalEthNum = onuTotalEthNum;
    }

    /**
     * @return the onuTotalWlanNum
     */
    public Integer getOnuTotalWlanNum() {
        return onuTotalWlanNum;
    }

    /**
     * @param onuTotalWlanNum the onuTotalWlanNum to set
     */
    public void setOnuTotalWlanNum(Integer onuTotalWlanNum) {
        this.onuTotalWlanNum = onuTotalWlanNum;
    }

    /**
     * @return the onuTotalCatvNum
     */
    public Integer getOnuTotalCatvNum() {
        return onuTotalCatvNum;
    }

    /**
     * @param onuTotalCatvNum the onuTotalCatvNum to set
     */
    public void setOnuTotalCatvNum(Integer onuTotalCatvNum) {
        this.onuTotalCatvNum = onuTotalCatvNum;
    }

    /**
     * @return the onuTotalVeipNum
     */
    public Integer getOnuTotalVeipNum() {
        return onuTotalVeipNum;
    }

    /**
     * @param onuTotalVeipNum the onuTotalVeipNum to set
     */
    public void setOnuTotalVeipNum(Integer onuTotalVeipNum) {
        this.onuTotalVeipNum = onuTotalVeipNum;
    }

    /**
     * @return the onuIpHostNum
     */
    public Integer getOnuIpHostNum() {
        return onuIpHostNum;
    }

    /**
     * @param onuIpHostNum the onuIpHostNum to set
     */
    public void setOnuIpHostNum(Integer onuIpHostNum) {
        this.onuIpHostNum = onuIpHostNum;
    }

    /**
     * @return the onuTrafficMgmtOption
     */
    public Integer getOnuTrafficMgmtOption() {
        return onuTrafficMgmtOption;
    }

    /**
     * @param onuTrafficMgmtOption the onuTrafficMgmtOption to set
     */
    public void setOnuTrafficMgmtOption(Integer onuTrafficMgmtOption) {
        this.onuTrafficMgmtOption = onuTrafficMgmtOption;
    }

    /**
     * @return the onuTotalGEMPortNum
     */
    public Integer getOnuTotalGEMPortNum() {
        return onuTotalGEMPortNum;
    }

    /**
     * @param onuTotalGEMPortNum the onuTotalGEMPortNum to set
     */
    public void setOnuTotalGEMPortNum(Integer onuTotalGEMPortNum) {
        this.onuTotalGEMPortNum = onuTotalGEMPortNum;
    }

    /**
     * @return the onuTotalTContNum
     */
    public Integer getOnuTotalTContNum() {
        return onuTotalTContNum;
    }

    /**
     * @param onuTotalTContNum the onuTotalTContNum to set
     */
    public void setOnuTotalTContNum(Integer onuTotalTContNum) {
        this.onuTotalTContNum = onuTotalTContNum;
    }

    /**
     * @return the onuConnectCapbility
     */
    public String getOnuConnectCapbility() {
        return onuConnectCapbility;
    }

    /**
     * @param onuConnectCapbility the onuConnectCapbility to set
     */
    public void setOnuConnectCapbility(String onuConnectCapbility) {
        this.onuConnectCapbility = onuConnectCapbility;
    }

    /**
     * @return the onuQosFlexibility
     */
    public String getOnuQosFlexibility() {
        return onuQosFlexibility;
    }

    /**
     * @param onuQosFlexibility the onuQosFlexibility to set
     */
    public void setOnuQosFlexibility(String onuQosFlexibility) {
        this.onuQosFlexibility = onuQosFlexibility;
    }
    
    
    public Integer getOnuTotalPotsNum() {
        return onuTotalPotsNum;
    }

    public void setOnuTotalPotsNum(Integer onuTotalPotsNum) {
        this.onuTotalPotsNum = onuTotalPotsNum;
    }

    public String getOnuCapabilityStr() {
        StringBuffer str = new StringBuffer();
        if (onuTotalEthNum!=null && onuTotalEthNum != 0) {
            str.append(onuTotalEthNum).append("ETH+");
        }
        if (onuTotalPotsNum!=null && onuTotalPotsNum != 0) {
            str.append(onuTotalPotsNum).append("POTS+");
        }
        if (onuTotalCatvNum!=null && onuTotalCatvNum != 0) {
            if (onuTotalCatvNum == 1) {
                str.append("CATV+");
            } else {
                str.append(onuTotalCatvNum).append("CATV+");
            }
        }
        if (onuTotalWlanNum!=null && onuTotalWlanNum != 0) {
            if (onuTotalWlanNum == 1) {
                str.append("WLAN");
            } else {
                str.append(onuTotalWlanNum).append("WLAN");
            }
        }
        onuCapabilityStr = str.toString();
        if (onuCapabilityStr!=null && !onuCapabilityStr.equals("")) {
            if (onuCapabilityStr.substring(onuCapabilityStr.length() - 1).equals("+")) {
                onuCapabilityStr = onuCapabilityStr.substring(0, onuCapabilityStr.length() - 1);
            }
        }
        return onuCapabilityStr;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GponOnuCapability [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuCapabilityDeviceIndex=");
        builder.append(onuCapabilityDeviceIndex);
        builder.append(", onuOMCCVersion=");
        builder.append(onuOMCCVersion);
        builder.append(", onuTotalEthNum=");
        builder.append(onuTotalEthNum);
        builder.append(", onuTotalWlanNum=");
        builder.append(onuTotalWlanNum);
        builder.append(", onuTotalCatvNum=");
        builder.append(onuTotalCatvNum);
        builder.append(", onuTotalVeipNum=");
        builder.append(onuTotalVeipNum);
        builder.append(", onuIpHostNum=");
        builder.append(onuIpHostNum);
        builder.append(", onuTrafficMgmtOption=");
        builder.append(onuTrafficMgmtOption);
        builder.append(", onuTotalGEMPortNum=");
        builder.append(onuTotalGEMPortNum);
        builder.append(", onuTotalTContNum=");
        builder.append(onuTotalTContNum);
        builder.append(", onuConnectCapbility=");
        builder.append(onuConnectCapbility);
        builder.append(", onuQosFlexibility=");
        builder.append(onuQosFlexibility);
        builder.append(", onuTotalPotsNum=");
        builder.append(onuTotalPotsNum);
        builder.append(", onuCapabilityStr=");
        builder.append(onuCapabilityStr);
        builder.append("]");
        return builder.toString();
    }

}
