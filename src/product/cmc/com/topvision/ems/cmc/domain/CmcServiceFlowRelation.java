/***********************************************************************
 * $Id: CmcServiceFlowRelation.java,v1.0 2011-11-30 上午11:25:12 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-30-上午11:25:12
 * 
 */
@Alias("cmcServiceFlowRelation")
public class CmcServiceFlowRelation implements AliasesSuperType {
    private static final long serialVersionUID = 1L;
    private Long sId;
    private Long cmcId;
    private Long serviceFlowId;

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcServiceFlowRelation [sId=");
        builder.append(sId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", serviceFlowId=");
        builder.append(serviceFlowId);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
