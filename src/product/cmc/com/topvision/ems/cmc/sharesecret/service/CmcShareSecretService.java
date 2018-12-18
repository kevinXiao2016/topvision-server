/***********************************************************************
 * $Id: CmcShareSecretService.java,v1.0 2013-7-23 下午2:06:23 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.service;

import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;
import com.topvision.framework.service.Service;

/**
 * @author dosion
 * @created @2013-7-23-下午2:06:23
 *
 */
public interface CmcShareSecretService extends Service {
    
    /**
     * 获取share secret 配置
     * @param cmcId
     * @return
     */
    CmcShareSecretConfig getCmcShareSecretConfig(Long cmcId);
    
    /**
     * 修改share secret配置
     * @param cmcId
     * @param cmcShareSecretConfig
     */
    void modifyCmcShareSecretConfig(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig);

}
