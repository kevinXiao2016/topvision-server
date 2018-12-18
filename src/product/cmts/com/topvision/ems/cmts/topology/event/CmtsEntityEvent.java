/***********************************************************************
 * $Id: CmtsEntityEvent.java,v1.0 2015-9-14 上午10:08:14 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.event;

import java.util.ArrayList;
import java.util.List;

import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author Rod John
 * @created @2015-9-14-上午10:08:14
 *
 */
public class CmtsEntityEvent extends EmsEventObject<CmtsEntityListener> {

    private static final long serialVersionUID = -4947654672173833821L;
    private Long entityId;
    private List<CmtsEntityInfo> cmtsEntityInfos = new ArrayList<>();

    /**
     * @param source
     */
    public CmtsEntityEvent(Object source) {
        super(source);
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmtsEntityInfos
     */
    public List<CmtsEntityInfo> getCmtsEntityInfos() {
        return cmtsEntityInfos;
    }

    /**
     * @param cmtsEntityInfos the cmtsEntityInfos to set
     */
    public void setCmtsEntityInfos(List<CmtsEntityInfo> cmtsEntityInfos) {
        this.cmtsEntityInfos = cmtsEntityInfos;
    }
    
    public void addCmtsEntityInfo(CmtsEntityInfo cmtsEntityInfo){
        this.cmtsEntityInfos.add(cmtsEntityInfo);
    }

}
