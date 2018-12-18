/***********************************************************************
 * $Id: OnuSynchronizedListener.java,v1.0 2015-8-7 上午10:18:09 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.event;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author Rod John
 * @created @2015-8-7-上午10:18:09
 *
 */
public interface OnuSynchronizedListener extends EmsListener {

    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    void insertEntityStates(OnuSynchronizedEvent event);
}
