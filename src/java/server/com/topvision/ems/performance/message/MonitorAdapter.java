/***********************************************************************
 * $Id: MonitorAdapter.java,v 1.1 Sep 21, 2009 12:10:52 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.message;

import com.topvision.platform.message.event.EmsAdapter;

/**
 * @Create Date Sep 21, 2009 12:10:52 PM
 * 
 * @author kelers
 * 
 */
public class MonitorAdapter extends EmsAdapter implements MonitorListener {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorAdded(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorAdded(MonitorEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorChanged(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorChanged(MonitorEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorDisabled(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorDisabled(MonitorEvent e) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorEnabled(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorEnabled(MonitorEvent e) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorRemoved(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorRemoved(MonitorEvent event) {
    }
}
