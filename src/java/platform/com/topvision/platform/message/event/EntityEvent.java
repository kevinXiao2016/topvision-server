/**
 * 
 */
package com.topvision.platform.message.event;

import com.topvision.ems.facade.domain.Entity;

/**
 * @author niejun
 * 
 */
public class EntityEvent extends EmsEventObject<EntityListener> {
    private static final long serialVersionUID = -7347933492563296187L;

    public static final int UNKNOWN = 0;
    public static final int RENAMED = 5;
    public static final int UPDATED = 3;
    public static final int INSERTED = 1;
    public static final int FIXED = 6;
    public static final int URL = 7;
    private int action = UNKNOWN;
    private Entity entity = null;
    private Boolean enableManager;

    public EntityEvent(Object obj) {
        super(obj);
    }

    public int getAction() {
        return action;
    }

    /**
     * @return the enableManager
     */
    public Boolean getEnableManager() {
        return enableManager;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setAction(int action) {
        this.action = action;
    }

    /**
     * @param enableManager
     *            the enableManager to set
     */
    public void setEnableManager(Boolean enableManager) {
        this.enableManager = enableManager;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
