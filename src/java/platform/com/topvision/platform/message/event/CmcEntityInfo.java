/***********************************************************************
 * $Id: CmcEntityInfo.java,v1.0 2015-7-13 下午2:19:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import java.io.Serializable;

/**
 * @author Rod John
 * @created @2015-7-13-下午2:19:47
 *
 */
public class CmcEntityInfo implements Serializable {
    private static final long serialVersionUID = 8848767170754095402L;
    private Long cmcIndex;
    private Long cmcId;
    private Long onuId;
    private Long onuIndex;
    private Long typeId;
    private Long cmcEntityId;

    public CmcEntityInfo(Long cmcId, Long onuId, Long cmcIndex, Long onuIndex, Long typeId, Long cmcEntityId) {
        this.cmcId = cmcId;
        this.onuId = onuId;
        this.cmcIndex = cmcIndex;
        this.onuIndex = onuIndex;
        this.typeId = typeId;
        this.cmcEntityId = cmcEntityId;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the cmcEntityId
     */
    public Long getCmcEntityId() {
        return cmcEntityId;
    }

    /**
     * @param cmcEntityId the cmcEntityId to set
     */
    public void setCmcEntityId(Long cmcEntityId) {
        this.cmcEntityId = cmcEntityId;
    }

}
