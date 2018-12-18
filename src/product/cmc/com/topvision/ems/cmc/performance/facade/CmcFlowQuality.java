/***********************************************************************
 * $Id: CmcChannelFlowQuality.java,v1.0 2013-8-13 下午02:20:22 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-8-13-下午02:20:22
 * 
 */
@Alias("cmcFlowQuality")
public class CmcFlowQuality implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6879004777720199193L;
    public static final Integer MACDOMAIN_CMC = 1;
    public static final Integer UPLINK_CMC = 2;
    public static final Integer CHANNEL_CMC = 3;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.3")
    private Integer ifType;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.7")
    private Long ifAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.8")
    private Long ifOperStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.5")
    private Long ifSpeed;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.6")
    private Long ifHCInOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.10")
    private Long ifHCOutOctets;
    @SnmpProperty(oid = "1.3.6.1.2.1.31.1.1.1.15")
    private Long ifHighSpeed;

    // 信道入流量
    private Long ifInOctets;
    // 信道出流量
    private Long ifOutOctets;
    // 信道出入总流量
    private Long ifOctets;
    // 信道利用率，根据信道实际速率和信道速率计算
    private Float ifUtilization;
    // 信道实际速率，根据信道总流量和时间间隔计算
    // 入方向速率
    private Float ifInSpeed;
    // 出方向速率
    private Float ifOutSpeed;
    private Timestamp collectTime;
    private Float ifInUsed;
    private Float ifOutUsed;
    private Integer qamChannelCommonUtilization; //EQAM信道利用率
    private Long inOctets_ = 0L;
    private Long outOctets_ = 0L;

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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
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
     * @return the ifOctets
     */
    public Long getIfOctets() {
        return ifOctets;
    }

    /**
     * @param ifOctets
     *            the ifOctets to set
     */
    public void setIfOctets(Long ifOctets) {
        this.ifOctets = ifOctets;
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
     * @return the ifAdminStatus
     */
    public Long getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Long ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @return the ifOperStatus
     */
    public Long getIfOperStatus() {
        return ifOperStatus;
    }

    /**
     * @param ifOperStatus
     *            the ifOperStatus to set
     */
    public void setIfOperStatus(Long ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
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
     * @return the indexType
     */
    public Integer getIndexType() {
        if ((ifIndex & 0xFFFF) == 0) {
            return MACDOMAIN_CMC;
        }
        if (ifIndex == 1L || ifIndex == 2L) {
            return UPLINK_CMC;
        }
        return CHANNEL_CMC;
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

    public Float getIfInUsed() {
        return ifInUsed;
    }

    public void setIfInUsed(Float ifInUsed) {
        this.ifInUsed = ifInUsed;
    }

    public Float getIfOutUsed() {
        return ifOutUsed;
    }

    public void setIfOutUsed(Float ifOutUsed) {
        this.ifOutUsed = ifOutUsed;
    }

    public Integer getIfType() {
        return ifType;
    }

    public void setIfType(Integer ifType) {
        this.ifType = ifType;
    }

    public Integer getQamChannelCommonUtilization() {
        return qamChannelCommonUtilization;
    }

    public void setQamChannelCommonUtilization(Integer qamChannelCommonUtilization) {
        this.qamChannelCommonUtilization = qamChannelCommonUtilization;
    }

    public Long getInOctets_() {
        return inOctets_;
    }

    public void setInOctets_(Long inOctets_) {
        this.inOctets_ = inOctets_;
    }

    public Long getOutOctets_() {
        return outOctets_;
    }

    public void setOutOctets_(Long outOctets_) {
        this.outOctets_ = outOctets_;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcFlowQuality [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifType=");
        builder.append(ifType);
        builder.append(", ifAdminStatus=");
        builder.append(ifAdminStatus);
        builder.append(", ifOperStatus=");
        builder.append(ifOperStatus);
        builder.append(", ifSpeed=");
        builder.append(ifSpeed);
        builder.append(", ifHCInOctets=");
        builder.append(ifHCInOctets);
        builder.append(", ifHCOutOctets=");
        builder.append(ifHCOutOctets);
        builder.append(", ifHighSpeed=");
        builder.append(ifHighSpeed);
        builder.append(", ifInOctets=");
        builder.append(ifInOctets);
        builder.append(", ifOutOctets=");
        builder.append(ifOutOctets);
        builder.append(", ifOctets=");
        builder.append(ifOctets);
        builder.append(", ifUtilization=");
        builder.append(ifUtilization);
        builder.append(", ifInSpeed=");
        builder.append(ifInSpeed);
        builder.append(", ifOutSpeed=");
        builder.append(ifOutSpeed);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", ifInUsed=");
        builder.append(ifInUsed);
        builder.append(", ifOutUsed=");
        builder.append(ifOutUsed);
        builder.append(", qamChannelCommonUtilization=");
        builder.append(qamChannelCommonUtilization);
        builder.append(", inOctets_=");
        builder.append(inOctets_);
        builder.append(", outOctets_=");
        builder.append(outOctets_);
        builder.append("]");
        return builder.toString();
    }

}
