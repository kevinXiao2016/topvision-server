/***********************************************************************
 * $Id: SynchronizedAdapter.java,v1.0 2011-10-30 上午09:14:48 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author huqiao
 * @created @2011-10-30-上午09:14:48
 * 
 */
public class SynchronizedAdapter extends EmsAdapter implements SynchronizedListener {
    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.SynchronizedListener#
     * updateEntityStates(com.topvision.ems.message.event.SynchronizedEvent)
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.SynchronizedListener#
     * insertEntityStates(com.topvision.ems.message.event.SynchronizedEvent)
     */
    @Override
    public void insertEntityStates(SynchronizedEvent event) {
    }
}
