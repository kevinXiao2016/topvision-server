/***********************************************************************
 * $Id: SynchronizedListener.java,v1.0 2011-10-30 上午09:01:20 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author huqiao
 * @created @2011-10-30-上午09:01:20
 * 
 */
public interface SynchronizedListener extends EmsListener {
    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    void insertEntityStates(SynchronizedEvent event);

    /**
     * 同步设备业务属性
     * 
     * @param event
     */
    void updateEntityStates(SynchronizedEvent event);
}
