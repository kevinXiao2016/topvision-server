/***********************************************************************
 * $Id: CmcQosServiceClass.java,v1.0 2011-12-1 下午06:13:31 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-12-1-下午06:13:31
 * 
 */
@Alias("cmcQosServiceClass")
public class CmcQosServiceClass implements Serializable, AliasesSuperType {
    private Long scId;
    private Long entityId;
    public static String[] RQUESTPOLICY = { "broadcastReqOpp", "priorityReqMulticastReq", "reqDataForReq",
            "reqDataForData", "piggybackReqWithData", "concatenateData", "fragmentData", "suppresspayloadheaders",
            "dropPktsExceedUGSize" };
    private String docsQosServiceClassDirectionString;
    private String docsQosServiceClassRequestPolicyString;
    private static final long serialVersionUID = 3657301157887453575L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.1", index = true)
    private String className;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.3", writable = true, type = "Integer")
    private Integer classStatus;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.4", writable = true, type = "Integer32")
    private Integer classPriority;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.5", writable = true, type = "Unsigned32")
    private Long classMaxTrafficRate;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.6", writable = true, type = "Unsigned32")
    private Long classMaxTrafficBurst;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.7", writable = true, type = "Unsigned32")
    private Long classMinReservedRate;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.8", writable = true, type = "Integer32")
    private Long classMinReservedPkt;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.9", writable = true, type = "Integer32")
    private Long classMaxConcatBurst;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.10", writable = true, type = "Unsigned32")
    private Long classNomPollInterval;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.11", writable = true, type = "Unsigned32")
    private Long classTolPollJitter;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.12", writable = true, type = "Integer32")
    private Long classUnsolicitGrantSize;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.13", writable = true, type = "Unsigned32")
    private Long classNomGrantInterval;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.14", writable = true, type = "Unsigned32")
    private Long classTolGrantJitter;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.15", writable = true, type = "Integer32")
    private Long classGrantsPerInterval;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.16", writable = true, type = "Unsigned32")
    private Long classMaxLatency;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.17", writable = true, type = "Integer32")
    private Long classActiveTimeout;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.18", writable = true, type = "Integer32")
    private Long classAdmittedTimeout;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.19", writable = true, type = "Integer")
    private Integer classSchedulingType;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.20", writable = true, type = "OCTET STRING")
    private String classRequestPolicy;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.21", writable = true, type = "OCTET STRING")
    private String classTosAndMask;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.22", writable = true, type = "OCTET STRING")
    private String classTosOrMask;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.8.1.23", writable = true, type = "Integer")
    private Integer classDirection;

    /**
     * @return the docsQosServiceClassName
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the docsQosServiceClassName to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the docsQosServiceClassStatus
     */
    public Integer getClassStatus() {
        return classStatus;
    }

    /**
     * @param classStatus
     *            the docsQosServiceClassStatus to set
     */
    public void setClassStatus(Integer classStatus) {
        this.classStatus = classStatus;
    }

    /**
     * @return the docsQosServiceClassPriority
     */
    public Integer getClassPriority() {
        return classPriority;
    }

    /**
     * @param classPriority
     *            the docsQosServiceClassPriority to set
     */
    public void setClassPriority(Integer classPriority) {
        this.classPriority = classPriority;
    }

    /**
     * @return the docsQosServiceClassMaxTrafficRate
     */
    public Long getClassMaxTrafficRate() {
        return classMaxTrafficRate;
    }

    /**
     * @param classMaxTrafficRate
     *            the docsQosServiceClassMaxTrafficRate to set
     */
    public void setClassMaxTrafficRate(Long classMaxTrafficRate) {
        this.classMaxTrafficRate = classMaxTrafficRate;
    }

    /**
     * @return the docsQosServiceClassMaxTrafficBurst
     */
    public Long getClassMaxTrafficBurst() {
        return classMaxTrafficBurst;
    }

    /**
     * @param classMaxTrafficBurst
     *            the docsQosServiceClassMaxTrafficBurst to set
     */
    public void setClassMaxTrafficBurst(Long classMaxTrafficBurst) {
        this.classMaxTrafficBurst = classMaxTrafficBurst;
    }

    /**
     * @return the docsQosServiceClassMinReservedRate
     */
    public Long getClassMinReservedRate() {
        return classMinReservedRate;
    }

    /**
     * @param classMinReservedRate
     *            the docsQosServiceClassMinReservedRate to set
     */
    public void setClassMinReservedRate(Long classMinReservedRate) {
        this.classMinReservedRate = classMinReservedRate;
    }

    /**
     * @return the docsQosServiceClassMinReservedPkt
     */
    public Long getClassMinReservedPkt() {
        return classMinReservedPkt;
    }

    /**
     * @param classMinReservedPkt
     *            the docsQosServiceClassMinReservedPkt to set
     */
    public void setClassMinReservedPkt(Long classMinReservedPkt) {
        this.classMinReservedPkt = classMinReservedPkt;
    }

