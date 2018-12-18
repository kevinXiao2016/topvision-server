/***********************************************************************
 * $Id: OnuEntityListener.java,v1.0 2015-4-22 上午10:12:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.event;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author flack
 * @created @2015-4-22-上午10:12:26
 *
 */
public interface OnuEntityListener extends EmsListener {
    /**
     * 添加entity
     * 
     * @param event
     */
    void onuAdded(OnuEntityEvent event);

    /**
     * 发现完entity
     * 
     * @param event
     */
    void onuDiscovered(OnuEntityEvent event);

    /**
     * 设备变化
     * 
     * @param event
     */
    void onuChanged(OnuEntityEvent event);

    /**
     * 删除Cmc
     * 
     * @param event
     */
    void onuRemoved(OnuEntityEvent event);

    /**
     * 设备管理状态变化
     * 
     * @param event
     */
    void managerChanged(OnuEntityEvent event);
}
