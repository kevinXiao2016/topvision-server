/***********************************************************************
 * $Id: EntityValueListener.java,v 1.1 Sep 12, 2009 5:16:24 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @Create Date Sep 12, 2009 5:16:24 PM
 * 
 * @author kelers
 * 
 */
public interface EntityValueListener extends EmsListener {
    /**
     * 告警状态变化
     * 
     * @param event
     */
    void alertChanged(EntityValueEvent event);

    /**
     * 设备的CPU、MEM等负载变化
     * 
     * @param event
     */
    void performanceChanged(EntityValueEvent event);

    /**
     * 在线状态变化
     * 
     * @param event
     */
    void stateChanged(EntityValueEvent event);
}
