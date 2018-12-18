/***********************************************************************
 * $Id: MonitorEvent.java,v 1.1 Sep 21, 2009 12:09:16 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.message;

import com.topvision.ems.performance.domain.Monitor;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @Create Date Sep 21, 2009 12:09:16 PM
 * 
 * @author kelers
 * 
 */
public class MonitorEvent extends EmsEventObject<MonitorListener> {
    private static final long serialVersionUID = -7379051720941410705L;
    private Monitor monitor;

    /**
     * @param source
     */
    public MonitorEvent(Object source) {
        super(source);
    }

    /**
     * @return the monitor
     */
    public final Monitor getMonitor() {
        return monitor;
    }

    /**
     * @param monitor
     *            the monitor to set
     */
    public final void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }
}
