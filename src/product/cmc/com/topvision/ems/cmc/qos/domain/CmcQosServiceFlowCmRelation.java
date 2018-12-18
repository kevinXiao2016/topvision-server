/***********************************************************************
 * $Id: CmcQosServiceFlowCmRelation.java,v1.0 2011-10-31 下午01:56:32 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;

/**
 * @author Dosion_Huang
 * @created @2011-10-31-下午01:56:32
 * 
 */
public class CmcQosServiceFlowCmRelation implements Serializable {
    private static final long serialVersionUID = -3356161097605724932L;
    private Long sId;
    private String mac;
    private Long serviceFlowId;
    private Long cmcIndex;

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
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the serviceFlowId
     */
    public Long getServiceFlowId() {
        return serviceFlowId;
    }

    /**
     * @param serviceFlowId
     *            the serviceFlowId to set
     */
    public void setServiceFlowId(Long serviceFlowId) {
        this.serviceFlowId = serviceFlowId;
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
        builder.append("CmcQosServiceFlowCmRelation [sId=");
        builder.append(sId);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
