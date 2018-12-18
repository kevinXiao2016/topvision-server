/***********************************************************************
 * $Id: BfsxEntitySnapService.java,v1.0 2014年9月23日 上午9:28:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

/**
 * @author Bravin
 * @created @2014年9月23日-上午9:28:30
 *
 */
public interface BfsxEntitySnapService {

    /**
     * 刷新snap信息
     * @param entityId
     */
    void refreshSnapInfo(Long entityId, Long typeId);

}
