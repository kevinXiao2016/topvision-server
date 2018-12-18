/***********************************************************************
 * $Id: CmcBaseConfigService.java,v1.0 2013-11-1 下午2:47:32 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import com.topvision.framework.service.Service;

/**
 * @author dosion
 * @created @2013-11-1-下午2:47:32
 * 
 */
public interface CmcBaseConfigService extends Service {
    /**
     * 修改CC基本信息
     * 
     * @param entityId
     * @param cmcName
     * @param cmcId
     * @param ccSysLocation
     * @param ccSysContact
     */
    void modifyCcmtsBasicInfo(Long entityId, Long cmcId, String cmcName, String ccSysLocation, String ccSysContact);

    /**
     * CC保存配置(8800B)
     * 
     * @param cmcId
     *            Long 设备ID
     */
    void saveConfig(Long cmcId);
    
    /**
     * 清除配置
     * @param cmcId
     */
    void clearConfig(Long cmcId);
}
