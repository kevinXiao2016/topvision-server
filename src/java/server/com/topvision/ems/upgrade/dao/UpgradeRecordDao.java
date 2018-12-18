/***********************************************************************
 * $Id: UpgradeRecordDao.java,v1.0 2014年9月23日 下午3:22:19 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.dao;

import java.util.List;

import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.domain.UpgradeRecordQueryParam;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:22:19
 * 
 */
public interface UpgradeRecordDao extends BaseEntityDao<UpgradeRecord> {
    /**
     * 查询升级记录
     * 
     * @param upgradeRecordQueryParam
     * @return
     */
    List<UpgradeRecord> getUpgradeRecord(UpgradeRecordQueryParam upgradeRecordQueryParam);

    /**
     * 查询升级记录数目
     * 
     * @param upgradeRecordQueryParam
     * @return
     */
    Long getUpgradeRecordNum(UpgradeRecordQueryParam upgradeRecordQueryParam);

    /**
     * 删除升级记录
     * 
     * @param recordIdList
     */
    void deleteUpgradeRecord(List<Long> recordIdList);

    /**
     * 插入升级记录
     * 
     * @param upgradeRecord
     * @return
     */
    Long insertUpgradeRecord(UpgradeRecord upgradeRecord);

    /**
     * 获取a型设备entityId
     * 
     * @param index
     * @param entityId
     * @return
     */
    Long selectCmcEntityIdByIndexAndEntityId(Long index, Long entityId);

    /**
     * 获取擦cmc版本信息
     * 
     * @param entityId
     * @return
     */
    String selectCmcVersionByEntityId(Long entityId);

    /**
     * 获取擦onu版本信息
     * 
     * @param entityId
     * @return
     */
    String selectOnuVersionByEntityId(Long entityId);

    /**
     * 获取上联设备名称
     * 
     * @param entityId
     * @return
     */
    String selectUplinkEntityName(Long entityId);
    
    /**
     * 获取ONU entityId
     * 
     * @param index
     * @param entityId
     * @return
     */
    Long selectOnuEntityIdByIndexAndEntityId(Long index, Long entityId);

}
