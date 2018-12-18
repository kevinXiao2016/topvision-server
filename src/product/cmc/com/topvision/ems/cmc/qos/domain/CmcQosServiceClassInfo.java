/***********************************************************************
 * $Id: CmcQosServiceClassInfo.java,v1.0 2011-12-7 下午07:00:57 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.upchannel.domain.CmcUpChannelBaseShowInfo;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Administrator
 * @created @2011-12-7-下午07:00:57
 * 
 */
@Alias("cmcQosServiceClassInfo")
public class CmcQosServiceClassInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 587588003553077911L;
    private Long scId;
    private Long entityId;
    private String docsQosServiceClassDirectionString;
    private String docsQosServiceClassStatusString;
    private String className;
    private Integer classStatus;
    private Integer classPriority;
    private Long classMaxTrafficRate;
    private Long classMaxTrafficBurst;
    private Long classMinReservedRate;
    private Long classMinReservedPkt;
    private Integer classDirection;
    private Integer paramSetNum;
    private Integer totalParamSetNum;

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
        this.docsQosServiceClassStatusString = CmcUpChannelBaseShowInfo.STATUSTYPES[classStatus.intValue()];
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
        this.docsQosServiceClassDirectionString = CmcQosServiceFlowInfo.direction[classDirection.intValue()];
    }

    /**
     * @return the paramSetNum
     */
    public Integer getParamSetNum() {
        return paramSetNum;
    }

    /**
     * @param paramSetNum
     *            the paramSetNum to set
     */
    public void setParamSetNum(Integer paramSetNum) {
        this.paramSetNum = paramSetNum;
    }

    /**
     * @return the totalParamSetNum
     */
    public Integer getTotalParamSetNum() {
        return totalParamSetNum;
    }

    /**
     * @param totalParamSetNum
     *            the totalParamSetNum to set
     */
    public void setTotalParamSetNum(Integer totalParamSetNum) {
        this.totalParamSetNum = totalParamSetNum;
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
     * @return the docsQosServiceClassStatusString
     */
    public String getDocsQosServiceClassStatusString() {
        return docsQosServiceClassStatusString;
    }

    /**
     * @param docsQosServiceClassStatusString
     *            the docsQosServiceClassStatusString to set
     */
    public void setDocsQosServiceClassStatusString(String docsQosServiceClassStatusString) {
        this.docsQosServiceClassStatusString = docsQosServiceClassStatusString;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceClassInfo [scId=");
        builder.append(scId);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", docsQosServiceClassDirectionString=");
        builder.append(docsQosServiceClassDirectionString);
        builder.append(", docsQosServiceClassStatusString=");
        builder.append(docsQosServiceClassStatusString);
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
        builder.append(", classDirection=");
        builder.append(classDirection);
        builder.append(", paramSetNum=");
        builder.append(paramSetNum);
        builder.append(", totalParamSetNum=");
        builder.append(totalParamSetNum);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
