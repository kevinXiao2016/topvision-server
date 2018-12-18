/***********************************************************************
 * $Id: CmcSyslogConfigDao.java,v1.0 2013-4-26 下午3:47:18 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.dao;

import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Administrator
 * @created @2013-4-26-下午3:47:18
 *
 */
public interface CmcDocsisConfigDao extends BaseEntityDao<CmcDocsisConfig> {
    /**
     * 添加CmcDocsisConfig配置
     * 
     * @param cmcDocsis
     */
    void insertCmcDocsisConfig(CmcDocsisConfig cmcDocsis);

    /**
     * 删除CmcDocsisConfig配置
     * 
     * @param entityId
     */
    void deleteCmcDocsisConfig(CmcDocsisConfig cmcDocsis);

    /**
     * 更新CCMTS的DOCSIS 3.0
     * 
     * @param CmcDocsisConfig
     */
    void updateCmcDocsisConfig(CmcDocsisConfig cmcDocsis);

    /**
     * 获取指定的CmcDocsisConfig配置
     * 
     * @param entityId
     * @param ifIndex
     * @return
     */
    CmcDocsisConfig getCmcDocsisConfig(Long entityId, Long ifIndex);
}
