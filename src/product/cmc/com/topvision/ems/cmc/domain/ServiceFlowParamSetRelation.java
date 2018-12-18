/***********************************************************************
 * $Id: ServiceFlowParamSetRelation.java,v1.0 2011-12-12 上午11:33:11 $
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
 * @created @2011-12-12-上午11:33:11
 * 
 */
@Alias("serviceFlowParamSetRelation")
public class ServiceFlowParamSetRelation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4393566435407133938L;
    private Long serviceParamId;
    private Long sId;
    private Long cmcId;
    private Long serviceFlowId;
    private Integer docsQosParamSetType;

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
     * @return the docsQosParamSetType
     */
    public Integer getDocsQosParamSetType() {
        return docsQosParamSetType;
    }

    /**
     * @param docsQosParamSetType
     *            the docsQosParamSetType to set
     */
    public void setDocsQosParamSetType(Integer docsQosParamSetType) {
        this.docsQosParamSetType = docsQosParamSetType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ServiceFlowParamSetRelation [serviceParamId=");
        builder.append(serviceParamId);
        builder.append(", sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(", docsQosParamSetType=");
        builder.append(docsQosParamSetType);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
