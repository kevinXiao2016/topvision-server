/***********************************************************************
 * $Id: CmtsXFlowQuality.java,v1.0 2014-4-17 上午9:38:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2014-4-17-上午9:38:17
 * 
 */
public class CmtsXFlowQuality implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -2670992854951844447L;
    private Long entityId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
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
    private Long inOctets;
    // 信道出流量
    private Long outOctets;
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
    private Integer ifIndexType;

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
     * @return the inOctets
     */
    public Long getInOctets() {
        return inOctets;
    }

    /**
     * @param inOctets
     *            the inOctets to set
     */
    public void setInOctets(Long inOctets) {
        this.inOctets = inOctets;
    }

    /**
     * @return the outOctets
     */
    public Long getOutOctets() {
        return outOctets;
    }

    /**
     * @param outOctets
     *            the outOctets to set
     */
    public void setOutOctets(Long outOctets) {
        this.outOctets = outOctets;
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
     * @return the ifIndexType
     */
    public Integer getIfIndexType() {
        return ifIndexType;
    }

    /**
     * @param ifIndexType
     *            the ifIndexType to set
     */
    public void setIfIndexType(Integer ifIndexType) {
        this.ifIndexType = ifIndexType;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CmtsXFlowQuality [entityId=" + entityId + ", cmcId=" + cmcId + ", ifIndex=" + ifIndex
                + ", ifAdminStatus=" + ifAdminStatus + ", ifOperStatus=" + ifOperStatus + ", ifSpeed=" + ifSpeed
                + ", ifHCInOctets=" + ifHCInOctets + ", ifHCOutOctets=" + ifHCOutOctets + ", ifHighSpeed="
                + ifHighSpeed + ", inOctets=" + inOctets + ", outOctets=" + outOctets + ", ifOctets=" + ifOctets
                + ", ifUtilization=" + ifUtilization + ", ifInSpeed=" + ifInSpeed + ", ifOutSpeed=" + ifOutSpeed
                + ", collectTime=" + collectTime + "]";
    }

}
