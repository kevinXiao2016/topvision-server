/***********************************************************************
 * $Id: CmcEntityListener.java,v1.0 2011-11-14 下午04:15:27 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Victor
 * @created @2011-11-14-下午04:15:27
 * 
 */
public interface CmcEntityListener extends EmsListener {
    /**
     * 添加entity
     * 
     * @param event
     */
    void cmcAdded(CmcEntityEvent event);
    
    /**
     * 发现完entity
     * 
     * @param event
     */
    void cmcDiscovered(CmcEntityEvent event);

    /**
     * 设备变化
     * 
     * @param event
     */
    void cmcChanged(CmcEntityEvent event);

    /**
     * 删除Cmc
     * 
     * @param event
     */
    void cmcRemoved(CmcEntityEvent event);
    
    /**
     * 刷新CM信息
     * 
     * @param event
     */
    void cmTopo(CmcEntityEvent event);

    /**
     * 设备管理状态变化
     * 
     * @param event
     */
    void managerChanged(CmcEntityEvent event);
}
