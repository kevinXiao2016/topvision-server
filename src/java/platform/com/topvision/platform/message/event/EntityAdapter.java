/**
 * 
 */
package com.topvision.platform.message.event;

/**
 * @author kelers
 * 
 */
public class EntityAdapter extends EmsAdapter implements EntityListener {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityAdded(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityDiscovered(com.topvision
     * .ems.message .event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#attributeChanged(long,
     *      java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityRemoved(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#managerChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }
}
