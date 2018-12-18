/***********************************************************************
 * $Id: CmcEntityAdapter.java,v1.0 2011-11-14 下午04:16:56 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;


/**
 * @author Victor
 * @created @2011-11-14-下午04:16:56
 * 
 */
public class CmcEntityAdapter extends EmsAdapter implements CmcEntityListener {
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.CmcEntityListener#cmcAdded(com.topvision.ems.message.event
     * .CmcEntityEvent)
     */
    @Override
    public void cmcAdded(CmcEntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.CmcEntityListener#cmcDiscovered(com.topvision.ems.message
     * .event.CmcEntityEvent)
     */
    @Override
    public void cmcDiscovered(CmcEntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.CmcEntityListener#cmcChanged(com.topvision.ems.message.event
     * .CmcEntityEvent)
     */
    @Override
    public void cmcChanged(CmcEntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.CmcEntityListener#cmcRemoved(com.topvision.ems.message.event
     * .CmcEntityEvent)
     */
    @Override
    public void cmcRemoved(CmcEntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.message.event.CmcEntityListener#managerChanged(com.topvision.ems.message
     * .event.CmcEntityEvent)
     */
    @Override
    public void managerChanged(CmcEntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.CmcEntityListener#cmTopo(com.topvision.platform.message.event.CmcEntityEvent)
     */
    @Override
    public void cmTopo(CmcEntityEvent event) {
        // TODO Auto-generated method stub
        
    }
}
