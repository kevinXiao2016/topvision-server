/***********************************************************************
 * $Id: AlertListener.java,v 1.1 Sep 12, 2009 4:37:26 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @Create Date Sep 12, 2009 4:37:26 PM
 * 
 * @author kelers
 * 
 */
public interface AlertListener extends EmsListener {
    /**
     * 添加告警
     * 
     * @param event
     */
    void alertAdded(AlertEvent event);

    /**
     * 清除告警
     * 
     * @param event
     */
    void alertCleared(AlertEvent event);

    /**
     * 确认告警
     * 
     * @param event
     */
    void alertConfirmed(AlertEvent event);

    /**
     * 删除告警
     * 
     * @param event
     */
    void alertDeleted(AlertEvent event);
}
