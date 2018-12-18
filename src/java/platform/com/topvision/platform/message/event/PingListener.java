/***********************************************************************
 * $Id: PingListener.java,v1.0 2012-4-25 下午04:34:52 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author huqiao
 * @created @2012-4-25-下午04:34:52
 * 
 */
public interface PingListener extends EmsListener {
    void pingAction(PingEvent pingEvent);
}
