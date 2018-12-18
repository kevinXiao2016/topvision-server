/***********************************************************************
 * $Id: OltRstpDao.java,v1.0 2013-10-25 下午5:34:12 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.dao;

import java.util.List;

import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author huqiao
 * @created @2011-12-1-下午02:51:45
 * 
 */
public interface OltRstpDao extends BaseEntityDao<Object> {

    /**
     * 批量插入STP 全局配置
     * 
     * @param entityId
     * @param stpGlobalConfig
     */
    void batchInsertOltStpGlobalConfig(Long entityId, OltStpGlobalConfig stpGlobalConfig);

    /**
     * 批量插入STP 端口配置
     * 
     * @param entityId
     * @param stpPortConfigs
     */
    void batchInsertOltStpPortConfig(Long entityId, final List<OltStpPortConfig> stpPortConfigs);

    /**
     * 获得STP 全局配置
     * 
     * @param entityId
     * @return oltStpGlobalConfig
     */
    OltStpGlobalConfig getOltStpGlobalConfig(Long entityId);

    /**
     * 更新STP 全局使能
     * 
     * @param entityId
     * @param globalEnable
     */
    void updateOltStpGlobalEnable(Long entityId, Integer globalEnable);

    /**
     * 获得STP 指定端口配置信息
     * 
     * @param entityId
     * @param sniId
     * @return oltStpPortConfig
     */
    OltStpPortConfig getOltStpPortConfig(Long entityId, Long sniId);

    /**
     * 更新STP端口使能
     * 
     * @param entityId
     * @param portId
     * @param portEnable
     */
    void updateOltStpPortEnable(Long entityId, Long portId, Integer portEnable);

    /**
     * 更新STP端口迁移状态
     * 
     * @param entityId
     * @param portId
     * @param status
     */
    void updateOltStpPortProtocolMigration(Long entityId, Long portId, Integer status);

    /**
     * 更新STP 全局配置
     * 
     * @param entityId
     * @param globalConfig
     */
    void updateOltStpGlobalConfig(OltStpGlobalConfig oltStpGlobalConfig);

    /**
     * 更新STP端口全局配置
     * 
     * @param entityId
     * @param portConfig
     */
    void updateOltStpPortConfig(Long entityId, OltStpPortConfig portConfig);

    /**
     * 获取开启stp使能端口index
     * 
     * @param entityId
     * @return
     */
    List<Long> getStpEnablePortList(Long entityId);
}
