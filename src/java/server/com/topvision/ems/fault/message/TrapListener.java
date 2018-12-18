/***********************************************************************
 * $Id: TrapListener.java,v1.0 2011-11-28 下午03:25:57 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author Victor
 * @created @2011-11-28-下午03:25:57
 * 
 */
public interface TrapListener extends EmsListener {
    /**
     * 收到trap的消息
     * 
     * @param evt
     */
    void onTrapMessage(TrapEvent evt);
}
