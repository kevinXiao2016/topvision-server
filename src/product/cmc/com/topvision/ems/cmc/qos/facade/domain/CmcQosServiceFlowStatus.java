/***********************************************************************
 * $Id: CmcQosServiceFlowStatus.java,v1.0 2011-11-30 下午01:53:05 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-30-下午01:53:05
 * 
 */
@Alias("cmcQosServiceFlowStatus")
public class CmcQosServiceFlowStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8726611592541386411L;
    private Long sId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.1", index = true)
    private Long flowId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.1")
    private Long flowPkts; // docsQosServiceFlowStatsTable
                           // 中内容，包括下面内容
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.2")
    private Long flowOctets;
    // TODO 需要定义一个对应mib的变量，然后做一个转换
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.3")
    private Long flowTimeCreated;
    // TODO 需要定义一个对应mib的变量，然后做一个转换
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.4")
    private Long flowTimeActive;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.5")
    private Long flowPHSUnknowns;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.6")
    private Long flowPolicedDropPkts;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.4.1.7")
    private Long flowPolicedDelayPkts;

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
     * @return the docsQosServiceFlowId
     */
    public Long getFlowId() {
        return flowId;
    }

    /**
     * @param flowId
     *            the docsQosServiceFlowId to set
     */
    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    /**
     * @return the docsQosServiceFlowPkts
     */
    public Long getFlowPkts() {
        return flowPkts;
    }

    /**
     * @param flowPkts
     *            the docsQosServiceFlowPkts to set
     */
    public void setFlowPkts(Long flowPkts) {
        this.flowPkts = flowPkts;
    }

    /**
     * @return the docsQosServiceFlowOctets
     */
    public Long getFlowOctets() {
        return flowOctets;
    }

    /**
     * @param flowOctets
     *            the docsQosServiceFlowOctets to set
     */
    public void setFlowOctets(Long flowOctets) {
        this.flowOctets = flowOctets;
    }

    /**
     * @return the docsQosServiceFlowTimeCreated
     */
    public Long getFlowTimeCreated() {
        return flowTimeCreated;
    }

    /**
     * @param flowTimeCreated
     *            the docsQosServiceFlowTimeCreated to set
     */
    public void setFlowTimeCreated(Long flowTimeCreated) {
        this.flowTimeCreated = flowTimeCreated;
    }

    /**
     * @return the docsQosServiceFlowTimeActive
     */
    public Long getFlowTimeActive() {
        return flowTimeActive;
    }

    /**
     * @param flowTimeActive
     *            the docsQosServiceFlowTimeActive to set
     */
    public void setFlowTimeActive(Long flowTimeActive) {
        this.flowTimeActive = flowTimeActive;
    }

    /**
     * @return the docsQosServiceFlowPHSUnknowns
     */
    public Long getFlowPHSUnknowns() {
        return flowPHSUnknowns;
    }

    /**
     * @param flowPHSUnknowns
     *            the docsQosServiceFlowPHSUnknowns to set
     */
    public void setFlowPHSUnknowns(Long flowPHSUnknowns) {
        this.flowPHSUnknowns = flowPHSUnknowns;
    }

    /**
     * @return the docsQosServiceFlowPolicedDropPkts
     */
    public Long getFlowPolicedDropPkts() {
        return flowPolicedDropPkts;
    }

    /**
     * @param flowPolicedDropPkts
     *            the docsQosServiceFlowPolicedDropPkts to set
     */
    public void setFlowPolicedDropPkts(Long flowPolicedDropPkts) {
        this.flowPolicedDropPkts = flowPolicedDropPkts;
    }

    /**
     * @return the docsQosServiceFlowPolicedDelayPkts
     */
    public Long getFlowPolicedDelayPkts() {
        return flowPolicedDelayPkts;
    }

    /**
     * @param flowPolicedDelayPkts
     *            the docsQosServiceFlowPolicedDelayPkts to set
     */
    public void setFlowPolicedDelayPkts(Long flowPolicedDelayPkts) {
        this.flowPolicedDelayPkts = flowPolicedDelayPkts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceFlowStatus [sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", flowId=");
        builder.append(flowId);
        builder.append(", flowPkts=");
        builder.append(flowPkts);
        builder.append(", flowOctets=");
        builder.append(flowOctets);
        builder.append(", flowTimeCreated=");
        builder.append(flowTimeCreated);
        builder.append(", flowTimeActive=");
        builder.append(flowTimeActive);
        builder.append(", flowPHSUnknowns=");
        builder.append(flowPHSUnknowns);
        builder.append(", flowPolicedDropPkts=");
        builder.append(flowPolicedDropPkts);
        builder.append(", flowPolicedDelayPkts=");
        builder.append(flowPolicedDelayPkts);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
