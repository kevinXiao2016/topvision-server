/***********************************************************************
 * $Id: OltOnuComAttribute.java,v1.0 2012-12-18 下午14:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2012-12-18-下午14:47:59
 * 
 */
public class OltOnuComAttribute implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 4880596249410187512L;
    private Long entityId;
    private Long onuId;
    private Long onuComId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.1", index = true)
    private Integer topOnuComInfoCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.2", index = true)
    private Integer topOnuComInfoPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.3", index = true)
    private Integer topOnuComInfoOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.4", index = true)
    private Integer topOnuComInfoComIndex;
    private Long onuComIndex;
    private Long onuComRealIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.5", writable = true)
    private String onuComInfoComDesc;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.6", writable = true, type = "Integer32")
    private Integer onuComInfoComType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.7", writable = true, type = "Integer32")
    private Integer onuComInfoBuad;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.8", writable = true, type = "Integer32")
    private Integer onuComInfoDataBits;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.9", writable = true, type = "Integer32")
    private Integer onuComInfoStartBits;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.10", writable = true, type = "Integer32")
    private Integer onuComInfoStopBits;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.11", writable = true, type = "Integer32")
    private Integer onuComInfoParityType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.12", writable = true, type = "IpAddress")
    private String onuComInfoMainRemoteIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.13", writable = true, type = "Integer32")
    private Integer onuComInfoMainRemotePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.14", writable = true, type = "IpAddress")
    private String onuComInfoBackRemoteIp;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.15", writable = true, type = "Integer32")
    private Integer onuComInfoBackRemotePort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.16", writable = true, type = "Integer32")
    private Integer onuComInfoSrvType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.17", writable = true, type = "Integer32")
    private Integer onuComInfoSrvPort;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.10.2.1.18", writable = true, type = "Integer32")
    private Integer onuComInfoClientNum;

    public Long getEntityId() {
        return entityId;
    }

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
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuComId() {
        return onuComId;
    }

    public void setOnuComId(Long onuComId) {
        this.onuComId = onuComId;
    }

    public Integer getTopOnuComInfoCardIndex() {
        return topOnuComInfoCardIndex;
    }

    public void setTopOnuComInfoCardIndex(Integer topOnuComInfoCardIndex) {
        this.topOnuComInfoCardIndex = topOnuComInfoCardIndex;
    }

    public Integer getTopOnuComInfoPonIndex() {
        return topOnuComInfoPonIndex;
    }

    public void setTopOnuComInfoPonIndex(Integer topOnuComInfoPonIndex) {
        this.topOnuComInfoPonIndex = topOnuComInfoPonIndex;
    }

    public Integer getTopOnuComInfoOnuIndex() {
        return topOnuComInfoOnuIndex;
    }

    public void setTopOnuComInfoOnuIndex(Integer topOnuComInfoOnuIndex) {
        this.topOnuComInfoOnuIndex = topOnuComInfoOnuIndex;
    }

    public Integer getTopOnuComInfoComIndex() {
        return topOnuComInfoComIndex;
    }

    public void setTopOnuComInfoComIndex(Integer topOnuComInfoComIndex) {
        this.topOnuComInfoComIndex = topOnuComInfoComIndex;
    }

    public Long getOnuComIndex() {
        if (onuComIndex == null) {
            onuComIndex = EponIndex.getOnuComIndex(topOnuComInfoCardIndex, topOnuComInfoPonIndex,
                    topOnuComInfoOnuIndex, topOnuComInfoComIndex);
        }
        // COM的位置取法和UNI口的位置取法一致
        this.onuComRealIndex = EponIndex.getUniNo(onuComIndex);
        return onuComIndex;
    }

    public void setOnuComIndex(Long onuComIndex) {
        this.topOnuComInfoCardIndex = EponIndex.getSlotNo(onuComIndex).intValue();
        this.topOnuComInfoPonIndex = EponIndex.getPonNo(onuComIndex).intValue();
        this.topOnuComInfoOnuIndex = EponIndex.getOnuNo(onuComIndex).intValue();
        this.topOnuComInfoComIndex = EponIndex.getUniNo(onuComIndex).intValue();
        this.onuComIndex = onuComIndex;
    }

    public String getOnuComInfoComDesc() {
        return onuComInfoComDesc;
    }

    public void setOnuComInfoComDesc(String onuComInfoComDesc) {
        this.onuComInfoComDesc = onuComInfoComDesc;
    }

    public Integer getOnuComInfoComType() {
        return onuComInfoComType;
    }

    public void setOnuComInfoComType(Integer onuComInfoComType) {
        this.onuComInfoComType = onuComInfoComType;
    }

    public Integer getOnuComInfoBuad() {
        return onuComInfoBuad;
    }

    public void setOnuComInfoBuad(Integer onuComInfoBuad) {
        this.onuComInfoBuad = onuComInfoBuad;
    }

    public Integer getOnuComInfoDataBits() {
        return onuComInfoDataBits;
    }

    public void setOnuComInfoDataBits(Integer onuComInfoDataBits) {
        this.onuComInfoDataBits = onuComInfoDataBits;
    }

    public Integer getOnuComInfoStartBits() {
        return onuComInfoStartBits;
    }

    public void setOnuComInfoStartBits(Integer onuComInfoStartBits) {
        this.onuComInfoStartBits = onuComInfoStartBits;
    }

    public Integer getOnuComInfoStopBits() {
        return onuComInfoStopBits;
    }

    public void setOnuComInfoStopBits(Integer onuComInfoStopBits) {
        this.onuComInfoStopBits = onuComInfoStopBits;
    }

    public Integer getOnuComInfoParityType() {
        return onuComInfoParityType;
    }

    public void setOnuComInfoParityType(Integer onuComInfoParityType) {
        this.onuComInfoParityType = onuComInfoParityType;
    }

    public String getOnuComInfoMainRemoteIp() {
        return onuComInfoMainRemoteIp;
    }

    public void setOnuComInfoMainRemoteIp(String onuComInfoMainRemoteIp) {
        this.onuComInfoMainRemoteIp = onuComInfoMainRemoteIp;
    }

    public Integer getOnuComInfoMainRemotePort() {
        return onuComInfoMainRemotePort;
    }

    public void setOnuComInfoMainRemotePort(Integer onuComInfoMainRemotePort) {
        this.onuComInfoMainRemotePort = onuComInfoMainRemotePort;
    }

    public String getOnuComInfoBackRemoteIp() {
        return onuComInfoBackRemoteIp;
    }

    public void setOnuComInfoBackRemoteIp(String onuComInfoBackRemoteIp) {
        this.onuComInfoBackRemoteIp = onuComInfoBackRemoteIp;
    }

    public Integer getOnuComInfoBackRemotePort() {
        return onuComInfoBackRemotePort;
    }

    public void setOnuComInfoBackRemotePort(Integer onuComInfoBackRemotePort) {
        this.onuComInfoBackRemotePort = onuComInfoBackRemotePort;
    }

    public Integer getOnuComInfoSrvType() {
        return onuComInfoSrvType;
    }

    public void setOnuComInfoSrvType(Integer onuComInfoSrvType) {
        this.onuComInfoSrvType = onuComInfoSrvType;
    }

    public Integer getOnuComInfoSrvPort() {
        return onuComInfoSrvPort;
    }

    public void setOnuComInfoSrvPort(Integer onuComInfoSrvPort) {
        this.onuComInfoSrvPort = onuComInfoSrvPort;
    }

    public Integer getOnuComInfoClientNum() {
        return onuComInfoClientNum;
    }

    public void setOnuComInfoClientNum(Integer onuComInfoClientNum) {
        this.onuComInfoClientNum = onuComInfoClientNum;
    }

    /**
     * @return the onuComRealIndex
     */
    public Long getOnuComRealIndex() {
        return onuComRealIndex;
    }

    /**
     * @param onuComRealIndex
     *            the onuComRealIndex to set
     */
    public void setOnuComRealIndex(Long onuComRealIndex) {
        this.onuComRealIndex = onuComRealIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuComAttribute [entityId=");
        builder.append(entityId);
        builder.append(", onuComId=");
        builder.append(onuComId);
        builder.append(", topOnuComInfoCardIndex=");
        builder.append(topOnuComInfoCardIndex);
        builder.append(", topOnuComInfoPonIndex=");
        builder.append(topOnuComInfoPonIndex);
        builder.append(", topOnuComInfoOnuIndex=");
        builder.append(topOnuComInfoOnuIndex);
        builder.append(", topOnuComInfoComIndex=");
        builder.append(topOnuComInfoComIndex);
        builder.append(", onuComIndex=");
        builder.append(onuComIndex);
        builder.append(", onuComInfoComDesc=");
        builder.append(onuComInfoComDesc);
        builder.append(", onuComInfoComType=");
        builder.append(onuComInfoComType);
        builder.append(", onuComInfoBuad=");
        builder.append(onuComInfoBuad);
        builder.append(", onuComInfoDataBits=");
        builder.append(onuComInfoDataBits);
        builder.append(", onuComInfoStartBits=");
        builder.append(onuComInfoStartBits);
        builder.append(", onuComInfoStopBits=");
        builder.append(onuComInfoStopBits);
        builder.append(", onuComInfoParityType=");
        builder.append(onuComInfoParityType);
        builder.append(", onuComInfoMainRemoteIp=");
        builder.append(onuComInfoMainRemoteIp);
        builder.append(", onuComInfoMainRemotePort=");
        builder.append(onuComInfoMainRemotePort);
        builder.append(", onuComInfoBackRemoteIp=");
        builder.append(onuComInfoBackRemoteIp);
        builder.append(", onuComInfoBackRemotePort=");
        builder.append(onuComInfoBackRemotePort);
        builder.append(", onuComInfoSrvType=");
        builder.append(onuComInfoSrvType);
        builder.append(", onuComInfoSrvPort=");
        builder.append(onuComInfoSrvPort);
        builder.append(", onuComInfoClientNum=");
        builder.append(onuComInfoClientNum);
        builder.append("]");
        return builder.toString();
    }

}
