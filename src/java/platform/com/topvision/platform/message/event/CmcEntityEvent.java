/***********************************************************************
 * $Id: CmcEntityEvent.java,v1.0 2011-11-14 下午04:14:43 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.EntityType;

/**
 * @author Victor
 * @created @2011-11-14-下午04:14:43
 * 
 */
public class CmcEntityEvent extends EmsEventObject<CmcEntityListener> {
    private static final long serialVersionUID = -645631083428204318L;
    private Long entityId;
    private List<Long> cmcIndex = new ArrayList<>();
    private Map<Long, CmcEntityInfo> cmcEntityInfos = new HashMap<Long, CmcEntityInfo>();
    // only use in delete onu info
    private List<Long> del_onuId = new ArrayList<>();

    /**
     * @param source
     */
    public CmcEntityEvent(Object source) {
        super(source);
    }

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
     * @return the cmcIndex
     */
    public List<Long> getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(List<Long> cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the cmcEntityInfos
     */
    public Map<Long, CmcEntityInfo> getCmcEntityInfos() {
        return cmcEntityInfos;
    }

    /**
     * @param cmcEntityInfos the cmcEntityInfos to set
     */
    public void setCmcEntityInfos(Map<Long, CmcEntityInfo> cmcEntityInfos) {
        this.cmcEntityInfos = cmcEntityInfos;
    }

    /**
     * 
     * 
     * @param cmcIndex
     * @param cmcId
     * @param onuIndex
     * @param entityType
     */
    public void addCmcEntity(Long cmcIndex, Long cmcId, Long onuIndex, EntityType entityType) {
        CmcEntityInfo entityInfo = new CmcEntityInfo(cmcId, cmcId, cmcIndex, onuIndex, entityType.getTypeId(), entityId);
        cmcEntityInfos.put(cmcIndex, entityInfo);
        this.cmcIndex.add(cmcIndex);
    }

    /**
     * 
     * 
     * @param onuId
     */
    public void addDelOnuInfo(Long onuId) {
        del_onuId.add(onuId);
    }

    /**
     * @return the del_onuId
     */
    public List<Long> getDel_onuId() {
        return del_onuId;
    }

    /**
     * @param del_onuId the del_onuId to set
     */
    public void setDel_onuId(List<Long> del_onuId) {
        this.del_onuId = del_onuId;
    }

}
