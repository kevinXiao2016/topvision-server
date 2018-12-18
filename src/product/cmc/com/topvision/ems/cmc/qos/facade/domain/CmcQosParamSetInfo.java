/***********************************************************************
 * $Id: CmcQosParamSetInfo.java,v1.0 2011-10-20 下午03:13:44 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * 服务流参数集
 * 
 * @author Dosion_Huang
 * @created @2011-10-20-下午03:13:44
 * 
 */
@Alias("cmcQosParamSetInfo")
public class CmcQosParamSetInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 699300025891650671L;
    private Long serviceParamId;
    private Long cmcId;
    private Long sId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.1", index = true)
    private Long serviceFlowId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.4")
    private String serviceClassName;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.5")
    private Integer priority;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.6")
    private Long maxTrafficRate;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.7")
    private Long maxTrafficBurst;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.8")
    private Long minReservedRate;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.9")
    private Long minReservedPkt;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.10")
    private Long activeTimeout;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.11")
    private Long admittedTimeout;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.12")
    private Long maxConcatBurst;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.13")
    private Integer schedulingType;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.15")
    private Long nomPollInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.16")
    private Long tolPollJitter;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.17")
    private Long unsolicitGrantSize;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.18")
    private Long nomGrantInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.19")
    private Long tolGrantJitter;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.20")
    private Integer grantsPerInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.21")
    private String tosAndMask;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.22")
    private String tosOrMask;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.23")
    private Long maxLatency;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.24", index = true)
    private Integer type;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.25")
    private String requestPolicyOct;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.2.1.26")
    private String bitMap;
    private String docsQosParamSetTypeName;
    private String docsQosParamSetSchedulingTypeName;
    private String docsQosParamSetBitMapName;
    private String docsQosParamSetMaxTrafficRateString;
    private String docsQosParamSetMaxTrafficBurstString;
    private String docsQosParamSetMinReservedRateString;
    private String docsQosParamSetMinReservedPktString;
    private String docsQosParamSetActiveTimeoutString;
    private String docsQosParamSetAdmittedTimeoutString;
    private String docsQosParamSetMaxConcatBurstString;
    private String docsQosParamSetUnsolicitGrantSizeString;
    public static final String[] PARAMSETTYPE = { "", "active", "admitted", "provisioned" };
    public static final String[] SCHEDULINGTYPESTRING = { "", "undefined", "bestEffort", "nonRealTimePollingService",
            "realTimePollingService", "unsolictedGrantServiceWithAD", "unsolictedGrantService" };
    public static final String[] BITMAPSTRING = { "trafficPriority", "maxTrafficRate", "maxTrafficBurst",
            "minReservedRate", "minReservedPkt", "activeTimeout", "admittedTimeout", "maxConcatBurst",
            "schedulingType", "requestPolicy", "nomPollInterval", "tolPollJitter", "unsolicitGrantSize",
            "nomGrantInterval", "tolGrantJitter", "grantsPerInterval", "tosOverwrite", "maxLatency" };

    /**
     * @return the serviceParamId
     */
    public Long getServiceParamId() {
        return serviceParamId;
    }

    /**
     * @param serviceParamId
     *            the serviceParamId to set
     */
    public void setServiceParamId(Long serviceParamId) {
        this.serviceParamId = serviceParamId;
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
     * @return the docsQosParamSetServiceClassName
     */
    public String getServiceClassName() {
        return serviceClassName;
    }

    /**
     * @return the sId
     */
    public Long getsId() {
        return sId;
    }

    /**
     * @param sId
     *            the sId to set
     */
    public void setsId(Long sId) {
        this.sId = sId;
    }

    /**
     * @return the docsQosServiceFlowId
     */
    public Long getServiceFlowId() {
        return serviceFlowId;
    }

    /**
     * @param serviceFlowId
     *            the docsQosServiceFlowId to set
     */
    public void setServiceFlowId(Long serviceFlowId) {
        this.serviceFlowId = serviceFlowId;
    }

    /**
     * @param serviceClassName
     *            the docsQosParamSetServiceClassName to set
     */
    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    /**
     * @return the docsQosParamSetPriority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            the docsQosParamSetPriority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return the docsQosParamSetMaxTrafficRate
     */
    public Long getMaxTrafficRate() {
        return maxTrafficRate;
    }

    /**
     * @param maxTrafficRate
     *            the docsQosParamSetMaxTrafficRate to set
     */
    public void setMaxTrafficRate(Long maxTrafficRate) {
        this.maxTrafficRate = maxTrafficRate;
        this.docsQosParamSetMaxTrafficRateString = CmcUtil.turnToEndWithKOrM(maxTrafficRate.intValue()) + "bps";
    }

    /**
     * @return the docsQosParamSetMaxTrafficBurst
     */
    public Long getMaxTrafficBurst() {
        return maxTrafficBurst;
    }

    /**
     * @param maxTrafficBurst
     *            the docsQosParamSetMaxTrafficBurst to set
     */
    public void setMaxTrafficBurst(Long maxTrafficBurst) {
        this.maxTrafficBurst = maxTrafficBurst;
        this.docsQosParamSetMaxTrafficBurstString = CmcUtil.turnToEndWithKOrM(maxTrafficBurst.intValue()) + "B";
    }

    /**
     * @return the docsQosParamSetMinReservedRate
     */
    public Long getMinReservedRate() {
        return minReservedRate;
    }

    /**
     * @param minReservedRate
     *            the docsQosParamSetMinReservedRate to set
     */
    public void setMinReservedRate(Long minReservedRate) {
        this.minReservedRate = minReservedRate;
        this.docsQosParamSetMinReservedRateString = CmcUtil.turnToEndWithKOrM(minReservedRate.intValue()) + "bps";
    }

    /**
     * @return the docsQosParamSetMinReservedPkt
     */
    public Long getMinReservedPkt() {
        return minReservedPkt;
    }

    /**
     * @param minReservedPkt
     *            the docsQosParamSetMinReservedPkt to set
     */
    public void setMinReservedPkt(Long minReservedPkt) {
        this.minReservedPkt = minReservedPkt;
        this.docsQosParamSetMinReservedPktString = CmcUtil.turnToEndWithKOrM(minReservedPkt.intValue()) + "B";
    }

    /**
     * @return the docsQosParamSetActiveTimeout
     */
    public Long getActiveTimeout() {
        return activeTimeout;
    }

    /**
     * @param activeTimeout
     *            the docsQosParamSetActiveTimeout to set
     */
    public void setActiveTimeout(Long activeTimeout) {
        this.activeTimeout = activeTimeout;
        this.docsQosParamSetActiveTimeoutString = CmcUtil.timeFormat(activeTimeout.intValue());
    }

    /**
     * @return the docsQosParamSetAdmittedTimeout
     */
    public Long getAdmittedTimeout() {
        return admittedTimeout;
    }

    /**
     * @param admittedTimeout
     *            the docsQosParamSetAdmittedTimeout to set
     */
    public void setAdmittedTimeout(Long admittedTimeout) {
        this.admittedTimeout = admittedTimeout;
        this.docsQosParamSetAdmittedTimeoutString = CmcUtil.timeFormat(admittedTimeout.intValue());
    }

    /**
     * @return the docsQosParamSetMaxConcatBurst
     */
    public Long getMaxConcatBurst() {
        return maxConcatBurst;
    }

    /**
     * @param maxConcatBurst
     *            the docsQosParamSetMaxConcatBurst to set
     */
    public void setMaxConcatBurst(Long maxConcatBurst) {
        this.maxConcatBurst = maxConcatBurst;
        this.docsQosParamSetMaxConcatBurstString = CmcUtil.turnToEndWithKOrM(maxConcatBurst.intValue()) + "B";
    }

    /**
     * @return the docsQosParamSetSchedulingType
     */
    public Integer getSchedulingType() {
        return schedulingType;
    }

    /**
     * @param schedulingType
     *            the docsQosParamSetSchedulingType to set
     */
    public void setSchedulingType(Integer schedulingType) {
        this.schedulingType = schedulingType;
    }

    /**
     * @return the docsQosParamSetNomPollInterval
     */
    public Long getNomPollInterval() {
        return nomPollInterval;
    }

    /**
     * @param nomPollInterval
     *            the docsQosParamSetNomPollInterval to set
     */
    public void setNomPollInterval(Long nomPollInterval) {
        this.nomPollInterval = nomPollInterval;
    }

    /**
     * @return the docsQosParamSetTolPollJitter
     */
    public Long getTolPollJitter() {
        return tolPollJitter;
    }

    /**
     * @param tolPollJitter
     *            the docsQosParamSetTolPollJitter to set
     */
    public void setTolPollJitter(Long tolPollJitter) {
        this.tolPollJitter = tolPollJitter;
    }

    /**
     * @return the docsQosParamSetUnsolicitGrantSize
     */
    public Long getUnsolicitGrantSize() {
        return unsolicitGrantSize;
    }

    /**
     * @param unsolicitGrantSize
     *            the docsQosParamSetUnsolicitGrantSize to set
     */
    public void setUnsolicitGrantSize(Long unsolicitGrantSize) {
        this.unsolicitGrantSize = unsolicitGrantSize;
        this.docsQosParamSetUnsolicitGrantSizeString = CmcUtil.turnToEndWithKOrM(unsolicitGrantSize) + "B";
    }

    /**
     * @return the docsQosParamSetNomGrantInterval
     */
    public Long getNomGrantInterval() {
        return nomGrantInterval;
    }

    /**
     * @param nomGrantInterval
     *            the docsQosParamSetNomGrantInterval to set
     */
    public void setNomGrantInterval(Long nomGrantInterval) {
        this.nomGrantInterval = nomGrantInterval;
    }

    /**
     * @return the docsQosParamSetTolGrantJitter
     */
    public Long getTolGrantJitter() {
        return tolGrantJitter;
    }

    /**
     * @param tolGrantJitter
     *            the docsQosParamSetTolGrantJitter to set
     */
    public void setTolGrantJitter(Long tolGrantJitter) {
        this.tolGrantJitter = tolGrantJitter;
    }

    /**
     * @return the docsQosParamSetGrantsPerInterval
     */
    public Integer getGrantsPerInterval() {
        return grantsPerInterval;
    }

    /**
     * @param grantsPerInterval
     *            the docsQosParamSetGrantsPerInterval to set
     */
    public void setGrantsPerInterval(Integer grantsPerInterval) {
        this.grantsPerInterval = grantsPerInterval;
    }

    /**
     * @return the docsQosParamSetTosAndMask
     */
    public String getTosAndMask() {
        return tosAndMask;
    }

    /**
     * @param tosAndMask
     *            the docsQosParamSetTosAndMask to set
     */
    public void setTosAndMask(String tosAndMask) {
        this.tosAndMask = tosAndMask;
    }

    /**
     * @return the docsQosParamSetTosOrMask
     */
    public String getTosOrMask() {
        return tosOrMask;
    }

    /**
     * @param tosOrMask
     *            the docsQosParamSetTosOrMask to set
     */
    public void setTosOrMask(String tosOrMask) {
        this.tosOrMask = tosOrMask;
    }

    /**
     * @return the docsQosParamSetMaxLatency
     */
    public Long getMaxLatency() {
        return maxLatency;
    }

    /**
     * @param maxLatency
     *            the docsQosParamSetMaxLatency to set
     */
    public void setMaxLatency(Long maxLatency) {
        this.maxLatency = maxLatency;
    }

    /**
     * @return the docsQosParamSetType
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            the docsQosParamSetType to set
     */
    public void setType(Integer type) {
        this.type = type;
        if (this.type != null) {
            this.docsQosParamSetTypeName = PARAMSETTYPE[type];
        }
    }

    /**
     * @return the docsQosParamSetRequestPolicyOct
     */
    public String getRequestPolicyOct() {
        return requestPolicyOct;
    }

    /**
     * @param requestPolicyOct
     *            the docsQosParamSetRequestPolicyOct to set
     */
    public void setRequestPolicyOct(String requestPolicyOct) {
        this.requestPolicyOct = requestPolicyOct;
    }

    /**
     * @return the docsQosParamSetBitMap
     */
    public String getBitMap() {
        return bitMap;
    }

    /**
     * @param docsQosParamSetBitMap
     *            the docsQosParamSetBitMap to set
     */
    public void setBitMap(String docsQosParamSetBitMap) {
        this.bitMap = docsQosParamSetBitMap;
    }

    /**
     * @return the docsQosParamSetTypeName
     */
    public String getDocsQosParamSetTypeName() {
        return docsQosParamSetTypeName;
    }

    /**
     * @return the docsQosParamSetSchedulingTypeName
     */
    public String getDocsQosParamSetSchedulingTypeName() {
        if (this.schedulingType != null) {
            this.docsQosParamSetSchedulingTypeName = SCHEDULINGTYPESTRING[schedulingType];
        }
        return docsQosParamSetSchedulingTypeName;
    }

    /**
     * @return the docsQosParamSetBitMapName
     */
    public String getDocsQosParamSetBitMapName() {
        if (bitMap != null) {
            this.docsQosParamSetBitMapName = CmcUtil.turnBitsToString(bitMap, BITMAPSTRING);
        }
        return docsQosParamSetBitMapName;
    }

    /**
     * @return the docsQosParamSetMaxTrafficRateString
     */
    public String getDocsQosParamSetMaxTrafficRateString() {
        return docsQosParamSetMaxTrafficRateString;
    }

    /**
     * @return the docsQosParamSetMaxTrafficBurstString
     */
    public String getDocsQosParamSetMaxTrafficBurstString() {
        return docsQosParamSetMaxTrafficBurstString;
    }

    /**
     * @return the docsQosParamSetMinReservedRateString
     */
    public String getDocsQosParamSetMinReservedRateString() {
        return docsQosParamSetMinReservedRateString;
    }

    /**
     * @return the docsQosParamSetMinReservedPktString
     */
    public String getDocsQosParamSetMinReservedPktString() {
        return docsQosParamSetMinReservedPktString;
    }

    /**
     * @return the docsQosParamSetActiveTimeoutString
     */
    public String getDocsQosParamSetActiveTimeoutString() {
        return docsQosParamSetActiveTimeoutString;
    }

    /**
     * @return the docsQosParamSetAdmittedTimeoutString
     */
    public String getDocsQosParamSetAdmittedTimeoutString() {
        return docsQosParamSetAdmittedTimeoutString;
    }

    /**
     * @return the docsQosParamSetMaxConcatBurstString
     */
    public String getDocsQosParamSetMaxConcatBurstString() {
        return docsQosParamSetMaxConcatBurstString;
    }

    /**
     * @return the docsQosParamSetUnsolicitGrantSizeString
     */
    public String getDocsQosParamSetUnsolicitGrantSizeString() {
        return docsQosParamSetUnsolicitGrantSizeString;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosParamSetInfo [serviceParamId=");
        builder.append(serviceParamId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", sId=");
        builder.append(sId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(", serviceClassName=");
        builder.append(serviceClassName);
        builder.append(", priority=");
        builder.append(priority);
        builder.append(", maxTrafficRate=");
        builder.append(maxTrafficRate);
        builder.append(", maxTrafficBurst=");
        builder.append(maxTrafficBurst);
        builder.append(", minReservedRate=");
        builder.append(minReservedRate);
        builder.append(", minReservedPkt=");
        builder.append(minReservedPkt);
        builder.append(", activeTimeout=");
        builder.append(activeTimeout);
        builder.append(", admittedTimeout=");
        builder.append(admittedTimeout);
        builder.append(", maxConcatBurst=");
        builder.append(maxConcatBurst);
        builder.append(", schedulingType=");
        builder.append(schedulingType);
        builder.append(", nomPollInterval=");
        builder.append(nomPollInterval);
        builder.append(", tolPollJitter=");
        builder.append(tolPollJitter);
        builder.append(", unsolicitGrantSize=");
        builder.append(unsolicitGrantSize);
        builder.append(", nomGrantInterval=");
        builder.append(nomGrantInterval);
        builder.append(", tolGrantJitter=");
        builder.append(tolGrantJitter);
        builder.append(", grantsPerInterval=");
        builder.append(grantsPerInterval);
        builder.append(", tosAndMask=");
        builder.append(tosAndMask);
        builder.append(", tosOrMask=");
        builder.append(tosOrMask);
        builder.append(", maxLatency=");
        builder.append(maxLatency);
        builder.append(", type=");
        builder.append(type);
        builder.append(", requestPolicyOct=");
        builder.append(requestPolicyOct);
        builder.append(", docsQosParamSetBitMap=");
        builder.append(bitMap);
        builder.append(", docsQosParamSetTypeName=");
        builder.append(docsQosParamSetTypeName);
        builder.append(", docsQosParamSetSchedulingTypeName=");
        builder.append(docsQosParamSetSchedulingTypeName);
        builder.append(", docsQosParamSetBitMapName=");
        builder.append(docsQosParamSetBitMapName);
        builder.append(", docsQosParamSetMaxTrafficRateString=");
        builder.append(docsQosParamSetMaxTrafficRateString);
        builder.append(", docsQosParamSetMaxTrafficBurstString=");
        builder.append(docsQosParamSetMaxTrafficBurstString);
        builder.append(", docsQosParamSetMinReservedRateString=");
        builder.append(docsQosParamSetMinReservedRateString);
        builder.append(", docsQosParamSetMinReservedPktString=");
        builder.append(docsQosParamSetMinReservedPktString);
        builder.append(", docsQosParamSetActiveTimeoutString=");
        builder.append(docsQosParamSetActiveTimeoutString);
        builder.append(", docsQosParamSetAdmittedTimeoutString=");
        builder.append(docsQosParamSetAdmittedTimeoutString);
        builder.append(", docsQosParamSetMaxConcatBurstString=");
        builder.append(docsQosParamSetMaxConcatBurstString);
        builder.append(", docsQosParamSetUnsolicitGrantSizeString=");
        builder.append(docsQosParamSetUnsolicitGrantSizeString);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}