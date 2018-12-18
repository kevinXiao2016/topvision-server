/***********************************************************************
 * $Id: CmUpgradeDao.java,v1.0 2016年12月5日 下午6:41:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;

/**
 * @author Rod John
 * @created @2016年12月5日-下午6:41:15
 *
 */
public interface CmUpgradeDao {

    List<CmcUpgradeInfo> selectCmcUpgradeEntityInfo(Map<String, Object> param);

    List<CmUpgradeConfig> loadCmUpgradeConfig();

    void insertCmUpgradeConfig(CmUpgradeConfig cmUpgradeConfig);

    void modifyCmUpgradeConfig(CmUpgradeConfig cmUpgradeConfig);

    void deleteCmUpgradeConfig(Integer id);

    void syncCmModulSoftversion(Map<Long, List<TopCcmtsCmSwVersionTable>> version);

    TopCcmtsCmSwVersionTable selectCmModulSoftversion(Long entityId, Long statusIndex);

    List<String> selectModulList();

    List<TopCcmtsCmSwVersionTable> getCmSwVersionInfo(Long entityId);

}
