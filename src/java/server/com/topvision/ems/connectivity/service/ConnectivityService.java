/***********************************************************************
 * $Id: ConnectivityService.java,v1.0 2017年9月7日 下午4:11:39 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.connectivity.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.ConnectivityStrategy;
import com.topvision.ems.facade.domain.Entity;

/**
 * @author vanzand
 * @created @2017年9月7日-下午4:11:39
 *
 */
public interface ConnectivityService {
    /**
     * 获取各连通性策略的状态（0：关闭，1：开启）
     * 
     * @return
     */
    Map<String, Integer> getStrategyState();

    /**
     * 保存各连通性策略的状态（0：关闭，1：开启）
     * 
     * @param stratgeryMap
     */
    void saveConnectivityStrategy(Map<String, Integer> stratgeryMap);

    /**
     * 获取当前使用的连通性策略
     * 
     * @return
     */
    List<ConnectivityStrategy> getUsingConnectivityStrategy();

    /**
     * 根据设备唯一性来判断当前设备是否在线
     * 
     * @param delay
     *            如果传递null，将调用ping获取延迟。如果传递非null字符，将在此基础上直接判断
     * @param entity
     * @param currentMac
     * @return
     */
    int checkConnectivityUsingEntityUnique(Entity entity, Integer delay, String currentMac);
}
