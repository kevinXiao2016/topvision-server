/***********************************************************************
 * $Id: EntityTypeChangeEvent.java,v1.0 2013-3-23 上午09:53:56 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;

/**
 * @author  Rod John
 * @created @2013-3-23-上午09:53:56
 *
 */
public class EntityTypeChangeEvent extends EmsEventObject<EntityTypeChangeListener> {
    private long oldEntityTypeId = EntityType.UNKNOWN_TYPE;
    private long newEntityTypeId = EntityType.UNKNOWN_TYPE;
    private Entity entity;
    
    /**
     * 
     */
    private static final long serialVersionUID = -2475354795996619594L;

    
    /**
     * @param source
     */
    public EntityTypeChangeEvent(Object source) {
        super(source);
    }


    
    /**
     * @return the oldEntityTypeId
     */
    public long getOldEntityTypeId() {
        return oldEntityTypeId;
    }



    /**
     * @param oldEntityTypeId the oldEntityTypeId to set
     */
    public void setOldEntityTypeId(long oldEntityTypeId) {
        this.oldEntityTypeId = oldEntityTypeId;
    }



    /**
     * @return the newEntityTypeId
     */
    public long getNewEntityTypeId() {
        return newEntityTypeId;
    }



    /**
     * @param newEntityTypeId the newEntityTypeId to set
     */
    public void setNewEntityTypeId(long newEntityTypeId) {
        this.newEntityTypeId = newEntityTypeId;
    }



    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }


    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    
    
}
