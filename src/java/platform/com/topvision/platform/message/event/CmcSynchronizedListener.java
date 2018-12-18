/***********************************************************************
 * $Id: Cmc8800BSynchronizedListener.java,v1.0 2014-1-8 下午2:26:08 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Rod John
 * @created @2014-1-8-下午2:26:09
 * 
 */
public interface CmcSynchronizedListener extends EmsListener {

    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    void insertEntityStates(CmcSynchronizedEvent event);

}
