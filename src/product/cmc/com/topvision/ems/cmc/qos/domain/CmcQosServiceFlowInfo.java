/***********************************************************************
 * $Id: CmcQosServiceFlowInfo.java,v1.0 2011-10-20 下午03:46:56 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 服务流信息
 * 
 * @author Dosion_Huang
 * @created @2011-10-20-下午03:46:56
 * 
 */
@Alias("cmcQosServiceFlowInfo")
public class CmcQosServiceFlowInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -266942606226719257L;
    private Long sId;
    private Long cmcId;
    private Long cmcPortId;
    public static final String[] direction = { "", ResourcesUtil.getString("CCMTS.downStream"), ResourcesUtil.getString("CCMTS.upStream") };
    public static final String[] primary = { "", ResourcesUtil.getString("COMMON.yes"), ResourcesUtil.getString("COMMON.no") };
    private String docsQosServiceFlowDirectionString;
    private String docsQosServiceFlowPrimaryString;
    private String docsQosServiceFlowTimeCreatedString;
    private String docsQosServiceFlowTimeActiveString;
    private Long cmcIndex;
    // 服务流ID
    private Long docsQosServiceFlowId;
    // CM MAC
    private String statusMacAddress;
    // SID
    private Integer docsQosServiceFlowSID;
    // 方向1: downStream 2: upstream
    private Integer docsQosServiceFlowDirection;
    // 主要服务流 1：true 2：false
    private Integer docsQosServiceFlowPrimary;
    private Long flowPkts;
    private Long flowOctets;
    private Long flowTimeCreated;
    private Long flowTimeActive;
    private Long flowPHSUnknowns;
    private Long flowPolicedDropPkts;
    private Long flowPolicedDelayPkts;

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
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
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
    public Long getDocsQosServiceFlowId() {
        return docsQosServiceFlowId;
    }

    /**
     * @param flowId
     *            the docsQosServiceFlowId to set
     */
    public void setDocsQosServiceFlowId(Long docsQosServiceFlowId) {
        this.docsQosServiceFlowId = docsQosServiceFlowId;
    }

    /**
     * @return the docsIfCmtsCmStatusMacAddress
     */
    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    /**
     * @param statusMacAddress
     *            the docsIfCmtsCmStatusMacAddress to set
     */
    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    /**
     * @return the docsQosServiceFlowSID
     */
    public Integer getDocsQosServiceFlowSID() {
        return docsQosServiceFlowSID;
    }

    /**
     * @param docsQosServiceFlowSID
     *            the docsQosServiceFlowSID to set
     */
    public void setDocsQosServiceFlowSID(Integer docsQosServiceFlowSID) {
        this.docsQosServiceFlowSID = docsQosServiceFlowSID;
    }

    /**
     * @return the docsQosServiceFlowDirection
     */
    public Integer getDocsQosServiceFlowDirection() {
        return docsQosServiceFlowDirection;
    }

    /**
     * @param docsQosServiceFlowDirection
     *            the docsQosServiceFlowDirection to set
     */
    public void setDocsQosServiceFlowDirection(Integer docsQosServiceFlowDirection) {
        this.docsQosServiceFlowDirection = docsQosServiceFlowDirection;
        if (docsQosServiceFlowDirection != null) {
            this.docsQosServiceFlowDirectionString = direction[docsQosServiceFlowDirection.intValue()];
        }
    }

    /**
     * @return the docsQosServiceFlowPrimary
     */
    public Integer getDocsQosServiceFlowPrimary() {
        return docsQosServiceFlowPrimary;
    }

    /**
     * @param docsQosServiceFlowPrimary
     *            the docsQosServiceFlowPrimary to set
     */
    public void setDocsQosServiceFlowPrimary(Integer docsQosServiceFlowPrimary) {
        this.docsQosServiceFlowPrimary = docsQosServiceFlowPrimary;
        if (docsQosServiceFlowPrimary != null) {
            this.docsQosServiceFlowPrimaryString = primary[docsQosServiceFlowPrimary];
        }
    }

    /**
     * @return the docsQosServiceFlowDirectionString
     */
    public String getDocsQosServiceFlowDirectionString() {
        return docsQosServiceFlowDirectionString;
    }

    /**
     * @param docsQosServiceFlowDirectionString
     *            the docsQosServiceFlowDirectionString to set
     */
    public void setDocsQosServiceFlowDirectionString(String docsQosServiceFlowDirectionString) {
        this.docsQosServiceFlowDirectionString = docsQosServiceFlowDirectionString;
    }

    /**
     * @return the docsQosServiceFlowPrimaryString
     */
    public String getDocsQosServiceFlowPrimaryString() {
        return docsQosServiceFlowPrimaryString;
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
        if (flowTimeCreated != null) {
            Date date = new Date(flowTimeCreated * 10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.docsQosServiceFlowTimeCreatedString = sdf.format(date);
        }
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
        if (flowTimeActive != null) {
            this.docsQosServiceFlowTimeActiveString = CmcUtil.timeFormat(flowTimeActive);
        }
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

    /**
     * @param docsQosServiceFlowPrimaryString
     *            the docsQosServiceFlowPrimaryString to set
     */
    public void setDocsQosServiceFlowPrimaryString(String docsQosServiceFlowPrimaryString) {
        this.docsQosServiceFlowPrimaryString = docsQosServiceFlowPrimaryString;
    }

    /**
     * @return the docsQosServiceFlowTimeCreatedString
     */
    public String getDocsQosServiceFlowTimeCreatedString() {
        return docsQosServiceFlowTimeCreatedString;
    }

    /**
     * @param docsQosServiceFlowTimeCreatedString
     *            the docsQosServiceFlowTimeCreatedString to set
     */
    public void setDocsQosServiceFlowTimeCreatedString(String docsQosServiceFlowTimeCreatedString) {
        this.docsQosServiceFlowTimeCreatedString = docsQosServiceFlowTimeCreatedString;
    }

    /**
     * @return the docsQosServiceFlowTimeActiveString
     */
    public String getDocsQosServiceFlowTimeActiveString() {
        return docsQosServiceFlowTimeActiveString;
    }

    /**
     * @param docsQosServiceFlowTimeActiveString
     *            the docsQosServiceFlowTimeActiveString to set
     */
    public void setDocsQosServiceFlowTimeActiveString(String docsQosServiceFlowTimeActiveString) {
        this.docsQosServiceFlowTimeActiveString = docsQosServiceFlowTimeActiveString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceFlowInfo [sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", docsQosServiceFlowDirectionString=");
        builder.append(docsQosServiceFlowDirectionString);
        builder.append(", docsQosServiceFlowPrimaryString=");
        builder.append(docsQosServiceFlowPrimaryString);
        builder.append(", docsQosServiceFlowTimeCreatedString=");
        builder.append(docsQosServiceFlowTimeCreatedString);
        builder.append(", docsQosServiceFlowTimeActiveString=");
        builder.append(docsQosServiceFlowTimeActiveString);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsQosServiceFlowId=");
        builder.append(docsQosServiceFlowId);
        builder.append(", statusMacAddress=");
        builder.append(statusMacAddress);
        builder.append(", docsQosServiceFlowSID=");
        builder.append(docsQosServiceFlowSID);
        builder.append(", docsQosServiceFlowDirection=");
        builder.append(docsQosServiceFlowDirection);
        builder.append(", docsQosServiceFlowPrimary=");
        builder.append(docsQosServiceFlowPrimary);
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
