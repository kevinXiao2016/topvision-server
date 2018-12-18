/***********************************************************************
 * $Id: EponLinkQualityData.java,v1.0 2013-8-6 下午04:45:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.epon.performance.facade.OltPonOptInfoPerf;
import com.topvision.ems.epon.performance.facade.OltSniOptInfoPerf;
import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-8-6-下午04:45:40
 * 
 */
public class EponLinkQualityData implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7135580713841469808L;
    private Long entityId;
    private Long portIndex;
    private String portType;

    private Long optTxPower;
    private Long optCurrent;
    private Long optVoltage;
    private Long optTemp;
    // v单位的电压
    private Float optVoltageUnitV;
    private Long optRePower;
    private Timestamp collectTime;

    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.3.1.1")
    private Float transPower;
    private Float recvPower;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.3.1.2")
    private Float biasCurrent;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.3.1.4")
    private Float workingTemp;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.3.1.3")
    private Float workingVoltage;

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
    }

    /**
     * @return the optTemp
     */
    public Long getOptTemp() {
        return optTemp;
    }

    /**
     * @param optTemp
     *            the optTemp to set
     */
    public void setOptTemp(Long optTemp) {
        this.optTemp = optTemp;
    }

    /**
     * @return the optVoltage
     */
    public Long getOptVoltage() {
        return optVoltage;
    }

    /**
     * @param optVoltage
     *            the optVoltage to set
     */
    public void setOptVoltage(Long optVoltage) {
        if (optVoltage != null) {
            this.optVoltageUnitV = optVoltage / 1000F;
        }
        this.optVoltage = optVoltage;
    }

    /**
     * @return the optCurrent
     */
    public Long getOptCurrent() {
        return optCurrent;
    }

    /**
     * @param optCurrent
     *            the optCurrent to set
     */
    public void setOptCurrent(Long optCurrent) {
        this.optCurrent = optCurrent;
    }

    /**
     * @return the optTxPower
     */
    public Long getOptTxPower() {
        return optTxPower;
    }

    /**
     * @param optTxPower
     *            the optTxPower to set
     */
    public void setOptTxPower(Long optTxPower) {
        this.optTxPower = optTxPower;
    }

    public Long getOptRePower() {
        return optRePower;
    }

    public void setOptRePower(Long optRePower) {
        this.optRePower = optRePower;
    }

    public Float getOptVoltageUnitV() {
        return optVoltageUnitV;
    }

    public void setOptVoltageUnitV(Float optVoltageUnitV) {
        this.optVoltageUnitV = optVoltageUnitV;
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

    public EponLinkQualityData(OltPonOptInfoPerf perf) {
        this.entityId = perf.getEntityId();
        this.portIndex = perf.getPonIndex();
        this.portType = "PON";
        this.optCurrent = perf.getTopPonOptInfoBiasCurrent();
        this.optRePower = perf.getTopPonOptInfoRxPower();
        this.optTemp = perf.getTopPonOptInfoWorkingTemp();
        this.optTxPower = perf.getTopPonOptInfoTxPower();
        this.optVoltage = perf.getTopPonOptInfoWorkingVoltage();
        if (optVoltage != null) {
            this.optVoltageUnitV = optVoltage / 1000F;
        }
        this.collectTime = perf.getCollectTime();
    }

    public EponLinkQualityData(OltSniOptInfoPerf perf) {
        this.entityId = perf.getEntityId();
        this.portIndex = perf.getSniIndex();
        this.portType = "SNI";
        this.optCurrent = perf.getTopSniOptInfoBiasCurrent();
        this.optRePower = perf.getTopSniOptInfoRxPower();
        this.optTemp = perf.getTopSniOptInfoWorkingTemp();
        this.optTxPower = perf.getTopSniOptInfoTxPower();
        this.optVoltage = perf.getTopSniOptInfoWorkingVoltage();
        if (optVoltage != null) {
            this.optVoltageUnitV = optVoltage / 1000F;
        }
        this.collectTime = perf.getCollectTime();
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

    public Float getTransPower() {
        return transPower;
    }

    public void setTransPower(Float transPower) {
        this.transPower = transPower;
    }

    public Float getRecvPower() {
        return recvPower;
    }

    public void setRecvPower(Float recvPower) {
        this.recvPower = recvPower;
    }

    public Float getBiasCurrent() {
        return biasCurrent;
    }

    public void setBiasCurrent(Float biasCurrent) {
        this.biasCurrent = biasCurrent;
    }

    public Float getWorkingTemp() {
        return workingTemp;
    }

    public void setWorkingTemp(Float workingTemp) {
        this.workingTemp = workingTemp;
    }

    public Float getWorkingVoltage() {
        return workingVoltage;
    }

    public void setWorkingVoltage(Float workingVoltage) {
        this.workingVoltage = workingVoltage;
    }

}
