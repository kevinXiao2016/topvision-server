/***********************************************************************
 * $Id: UpgradeRecordServiceImpl.java,v1.0 2014年9月23日 下午3:40:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.upgrade.dao.UpgradeRecordDao;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.domain.UpgradeRecordQueryParam;
import com.topvision.ems.upgrade.service.UpgradeRecordService;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:40:57
 * 
 */
@Service("upgradeRecordService")
public class UpgradeRecordServiceImpl extends BaseService implements UpgradeRecordService {

    @Autowired
    private UpgradeRecordDao upgradeRecordDao;

    @Override
    public List<UpgradeRecord> getUpgradeRecord(UpgradeRecordQueryParam upgradeRecordQueryParam) {
        return upgradeRecordDao.getUpgradeRecord(upgradeRecordQueryParam);
    }

    @Override
    public Long getUpgradeRecordNum(UpgradeRecordQueryParam upgradeRecordQueryParam) {
        return upgradeRecordDao.getUpgradeRecordNum(upgradeRecordQueryParam);
    }

    @Override
    public void deleteUpgradeRecord(List<Long> recordIdList) {
        upgradeRecordDao.deleteUpgradeRecord(recordIdList);
    }

    @Override
    public Long saveUpgradeRecord(UpgradeRecord upgradeRecord) {
        return upgradeRecordDao.insertUpgradeRecord(upgradeRecord);
    }

    @Override
    public Long getCmcEntityIdByIndexAndEntityId(Long index, Long entityId) {
        return upgradeRecordDao.selectCmcEntityIdByIndexAndEntityId(index, entityId);
    }
    
    @Override
    public Long getOnuEntityIdByIndexAndEntityId(Long index, Long entityId) {
        return upgradeRecordDao.selectOnuEntityIdByIndexAndEntityId(index, entityId);
    }

    @Override
    public String getCmcVersionByEntityId(Long entityId) {
        return upgradeRecordDao.selectCmcVersionByEntityId(entityId);
    }

    @Override
    public String getOnuVersionByEntityId(Long entityId) {
        return upgradeRecordDao.selectOnuVersionByEntityId(entityId);
    }

    @Override
    public String getUplinkEntityName(Long entityId) {
        return upgradeRecordDao.selectUplinkEntityName(entityId);
    }

}
