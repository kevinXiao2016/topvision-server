/***********************************************************************
 * $Id: OltRstpService.java,v1.0 2013-10-25 下午5:33:14 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.rstp.service;

import java.util.List;

import com.topvision.ems.epon.rstp.domain.OltStpGlobalConfig;
import com.topvision.ems.epon.rstp.domain.OltStpPortConfig;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午5:33:14
 *
 */
public interface OltRstpService extends Service {

    /**
     * 获得OLT设备STP配置信息
     * 
     * @param entityId
     * @return oltStpGlobalConfig
     */
    OltStpGlobalConfig getOltStpGlobalConfig(Long entityId);

    /**
     * 更新OLT设备STP配置信息
     * 
     * @param entityId
     * @param oltStpGlobalConfig
     */
    void updateStpGlobalConfig(Long entityId, OltStpGlobalConfig oltStpGlobalConfig);

    /**
     * 更新OLT设备STP使能状态
     * 
     * @param stpGlobalSetEnable
     */
    void setStpGlobalSetEnable(Long entityId, Integer stpGlobalSetEnable);

    /**
     * 获得SNI口STP配置信息
     * 
     * @param portId
     * @return oltStpPortConfig
     */
    OltStpPortConfig getOltStpPortConfig(Long entityId, Long portId);

    /**
     * 更新SNI端口STP配置信息
     * 
     * @param entityId
     * @param oltStpPortConfig
     */
    void updateStpPortConfig(Long entityId, OltStpPortConfig oltStpPortConfig);

    /**
     * 更新SNI端口STP使能状态
     * 
     * @param entityId
     * @param portId
     * @param stpPortEnabled
     */
    void setStpPortEnabled(Long entityId, Long portId, Integer stpPortEnabled);

    /**
     * 更新SNI端口协议迁移状态
     * 
     * @param entityId
     * @param portId
     * @param status
     */
    void setPortRstpProtocolMigration(Long entityId, Long portId, Integer status);

    /**
     * 从设备刷新OLT设备STP配置信息
     * 
     * @param entityId
     */
    void refreshOltStpGlobalConfig(Long entityId);

    /**
     * 从设备刷新SNI端口STP配置信息
     * 
     * @param entityId
     */
    void refreshOltStpPortConfig(Long entityId);

    /**
     * 获取开启端口stp使能端口index
     * 
     * @param SNI端口开启使能端口index
     */
    List<Long> getEnablePortList(Long entityId);

}
