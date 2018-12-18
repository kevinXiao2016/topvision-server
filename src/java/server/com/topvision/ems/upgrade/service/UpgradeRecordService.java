/***********************************************************************
 * $Id: UpgradeRecordService.java,v1.0 2014年9月23日 下午3:39:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import java.util.List;

import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.domain.UpgradeRecordQueryParam;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年9月23日-下午3:39:10
 * 
 */
public interface UpgradeRecordService extends Service {

    /**
     * 获取升级记录
     * 
     * @param upgradeRecordQueryParam
     * @return
     */
    List<UpgradeRecord> getUpgradeRecord(UpgradeRecordQueryParam upgradeRecordQueryParam);

    /**
     * 获取升级记录数目
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
    Long saveUpgradeRecord(UpgradeRecord upgradeRecord);

    /**
     * 获取a型设备entityId
     * 
     * @param index
     * @param entityId
     * @return
     */
    Long getCmcEntityIdByIndexAndEntityId(Long index, Long entityId);
    
    /**
     * 获取ONU entityId
     * 
     * @param index
     * @param entityId
     * @return
     */
    Long getOnuEntityIdByIndexAndEntityId(Long index, Long entityId);

    /**
     * 获取擦cmc版本信息
     * 
     * @param entityId
     * @return
     */
    String getCmcVersionByEntityId(Long entityId);

    /**
     * 获取擦onu版本信息
     * 
     * @param entityId
     * @return
     */
    String getOnuVersionByEntityId(Long entityId);

    /**
     * 获取上联设备名称
     * 
     * @param entityId
     * @return
     */
    String getUplinkEntityName(Long entityId);

}
