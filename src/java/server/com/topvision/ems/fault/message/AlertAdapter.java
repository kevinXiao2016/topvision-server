/***********************************************************************
 * $Id: AlertAdapter.java,v 1.1 Sep 17, 2009 9:24:45 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message;

import com.topvision.platform.message.event.EmsAdapter;

/**
 * @Create Date Sep 17, 2009 9:24:45 AM
 * 
 * @author kelers
 * 
 */
public class AlertAdapter extends EmsAdapter implements AlertListener {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertAdded(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertAdded(AlertEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertCleared(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertCleared(AlertEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertConfirmed(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertConfirmed(AlertEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.AlertListener#alertDeleted(com.topvision.ems.message.event.AlertEvent)
     */
    @Override
    public void alertDeleted(AlertEvent event) {
    }
}
