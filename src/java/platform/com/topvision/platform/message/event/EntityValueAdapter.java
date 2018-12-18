/***********************************************************************
 * $Id: EntityValueAdapter.java,v 1.1 Sep 17, 2009 9:13:27 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @Create Date Sep 17, 2009 9:13:27 AM
 * 
 * @author kelers
 * 
 */
public class EntityValueAdapter extends EmsAdapter implements EntityValueListener {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#alertChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void alertChanged(EntityValueEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#performanceChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void performanceChanged(EntityValueEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityValueListener#stateChanged(com.topvision.ems.message.event.EntityValueEvent)
     */
    @Override
    public void stateChanged(EntityValueEvent event) {
    }
}
