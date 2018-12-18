/***********************************************************************
 * $Id: CmtsEntityListener.java,v1.0 2015-9-14 上午10:11:31 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.event;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author Rod John
 * @created @2015-9-14-上午10:11:31
 *
 */
public interface CmtsEntityListener extends EmsListener {
    /**
     * 添加Cmts
     * 
     * @param event
     */
    void cmtsAdded(CmtsEntityEvent event);

    /**
     * 删除Cmts
     * 
     * @param event
     */
    void cmtsRemoved(CmtsEntityEvent event);

 
}
