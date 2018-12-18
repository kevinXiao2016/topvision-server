/***********************************************************************
 * $Id: DiscoveryService.java,v1.0 2011-6-28 下午07:06:54 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.exception.service.MacConflictException;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-6-28-下午07:06:54
 * 
 */
public interface DiscoveryService<T extends DiscoveryData> extends Service {
    /**
     * 根据设备ID刷新设备，用于添加设备后刷新
     * 
     * @param entityId
     *            设备ID
     * @return 设备Entity
     */
    Entity refresh(Long entityId);

    /**
     * 根据设备ID自动刷新设备，用于设备自动刷新
     * 
     * @param entityId
     *            设备ID
     * @return 设备Entity
     */
    Entity autoRefresh(Entity entity);

    /**
     * 发现设备，根据给定参数获取设备的值
     * 
     * @param param
     *            发现设备所需参数，DeviceParam/SnmpParam/HttpParam等等
     * @return 设备信息，DiscoveryData或者其子类
     */
    T discovery(SnmpParam param);

    /**
     * 采集设备值后更新设备Entity
     * 
     * @param entity
     *            设备Entity
     * @param data
     *            设备信息
     */
    void updateEntity(Entity entity, T data);

    /**
     * 采集设备值后同步内存和相关设备信息
     * 
     * @param entity
     * @param data
     */
    void syncEntityInfo(Entity entity, T data);

    /**
     * 同步性能任务
     * 
     * @param entity
     */
    void syncPerfMoniotr(Entity entity);

    /**
     * 发送同步数据消息
     * 
     * @param entityId
     * @param data
     */
    void sendSynchronizedEvent(Long entityId, T data);

    /**
     * 检测设备连通性
     * 
     * @param entity
     * @param snmpParam
     * @return
     * @throws MacConflictException
     * @throws NetworkException
     */
    int checkConnectivity(Entity entity, SnmpParam snmpParam) throws MacConflictException, NetworkException;
}