    /**
     * @return the docsQosServiceClassMaxConcatBurst
     */
    public Long getClassMaxConcatBurst() {
        return classMaxConcatBurst;
    }

    /**
     * @param classMaxConcatBurst
     *            the docsQosServiceClassMaxConcatBurst to set
     */
    public void setClassMaxConcatBurst(Long classMaxConcatBurst) {
        this.classMaxConcatBurst = classMaxConcatBurst;
    }

    /**
     * @return the docsQosServiceClassNomPollInterval
     */
    public Long getClassNomPollInterval() {
        return classNomPollInterval;
    }

    /**
     * @param classNomPollInterval
     *            the docsQosServiceClassNomPollInterval to set
     */
    public void setClassNomPollInterval(Long classNomPollInterval) {
        this.classNomPollInterval = classNomPollInterval;
    }

    /**
     * @return the docsQosServiceClassTolPollJitter
     */
    public Long getClassTolPollJitter() {
        return classTolPollJitter;
    }

    /**
     * @param classTolPollJitter
     *            the docsQosServiceClassTolPollJitter to set
     */
    public void setClassTolPollJitter(Long classTolPollJitter) {
        this.classTolPollJitter = classTolPollJitter;
    }

    /**
     * @return the docsQosServiceClassUnsolicitGrantSize
     */
    public Long getClassUnsolicitGrantSize() {
        return classUnsolicitGrantSize;
    }

    /**
     * @param classUnsolicitGrantSize
     *            the docsQosServiceClassUnsolicitGrantSize to set
     */
    public void setClassUnsolicitGrantSize(Long classUnsolicitGrantSize) {
        this.classUnsolicitGrantSize = classUnsolicitGrantSize;
    }

    /**
     * @return the docsQosServiceClassNomGrantInterval
     */
    public Long getClassNomGrantInterval() {
        return classNomGrantInterval;
    }

    /**
     * @param classNomGrantInterval
     *            the docsQosServiceClassNomGrantInterval to set
     */
    public void setClassNomGrantInterval(Long classNomGrantInterval) {
        this.classNomGrantInterval = classNomGrantInterval;
    }

    /**
     * @return the docsQosServiceClassTolGrantJitter
     */
    public Long getClassTolGrantJitter() {
        return classTolGrantJitter;
    }

    /**
     * @param classTolGrantJitter
     *            the docsQosServiceClassTolGrantJitter to set
     */
    public void setClassTolGrantJitter(Long classTolGrantJitter) {
        this.classTolGrantJitter = classTolGrantJitter;
    }

    /**
     * @return the docsQosServiceClassGrantsPerInterval
     */
    public Long getClassGrantsPerInterval() {
        return classGrantsPerInterval;
    }

    /**
     * @param classGrantsPerInterval
     *            the docsQosServiceClassGrantsPerInterval to set
     */
    public void setClassGrantsPerInterval(Long classGrantsPerInterval) {
        this.classGrantsPerInterval = classGrantsPerInterval;
    }

    /**
     * @return the docsQosServiceClassMaxLatency
     */
    public Long getClassMaxLatency() {
        return classMaxLatency;
    }

    /**
     * @param classMaxLatency
     *            the docsQosServiceClassMaxLatency to set
     */
    public void setClassMaxLatency(Long classMaxLatency) {
        this.classMaxLatency = classMaxLatency;
    }

    /**
     * @return the docsQosServiceClassActiveTimeout
     */
    public Long getClassActiveTimeout() {
        return classActiveTimeout;
    }

    /**
     * @param classActiveTimeout
     *            the docsQosServiceClassActiveTimeout to set
     */
    public void setClassActiveTimeout(Long classActiveTimeout) {
        this.classActiveTimeout = classActiveTimeout;
    }

    /**
     * @return the docsQosServiceClassAdmittedTimeout
     */
    public Long getClassAdmittedTimeout() {
        return classAdmittedTimeout;
    }

    /**
     * @param classAdmittedTimeout
     *            the docsQosServiceClassAdmittedTimeout to set
     */
    public void setClassAdmittedTimeout(Long classAdmittedTimeout) {
        this.classAdmittedTimeout = classAdmittedTimeout;
    }

    /**
     * @return the docsQosServiceClassSchedulingType
     */
    public Integer getClassSchedulingType() {
        return classSchedulingType;
    }

    /**
     * @param classSchedulingType
     *            the docsQosServiceClassSchedulingType to set
     */
    public void setClassSchedulingType(Integer classSchedulingType) {
        this.classSchedulingType = classSchedulingType;
    }

    /**
     * @return the docsQosServiceClassRequestPolicy
     */
    public String getClassRequestPolicy() {
        return classRequestPolicy;
    }

