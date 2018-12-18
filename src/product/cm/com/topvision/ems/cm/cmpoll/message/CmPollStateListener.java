/***********************************************************************
 * $Id: AlertListener.java,v 1.1 Sep 12, 2009 4:37:26 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @Create Date Sep 12, 2009 4:37:26 PM
 * 
 * @author kelers
 * 
 */
public interface CmPollStateListener extends EmsListener {
    /**
     * 一轮完成
     *
     * @param event
     */
    void completeRoundStatistics(CmPollStateEvent event);

    /**
     * 开始一轮采集
     */
    void startRoundStatistics(CmPollStateEvent event);
}
