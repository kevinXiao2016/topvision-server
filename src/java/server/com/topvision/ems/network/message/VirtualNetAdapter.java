/***********************************************************************
 * $Id: VirtualNetAdapter.java,v1.0 2012-2-23 下午01:14:28 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsAdapter;

/**
 * @author zhanglongyang
 * @created @2012-2-23-下午01:14:28
 * 
 */
public class VirtualNetAdapter extends EmsAdapter implements VirtualNetListener {
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.VirtualNetListener#cmcAddedInVirtualNet(com.topvision.ems
     * .message.event.VirtualNetEvent)
     */
    @Override
    public void productAddedInVirtualNet(VirtualNetEvent event) {
    }
}
