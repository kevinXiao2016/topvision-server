/***********************************************************************
 * $Id: CmcSystemTimeService.java,v1.0 2013-7-18 下午4:06:23 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.systemtime.service;

import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.framework.service.Service;

/**
 * @author dosion
 * @created @2013-7-18-下午4:06:23
 *
 */
public interface CmcSystemTimeService extends Service{
    
    /**
     * 获取系统时间配置
     * @param entityId
     * @return
     */
    CmcSystemTimeConfig getCmcSystemTimeConfig(Long entityId);
    
    /**
     * 修改系统时间配置
     * @param cmcSystemTimeConfig
     * @return
     */
    CmcSystemTimeConfig modifyCmcSystemTimeConfig(CmcSystemTimeConfig cmcSystemTimeConfig);
}
