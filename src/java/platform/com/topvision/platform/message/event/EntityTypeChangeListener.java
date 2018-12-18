/***********************************************************************
 * $Id: EntityTypeChangeListener.java,v1.0 2013-3-23 上午09:55:35 $
 * 
 * @author:  Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author  Rod John
 * @created @2013-3-23-上午09:55:35
 *
 */
public interface EntityTypeChangeListener extends EmsListener {

    /**
     *  设备类型发送变化
     * 
     * @param event
     */
    void entityTypeChange(EntityTypeChangeEvent event);
    
}