    /**
     * @param classRequestPolicy
     *            the docsQosServiceClassRequestPolicy to set
     */
    public void setClassRequestPolicy(String classRequestPolicy) {
        this.classRequestPolicy = classRequestPolicy;
        this.docsQosServiceClassRequestPolicyString = CmcUtil.turnBitsToString(classRequestPolicy, RQUESTPOLICY);
    }

    /**
     * @return the docsQosServiceClassTosAndMask
     */
    public String getClassTosAndMask() {
        return classTosAndMask;
    }

    /**
     * @param classTosAndMask
     *            the docsQosServiceClassTosAndMask to set
     */
    public void setClassTosAndMask(String classTosAndMask) {
        this.classTosAndMask = classTosAndMask;
    }

    /**
     * @return the docsQosServiceClassTosOrMask
     */
    public String getClassTosOrMask() {
        return classTosOrMask;
    }

    /**
     * @param classTosOrMask
     *            the docsQosServiceClassTosOrMask to set
     */
    public void setClassTosOrMask(String classTosOrMask) {
        this.classTosOrMask = classTosOrMask;
    }

    /**
     * @return the docsQosServiceClassDirection
     */
    public Integer getClassDirection() {
        return classDirection;
    }

    /**
     * @param classDirection
     *            the docsQosServiceClassDirection to set
     */
    public void setClassDirection(Integer classDirection) {
        this.classDirection = classDirection;
        this.docsQosServiceClassDirectionString = CmcQosServiceFlowInfo.direction[classDirection];
    }

    /**
     * @return the scId
     */
    public Long getScId() {
        return scId;
    }

    /**
     * @param scId
     *            the scId to set
     */
    public void setScId(Long scId) {
        this.scId = scId;
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

    /**
     * @return the docsQosServiceClassDirectionString
     */
    public String getDocsQosServiceClassDirectionString() {
        return docsQosServiceClassDirectionString;
    }

    /**
     * @param docsQosServiceClassDirectionString
     *            the docsQosServiceClassDirectionString to set
     */
    public void setDocsQosServiceClassDirectionString(String docsQosServiceClassDirectionString) {
        this.docsQosServiceClassDirectionString = docsQosServiceClassDirectionString;
    }

    /**
     * @return the docsQosServiceClassRequestPolicyString
     */
    public String getDocsQosServiceClassRequestPolicyString() {
        return docsQosServiceClassRequestPolicyString;
    }

    /**
     * @param docsQosServiceClassRequestPolicyString
     *            the docsQosServiceClassRequestPolicyString to set
     */
    public void setDocsQosServiceClassRequestPolicyString(String docsQosServiceClassRequestPolicyString) {
        this.docsQosServiceClassRequestPolicyString = docsQosServiceClassRequestPolicyString;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceClass [scId=");
        builder.append(scId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", docsQosServiceClassDirectionString=");
        builder.append(docsQosServiceClassDirectionString);
        builder.append(", docsQosServiceClassRequestPolicyString=");
        builder.append(docsQosServiceClassRequestPolicyString);
        builder.append(", className=");
        builder.append(className);
        builder.append(", classStatus=");
        builder.append(classStatus);
        builder.append(", classPriority=");
        builder.append(classPriority);
        builder.append(", classMaxTrafficRate=");
        builder.append(classMaxTrafficRate);
        builder.append(", classMaxTrafficBurst=");
        builder.append(classMaxTrafficBurst);
        builder.append(", classMinReservedRate=");
        builder.append(classMinReservedRate);
        builder.append(", classMinReservedPkt=");
        builder.append(classMinReservedPkt);
        builder.append(", classMaxConcatBurst=");
        builder.append(classMaxConcatBurst);
        builder.append(", classNomPollInterval=");
        builder.append(classNomPollInterval);
        builder.append(", classTolPollJitter=");
        builder.append(classTolPollJitter);
        builder.append(", classUnsolicitGrantSize=");
        builder.append(classUnsolicitGrantSize);
        builder.append(", classNomGrantInterval=");
        builder.append(classNomGrantInterval);
        builder.append(", classTolGrantJitter=");
        builder.append(classTolGrantJitter);
        builder.append(", classGrantsPerInterval=");
        builder.append(classGrantsPerInterval);
        builder.append(", classMaxLatency=");
        builder.append(classMaxLatency);
        builder.append(", classActiveTimeout=");
        builder.append(classActiveTimeout);
        builder.append(", classAdmittedTimeout=");
        builder.append(classAdmittedTimeout);
        builder.append(", classSchedulingType=");
        builder.append(classSchedulingType);
        builder.append(", classRequestPolicy=");
        builder.append(classRequestPolicy);
        builder.append(", classTosAndMask=");
        builder.append(classTosAndMask);
        builder.append(", classTosOrMask=");
        builder.append(classTosOrMask);
        builder.append(", classDirection=");
        builder.append(classDirection);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
