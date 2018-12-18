/***********************************************************************
 * $Id: CmcQosServiceFlowAttribute.java,v1.0 2011-10-31 下午01:43:13 $
 * 
 * @author: Dosion_Huang
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
 * @author Dosion_Huang
 * @created @2011-10-31-下午01:43:13
 * 
 */
@Alias("cmcQosServiceFlowAttribute")
public class CmcQosServiceFlowAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6875503489381486479L;
    private Long sId;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.1", index = true)
    private Long docsQosServiceFlowId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.6")
    private Integer docsQosServiceFlowSID;
    // 方向 1：DownStream 2：UpStream
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.7")
    private Integer docsQosServiceFlowDirection;
    // 1:true 2:false
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.3.1.8")
    private Integer docsQosServiceFlowPrimary;

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
    public Long getDocsQosServiceFlowId() {
        return docsQosServiceFlowId;
    }

    /**
     * @param docsQosServiceFlowId
     *            the docsQosServiceFlowId to set
     */
    public void setDocsQosServiceFlowId(Long docsQosServiceFlowId) {
        this.docsQosServiceFlowId = docsQosServiceFlowId;
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceFlowAttribute [sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsQosServiceFlowId=");
        builder.append(docsQosServiceFlowId);
        builder.append(", docsQosServiceFlowSID=");
        builder.append(docsQosServiceFlowSID);
        builder.append(", docsQosServiceFlowDirection=");
        builder.append(docsQosServiceFlowDirection);
        builder.append(", docsQosServiceFlowPrimary=");
        builder.append(docsQosServiceFlowPrimary);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
