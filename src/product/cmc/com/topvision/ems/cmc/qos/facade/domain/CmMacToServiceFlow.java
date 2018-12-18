/***********************************************************************
 * $Id: CmcMacToServiceFlow.java,v1.0 2011-12-12 上午08:41:03 $
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
 * @created @2011-12-12-上午08:41:03
 * 
 */
@Alias("cmMacToServiceFlow")
public class CmMacToServiceFlow implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6781829423488473639L;
    private Long sId;
    private Long cmId;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.11.1.1", index = true)
    private String docsQosCmtsCmMac;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.11.1.2", index = true)
    private Long docsQosCmtsServiceFlowId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.7.1.11.1.3")
    private Long docsQosCmtsIfIndex;

    /**
     * @return the docsQosCmtsCmMac
     */
    public String getDocsQosCmtsCmMac() {
        return docsQosCmtsCmMac;
    }

    /**
     * @param docsQosCmtsCmMac
     *            the docsQosCmtsCmMac to set
     */
    public void setDocsQosCmtsCmMac(String docsQosCmtsCmMac) {
        this.docsQosCmtsCmMac = docsQosCmtsCmMac.toUpperCase();
    }

    /**
     * @return the docsQosCmtsServiceFlowId
     */
    public Long getDocsQosCmtsServiceFlowId() {
        return docsQosCmtsServiceFlowId;
    }

    /**
     * @param docsQosCmtsServiceFlowId
     *            the docsQosCmtsServiceFlowId to set
     */
    public void setDocsQosCmtsServiceFlowId(Long docsQosCmtsServiceFlowId) {
        this.docsQosCmtsServiceFlowId = docsQosCmtsServiceFlowId;
    }

    /**
     * @return the docsQosCmtsIfIndex
     */
    public Long getDocsQosCmtsIfIndex() {
        return docsQosCmtsIfIndex;
    }

    /**
     * @param docsQosCmtsIfIndex
     *            the docsQosCmtsIfIndex to set
     */
    public void setDocsQosCmtsIfIndex(Long docsQosCmtsIfIndex) {
        this.docsQosCmtsIfIndex = docsQosCmtsIfIndex;
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
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmMacToServiceFlow [docsQosCmtsCmMac=");
        builder.append(docsQosCmtsCmMac);
        builder.append(", docsQosCmtsServiceFlowId=");
        builder.append(docsQosCmtsServiceFlowId);
        builder.append(", docsQosCmtsIfIndex=");
        builder.append(docsQosCmtsIfIndex);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
