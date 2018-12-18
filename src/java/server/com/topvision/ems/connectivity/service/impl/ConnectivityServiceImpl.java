/***********************************************************************
 * $Id: ConnectivityServiceImpl.java,v1.0 2017年9月7日 下午4:11:59 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.connectivity.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.connectivity.service.ConnectivityService;
import com.topvision.ems.facade.domain.ConnectivityStrategy;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.performance.handle.ConnectivityHandle;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.MacConflictException;
import com.topvision.framework.common.MacUtils;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author vanzand
 * @created @2017年9月7日-下午4:11:59
 *
 */
@Service("connectivityService")
public class ConnectivityServiceImpl implements ConnectivityService {

    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    protected EntityTypeService entityTypeService;

    @Value("${entity.unique:ip}")
    private String entityUnique;

    // 这里只缓存连通方式，具体配置参数缓存在对应的位置，避免重复缓存，以及带来的一致性问题
    private ConcurrentHashMap<String, Integer> connectStrategyCache = new ConcurrentHashMap<String, Integer>();

    @Override
    public Map<String, Integer> getStrategyState() {
        if (!connectStrategyCache.isEmpty()) {
            return connectStrategyCache;
        }
        connectStrategyCache = new ConcurrentHashMap<String, Integer>();
        List<SystemPreferences> connectStrategyList = systemPreferencesService
                .getSystemPreferences(ConnectivityStrategy.CONNECT_STRATEGY);
        for (SystemPreferences connectStrategy : connectStrategyList) {
            connectStrategyCache.put(connectStrategy.getName(), Integer.valueOf(connectStrategy.getValue()));
        }
        return connectStrategyCache;
    }

    @Override
    public void saveConnectivityStrategy(Map<String, Integer> stratgeryMap) {
        List<SystemPreferences> connectStrategyList = new ArrayList<SystemPreferences>();
        for (String name : stratgeryMap.keySet()) {
            SystemPreferences connectStrategy = new SystemPreferences();
            connectStrategy.setModule(ConnectivityStrategy.CONNECT_STRATEGY);
            connectStrategy.setName(name);
            connectStrategy.setValue(stratgeryMap.get(name).toString());
            connectStrategyList.add(connectStrategy);
        }
        systemPreferencesService.savePreferences(connectStrategyList);
        connectStrategyCache.clear();
    }

    @Override
    public List<ConnectivityStrategy> getUsingConnectivityStrategy() {
        if (connectStrategyCache.isEmpty()) {
            getStrategyState();
        }
        List<ConnectivityStrategy> strategyList = new ArrayList<ConnectivityStrategy>();
        for (String strategyName : connectStrategyCache.keySet()) {
            if (connectStrategyCache.get(strategyName) == 1) {
                ConnectivityStrategy strategy = getStrategy(strategyName);
                strategyList.add(strategy);
            }
        }
        Collections.sort(strategyList);
        return strategyList;
    }

    private ConnectivityStrategy getStrategy(String strategyName) {
        ConnectivityStrategy strategy = new ConnectivityStrategy(strategyName);
        // 获得对应的systemPreferences
        switch (strategyName) {
        case ConnectivityStrategy.ICMP_CONNECT_STRATEGY:
            strategy.setOrder(1);
            strategy.setProperties(systemPreferencesService.getModulePreferences("Ping"));
            break;
        case ConnectivityStrategy.SNMP_CONNECT_STRATEGY:
            strategy.setOrder(2);
            strategy.setProperties(systemPreferencesService.getModulePreferences("Snmp"));
            break;
        case ConnectivityStrategy.TCP_CONNECT_STRATEGY:
            strategy.setOrder(3);
            strategy.setProperties(systemPreferencesService.getModulePreferences("tcp"));
            break;
        }
        return strategy;
    }

    @Override
    public int checkConnectivityUsingEntityUnique(Entity entity, Integer delay, String currentMac) {
        if (delay == null) {
            delay = onlineService.ping(entity.getIp());
        }

        if (delay < 0) {
            return delay;
        }

        // 判断是否需要根据设备唯一性判断
        if (!needCheckEntityUnique(entity)) {
            return delay;
        }

        if (currentMac == null) {
            // 取不到MAC认为不是我司设备返回设备不在线
            return -1;
        }

        // 对在线的设备判断MAC地址的匹配,MAC与数据库一致，保持原来的处理逻辑不变
        // 对entity的mac为空的情况，也不比较
        if (entity.getMac() == null || MacUtils.convertToMaohaoFormat(currentMac).equals(entity.getMac())) {
            return delay;
        }

        // MAC不一致
        if ("ip".equals(entityUnique)) {
            // IP唯一,产生设备被替换告警
            Event replaceEvent = EventSender.getInstance().createEvent(ConnectivityHandle.DEVICE_REPLACE,
                    entity.getIp(), entity.getIp());
            replaceEvent.setEntityId(entity.getEntityId());
            ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.network.resources");
            replaceEvent.setMessage(resourceManager.getString("Device.replace", entity.getName(), entity.getMac(),
                    entity.getName(), currentMac));
            EventSender.getInstance().send(replaceEvent);
            return delay;
        } else {
            // MAC唯一，返回设备不在线
            throw new MacConflictException(
                    String.format("entity mac: %s, reality mac: %s", entity.getMac(), currentMac));
        }
    }

    /**
     * 判断当前entity是否需要根据设备唯一性策略检查
     * 
     * @param entity
     * @return
     */
    private boolean needCheckEntityUnique(Entity entity) {
        return entityTypeService.isCcmtsWithAgent(entity.getTypeId());
    }
}
