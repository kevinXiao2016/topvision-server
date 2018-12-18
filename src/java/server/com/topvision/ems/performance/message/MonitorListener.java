/***********************************************************************
 * $Id: MonitorListener.java,v 1.1 Sep 21, 2009 12:08:52 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @Create Date Sep 21, 2009 12:08:52 PM
 * 
 * @author kelers
 * 
 */
public interface MonitorListener extends EmsListener {
    /**
     * 添加monitor
     * 
     * @param event
     */
    void monitorAdded(MonitorEvent event);

    /**
     * 修改monitor
     * 
     * @param event
     */
    void monitorChanged(MonitorEvent event);

    /**
     * 停用一个监视器
     * 
     * @param e
     */
    void monitorDisabled(MonitorEvent e);

    /**
     * 启用监视器
     * 
     * @param e
     */
    void monitorEnabled(MonitorEvent e);

    /**
     * 删除monitor
     * 
     * @param event
     */
    void monitorRemoved(MonitorEvent event);
}
