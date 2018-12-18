/***********************************************************************
 * $Id: CmcExtAttribute.java,v1.0 2012-2-15 上午09:46:46 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2012-2-15-上午09:46:46
 * 
 */
@Alias("cmcExtAttribute")
public class CmcExtAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 4753699956861694600L;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Integer topCcmtsSniEthInt;
    private Integer topCcmtsSniMainInt;
    private Integer topCcmtsSniBackupInt;

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
     * @return the topCcmtsSniEthInt
     */
    public Integer getTopCcmtsSniEthInt() {
        return topCcmtsSniEthInt;
    }

    /**
     * @param topCcmtsSniEthInt
     *            the topCcmtsSniEthInt to set
     */
    public void setTopCcmtsSniEthInt(Integer topCcmtsSniEthInt) {
        this.topCcmtsSniEthInt = topCcmtsSniEthInt;
    }

    /**
     * @return the topCcmtsSniMainInt
     */
    public Integer getTopCcmtsSniMainInt() {
        return topCcmtsSniMainInt;
    }

    /**
     * @param topCcmtsSniMainInt
     *            the topCcmtsSniMainInt to set
     */
    public void setTopCcmtsSniMainInt(Integer topCcmtsSniMainInt) {
        this.topCcmtsSniMainInt = topCcmtsSniMainInt;
    }

    /**
     * @return the topCcmtsSniBackupInt
     */
    public Integer getTopCcmtsSniBackupInt() {
        return topCcmtsSniBackupInt;
    }

    /**
     * @param topCcmtsSniBackupInt
     *            the topCcmtsSniBackupInt to set
     */
    public void setTopCcmtsSniBackupInt(Integer topCcmtsSniBackupInt) {
        this.topCcmtsSniBackupInt = topCcmtsSniBackupInt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcExtAttribute [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", topCcmtsSniEthInt=");
        builder.append(topCcmtsSniEthInt);
        builder.append(", topCcmtsSniMainInt=");
        builder.append(topCcmtsSniMainInt);
        builder.append(", topCcmtsSniBackupInt=");
        builder.append(topCcmtsSniBackupInt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
