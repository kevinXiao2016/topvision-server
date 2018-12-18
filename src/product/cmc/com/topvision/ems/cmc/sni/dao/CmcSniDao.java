/***********************************************************************
 * $Id: CmcSniDao.java,v1.0 2013-4-23 下午4:56:01 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.sni.dao;

import java.util.List;

import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.cmc.sni.facade.domain.CmcRateLimit;
import com.topvision.ems.cmc.sni.facade.domain.CmcSniConfig;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2013-4-23-下午4:56:01
 *
 */
public interface CmcSniDao extends BaseEntityDao<Entity>{
    
    /**
     * 获取CMC端口限速
     * @param cmcId
     * @return
     */
    CmcRateLimit queryCmcRateLimit(Long cmcId);

    /**
     * 修改CPU端口限速
     * @param cmcRateLimit
     */
    void updateCmcCpuPortRateLimit(CmcRateLimit cmcRateLimit);

    /**
     * 修改上联口限速
     * @param cmcRateLimit
     */
    void updateCmcSniRateLimit(CmcRateLimit cmcRateLimit);

    /**
     * 获取上联口PHY信息
     * @param cmcId
     * @return
     */
    List<CmcPhyConfig> queryCmcPhyConfigList(Long cmcId);
    
    /**
     * 批量修改上联口PHY信息
     * @param cmcPhyConfigList
     */
    void batchUpdateCmcSniPhyConfig(final List<CmcPhyConfig> cmcPhyConfigList);
    
    /**
     * 获取上联口环回使能
     * @param cmcId
     * @return
     */
    CmcSniConfig queryCmcSniConfig(Long cmcId);

    /**
     * 设置上联口环回使能
     * @param cmcSniConfig
     */
    void updateSniLoopbackStatus(CmcSniConfig cmcSniConfig);
    
    /**
     * 更新风暴抑制参数
     * @param cmcRateLimit
     */
    void updateStormLimitConfig(CmcRateLimit cmcRateLimit);

    /**
     * 批量更新上联口PHY信息
     * 
     * @param cmcPhyConfigList
     */
    void batchInsertOrUpdatePhyConfig(final List<CmcPhyConfig> cmcPhyConfigList);
    
    /**
     * 更新CMC限速表
     * 
     * @param cmcRateLimit
     */
    void insertOrUpdateCmcRateLimit(CmcRateLimit cmcRateLimit);

    /**
     * 更行CMC上联口属性表（环回使能）
     * 
     * @param cmcSniConfig
     */
    void insertOrUpdateCmcSniConfig(CmcSniConfig cmcSniConfig);
}
