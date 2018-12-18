/***********************************************************************
 * $Id: EngineServerAdapter.java,v1.0 2015年3月25日 上午8:46:49 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Victor
 * @created @2015年3月25日-上午8:46:49
 *
 */
public class EngineServerAdapter extends EmsAdapter implements EngineServerListener {
    @Override
    public void statusChanged(EngineServerEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("EngineServerAdapter.statusChanged:{}", event);
        }
    }
}
