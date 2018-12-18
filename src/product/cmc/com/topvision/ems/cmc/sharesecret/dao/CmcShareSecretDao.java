/***********************************************************************
 * $Id: CmcShareSecretDao.java,v1.0 2013-7-23 下午2:30:29 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sharesecret.dao;

import com.topvision.ems.cmc.sharesecret.facade.domain.CmcShareSecretConfig;

/**
 * @author dosion
 * @created @2013-7-23-下午2:30:29
 *
 */
public interface CmcShareSecretDao {
    
    CmcShareSecretConfig selectCmcShareSecretConfig(Long cmcId);
    
    void updateCmcShareSecretConfig(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig);
    
    /**
     * 插入shareSecret
     * @param cmcId
     * @param cmcShareSecretConfig
     */
    public void insertCmcShareSecret(Long cmcId, CmcShareSecretConfig cmcShareSecretConfig);
    
}
