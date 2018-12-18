/***********************************************************************
 * $Id: CmcEntityRelation.java,v1.0 2012-2-14 下午01:17:19 $
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
 * @created @2012-2-14-下午01:17:19
 * 
 */
@Alias("cmcEntityRelation")
public class CmcEntityRelation implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5228748107804820617L;
    private Long virtualNetId;
    private Long cmcId;
    private Long onuId;
    private Long cmcIndex;
    private Long onuIndex;
    private Long cmcType;
    private Long cmcEntityId;
    private Long uniIdMain;
    private Long uniIdBack;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcEntityRelation [virtualNetId=");
        builder.append(virtualNetId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", cmcType=");
        builder.append(cmcType);
        builder.append(", cmcEntityId=");
        builder.append(cmcEntityId);
        builder.append(", uniIdMain=");
        builder.append(uniIdMain);
        builder.append(", uniIdBack=");
        builder.append(uniIdBack);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    /**
     * @return the virtualNetId
     */
    public Long getVirtualNetId() {
        return virtualNetId;
    }

    /**
     * @param virtualNetId
     *            the virtualNetId to set
     */
    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
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
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
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
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the cmcType
     */
    public Long getCmcType() {
        return cmcType;
    }

    /**
     * @return the uniIdMain
     */
    public Long getUniIdMain() {
        return uniIdMain;
    }

    /**
     * @param uniIdMain
     *            the uniIdMain to set
     */
    public void setUniIdMain(Long uniIdMain) {
        this.uniIdMain = uniIdMain;
    }

    /**
     * @return the uniIdBack
     */
    public Long getUniIdBack() {
        return uniIdBack;
    }

    /**
     * @param uniIdBack
     *            the uniIdBack to set
     */
    public void setUniIdBack(Long uniIdBack) {
        this.uniIdBack = uniIdBack;
    }

    /**
     * @param cmcType
     *            the cmcType to set
     */
    public void setCmcType(Long cmcType) {
        this.cmcType = cmcType;
    }

    /**
     * @return the cmcEntityId
     */
    public Long getCmcEntityId() {
        return cmcEntityId;
    }

    /**
     * @param cmcEntityId
     *            the cmcEntityId to set
     */
    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg0) {
        CmcEntityRelation tmp = (CmcEntityRelation) arg0;
        if (this.cmcId.equals(tmp.getCmcId())) {
            return true;
        }
        return false;
    }

}
