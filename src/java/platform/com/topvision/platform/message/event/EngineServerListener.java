/***********************************************************************
 * $Id: EngineServerListener.java,v1.0 2015年3月25日 上午8:46:37 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Victor
 * @created @2015年3月25日-上午8:46:37
 *
 */
public interface EngineServerListener extends EmsListener {
    /**
     * engineServer状态变化都会发出这个通知，具体变化状态在event中可以查看
     * 
     * @param event 携带engineServer和状态
     */
    void statusChanged(EngineServerEvent event);
}
