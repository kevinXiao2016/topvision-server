/***********************************************************************
 * $Id: OltFlowQuality.java,v1.0 2014-3-15 上午10:46:06 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2014-3-15-上午10:46:06
 * 
 */
@Alias("oltFlowQuality")
public class OltFlowQuality implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8128395346248045541L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    private Long portIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.6")
    private Long ifHCInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.10")
    private Long ifHCOutOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.15")
    private Long ifHighSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    // 信道入流量
    private Long ifInOctets;
    // 信道出流量
    private Long ifOutOctets;
    // 信道利用率，根据信道实际速率和信道速率计算
    private Float ifUtilization;
    // 信道实际速率，根据信道总流量和时间间隔计算
    // 入方向速率
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.4.1.1")
    private Float ifInSpeed;
    // 出方向速率
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.4.1.2")
    private Float portInUsed;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.4.1.3")
    private Float ifOutSpeed;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.4.1.4")
    private Float portOutUsed;
    private Timestamp collectTime;
    private Boolean collectStatus;
    // 端口类型(SNI OR PON)
    private String portType;
    //端口入方向利用率
    //端口出方向利用率
    //端口带宽
    private Long portBandwidth;
    //gpon带宽
    private Long UsBandwidth;
    private Long DsBandwidth;

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
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
        /* if (this.portIndex == null) {
             this.portIndex = ((ifIndex & 0xF8000000) << 5) | ((ifIndex & 0x7800000) << 1);
         }*/
        /*
         * if (this.portIndex == null) { if ((this.ifIndex & 0x02) > 0) { this.portIndex = ((ifIndex
         * & 0xF800000) << 1 | 0x02); } else { this.portIndex = ((ifIndex & 0xF8000000) << 5) |
         * ((ifIndex & 0x7800000) << 1); } }
         */
    }

    /**
     * @return the ifHCInOctets
     */
    public Long getIfHCInOctets() {
        return ifHCInOctets;
    }

    /**
     * @param ifHCInOctets
     *            the ifHCInOctets to set
     */
    public void setIfHCInOctets(Long ifHCInOctets) {
        this.ifHCInOctets = ifHCInOctets;
    }

    /**
     * @return the ifHCOutOctets
     */
    public Long getIfHCOutOctets() {
        return ifHCOutOctets;
    }

    /**
     * @param ifHCOutOctets
     *            the ifHCOutOctets to set
     */
    public void setIfHCOutOctets(Long ifHCOutOctets) {
        this.ifHCOutOctets = ifHCOutOctets;
    }

    /**
     * @return the ifHighSpeed
     */
    public Long getIfHighSpeed() {
        return ifHighSpeed;
    }

    /**
     * @param ifHighSpeed
     *            the ifHighSpeed to set
     */
    public void setIfHighSpeed(Long ifHighSpeed) {
        this.ifHighSpeed = ifHighSpeed;
    }

    /**
     * @return the ifSpeed
     */
    public Long getIfSpeed() {
        return ifSpeed;
    }

    /**
     * @param ifSpeed
     *            the ifSpeed to set
     */
    public void setIfSpeed(Long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    /**
     * @return the ifInOctets
     */
    public Long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * @param ifInOctets
     *            the ifInOctets to set
     */
    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    /**
     * @return the ifOutOctets
     */
    public Long getIfOutOctets() {
        return ifOutOctets;
    }

    /**
     * @param ifOutOctets
     *            the ifOutOctets to set
     */
    public void setIfOutOctets(Long ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    /**
     * @return the ifUtilization
     */
    public Float getIfUtilization() {
        return ifUtilization;
    }

    /**
     * @param ifUtilization
     *            the ifUtilization to set
     */
    public void setIfUtilization(Float ifUtilization) {
        this.ifUtilization = ifUtilization;
    }

    /**
     * @return the ifInSpeed
     */
    public Float getIfInSpeed() {
        return ifInSpeed;
    }

    /**
     * @param ifInSpeed
     *            the ifInSpeed to set
     */
    public void setIfInSpeed(Float ifInSpeed) {
        this.ifInSpeed = ifInSpeed;
    }

    /**
     * @return the ifOutSpeed
     */
    public Float getIfOutSpeed() {
        return ifOutSpeed;
    }

    /**
     * @param ifOutSpeed
     *            the ifOutSpeed to set
     */
    public void setIfOutSpeed(Float ifOutSpeed) {
        this.ifOutSpeed = ifOutSpeed;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the collectStatus
     */
    public Boolean getCollectStatus() {
        return collectStatus;
    }

    /**
     * @param collectStatus
     *            the collectStatus to set
     */
    public void setCollectStatus(Boolean collectStatus) {
        this.collectStatus = collectStatus;
    }

    /**
     * @return the portIndex
     */
    public Long getPortIndex() {
        return portIndex;
    }

    /**
     * @param portIndex
     *            the portIndex to set
     */
    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        ifIndex = EponIndex.portIndexToIfIndex(portIndex);
    }

    /**
     * @return the portType
     */
    public String getPortType() {
        return portType;
    }

    /**
     * @param portType
     *            the portType to set
     */
    public void setPortType(String portType) {
        this.portType = portType;
    }

    public Float getPortInUsed() {
        return portInUsed;
    }

    public void setPortInUsed(Float portInUsed) {
        this.portInUsed = portInUsed;
    }

    public Float getPortOutUsed() {
        return portOutUsed;
    }

    public void setPortOutUsed(Float portOutUsed) {
        this.portOutUsed = portOutUsed;
    }

    public Long getPortBandwidth() {
        return portBandwidth;
    }

    public void setPortBandwidth(Long portBandwidth) {
        this.portBandwidth = portBandwidth;
    }

    public Long getUsBandwidth() {
        return UsBandwidth;
    }

    public void setUsBandwidth(Long usBandwidth) {
        UsBandwidth = usBandwidth;
    }

    public Long getDsBandwidth() {
        return DsBandwidth;
    }

    public void setDsBandwidth(Long dsBandwidth) {
        DsBandwidth = dsBandwidth;
    }

}
