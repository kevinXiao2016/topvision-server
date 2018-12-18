/***********************************************************************
 * $Id: CmcDiscoveryService.java,v1.0 2015-7-27 上午10:36:52 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service;

import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;

/**
 * @author Administrator
 * @created @2015-7-27-上午10:36:52
 *
 */
public interface CmcDiscoveryService {

    /**
     * 更新CC基本信息
     * @param data
     */
    void syncEntityInfo(CmcDiscoveryData data);

}
