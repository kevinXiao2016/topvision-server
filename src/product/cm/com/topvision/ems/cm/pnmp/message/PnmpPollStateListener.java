/***********************************************************************
 * $Id: AlertListener.java,v 1.1 Sep 12, 2009 4:37:26 PM kelers Exp $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @Create Date Sep 12, 2009 4:37:26 PM
 * 
 * @author jay
 * 
 */
public interface PnmpPollStateListener extends EmsListener {
    /**
     * 一轮完成
     *
     * @param event
     */
    void completeRoundStatistics(PnmpPollStateEvent event);

    /**
     * 开始一轮采集
     */
    void startRoundStatistics(PnmpPollStateEvent event);
}
