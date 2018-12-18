/***********************************************************************
 * $Id: BatchAutoDiscoveryService.java,v1.0 2014-5-11 上午11:30:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.BatchAutoDiscoveryEntityType;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoveryPeriod;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.UserContext;

/**
 * @author haojie
 * @created @2014-5-11-上午11:30:44
 *
 */
public interface BatchAutoDiscoveryService extends Service {

    /**
     * 获取所有IP段
     * @return
     */
    List<BatchAutoDiscoveryIps> getIps(Map<String,Object>queryMap);

    /**
     * 获取扫描的所有类型
     * @return
     */
    List<BatchAutoDiscoveryEntityType> getTypeIds();

    /**
     * 获取snmp标签
     * @return
     */
    List<BatchAutoDiscoverySnmpConfig> getSnmpConfigs();

    /**
     * 获取扫描策略
     * @return
     */
    BatchAutoDiscoveryPeriod getPeriod();

    /**
     * 增加IP段
     * @param ips
     */
    void addNetSegment(BatchAutoDiscoveryIps ips);

    /**
     * 修改IP段
     * @param ips
     */
    void modifyNetSegment(BatchAutoDiscoveryIps ips);

    /**
     * 批量删除IP段
     * @param ids
     */
    void batchDeleteNetSegment(List<Long> ids);

    /**
     * 增加snmp标签
     * @param snmpConfig
     */
    void addSnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig);

    /**
     * 修改snmp标签
     */
    void modifySnmpConfig(BatchAutoDiscoverySnmpConfig snmpConfig);

    /**
     * 删除snmp标签
     */
    void deleteSnmpConfig(Long id);

    /**
     * 更新扫描的设备类型
     * @param typeIds
     */
    void modifyTypeIds(List<Long> typeIds);

    /**
     * 更新扫描间隔
     * @param period
     */
    void modifyPeriod(BatchAutoDiscoveryPeriod period);

    /**
     * 手动扫描IP段
     * @param ids
     */
    void scanNetSegment(List<Long> ids, String jconnectID, String operationId, UserContext uc);

    /**
     * 通过ID获取IP段信息
     * @param id
     * @return
     */
    BatchAutoDiscoveryIps getNetSegmentById(Long id);

    /**
     * 通过ID获取一个标签
     * @param id
     * @return
     */
    BatchAutoDiscoverySnmpConfig getSnmpConfigById(Long id);

    /**
     * 修改单个ip段的自动扫描开关
     * @param batchAutoDiscoveryIps
     */
    void modifyAutoDiscovery(BatchAutoDiscoveryIps batchAutoDiscoveryIps);
}
