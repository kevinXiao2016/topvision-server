/***********************************************************************
 * $Id: VirtualNetListener.java,v1.0 2012-2-23 下午01:10:52 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author zhanglongyang
 * @created @2012-2-23-下午01:10:52
 * 
 */
public interface VirtualNetListener extends EmsListener {
    /**
     * 子网中添加
     * 
     * @param event
     */
    void productAddedInVirtualNet(VirtualNetEvent event);
}
