/***********************************************************************
 * $Id: ServiceFlowPkgClassRelation.java,v1.0 2011-11-30 下午05:18:51 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-30-下午05:18:51
 * 
 */
@Alias("serviceFlowPkgClassRelation")
public class ServiceFlowPkgClassRelation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6357431702572943789L;
    private Long servicePacketId;
    private Long sId;
    private Long cmcId;
    private Long serviceFlowId;
    private Integer docsQosPktClassId;

    /**
     * @return the servicePacketId
     */
    public Long getServicePacketId() {
        return servicePacketId;
    }

    /**
     * @param servicePacketId
     *            the servicePacketId to set
     */
    public void setServicePacketId(Long servicePacketId) {
        this.servicePacketId = servicePacketId;
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
     * @return the docsQosPktClassId
     */
    public Integer getDocsQosPktClassId() {
        return docsQosPktClassId;
    }

    /**
     * @param docsQosPktClassId
     *            the docsQosPktClassId to set
     */
    public void setDocsQosPktClassId(Integer docsQosPktClassId) {
        this.docsQosPktClassId = docsQosPktClassId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ServiceFlowPkgClassRelation [servicePacketId=");
        builder.append(servicePacketId);
        builder.append(", sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(", docsQosPktClassId=");
        builder.append(docsQosPktClassId);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
