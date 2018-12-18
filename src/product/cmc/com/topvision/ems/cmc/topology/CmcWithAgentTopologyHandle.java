/***********************************************************************
 * $Id: CmcWithAgentTopologyHandle.java,v1.0 2017年9月20日 上午11:22:26 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.domain.BatchDiscoveryInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.TopoHandlerProperty;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.parser.AbstractTopologyHandle;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.service.OperationService;

/**
 * @author vanzand
 * @created @2017年9月20日-上午11:22:26
 *
 */
public class CmcWithAgentTopologyHandle extends AbstractTopologyHandle {

    private final static String CC_WITHAGENT_MAC_OID = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.12.142671872";

    private final static int MAC_DUPLICATE_ALERT_ID = -103;

    static final ConcurrentMap<String, ReentrantLock> entityLockHolder = new ConcurrentHashMap<String, ReentrantLock>();

    @Autowired
    protected OperationService operationService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.parser.TopologyHandle#handleTopoResult(com.topvision.ems.facade.
     * domain.BatchDiscoveryInfo)
     */
    @Override
    public String handleTopoResult(BatchDiscoveryInfo info, Entity newEntity) {
        String result = super.handleTopoResult(info, newEntity);
        if (!SUCCESS.equals(result)) {
            return result;
        }

        String ip = info.getIpAddress();
        String mac = info.getProductInfo().toString();

        newEntity.setMac(mac);

        // 独立型的CCMTS，需要根据IP+MAC来判断设备唯一性
        if (UNIQUE_IP.equals(this.getEntityUnique())) {
            // IP唯一性策略
            if (this.isEntityReplace()) {
                // 如果网管支持设备替换策略，则找到对应MAC的设备，为后续替换做准备
                Entity entity = entityService.getEntityWithMacAndTypeId(mac, info.getTypeId());
                if (entity != null) {
                    entityService.replaceEntity(entity.getEntityId(), ip);
                    return REPLACEENTITY;
                }
            }
        } else {
            // MAC唯一性策略
            ReentrantLock ipLock = getReentrantLock(ip);
            ReentrantLock macLock = getReentrantLock(mac);
            OperationLog operationLogIp = null;
            OperationLog operationLogMac = null;
            String ipLogStr = null;
            String macLogStr = null;

            if (ipLock.tryLock() && macLock.tryLock()) {
                try {
                    // 查找ip对应的entity
                    Entity ipEntity = entityService.getEntityByIp(ip);
                    // TODO 发现多个发出告警 查找mac对应的entity
                    Entity macEntity = null;
                    List<Entity> macEntityList = entityService.getEntityListWithMacAndTypeId(mac, info.getTypeId());
                    if (macEntityList != null) {
                        if (macEntityList.size() > 1) {
                            // 找到多条记录，在所有CCMTS上记录并报错, 并直接告知设备已存在即可
                            for (Entity entity : macEntityList) {
                                Event event = EventSender.getInstance().createEvent(MAC_DUPLICATE_ALERT_ID,
                                        entity.getIp(), entity.getMac());
                                event.setEntityId(entity.getEntityId());
                                ResourceManager resourceManager = ResourceManager
                                        .getResourceManager("com.topvision.ems.network.resources");
                                event.setMessage(resourceManager.getString("Device.macDuplicated", entity.getName(),
                                        entity.getMac()));
                                EventSender.getInstance().send(event);
                            }
                            return ENTITYEXISTS;
                        } else if (macEntityList.size() == 1) {
                            macEntity = macEntityList.get(0);
                        }
                    }

                    if (ipEntity == null && macEntity == null) {
                        // 发现IP对应的Entity和MAC对应的Entity都不存在，增加新设备
                        return SUCCESS;
                    } else if (ipEntity == null) {
                        // 发现IP对应的Entity不存在，MAC对应的Entity存在，修改MAC对应Entity的IP为当前这个IP
                        entityService.replaceEntity(macEntity.getEntityId(), ip);
                        macLogStr = String.format(getString("cmc.changeIpLog", macEntity.getIp(), macEntity.getMac(),
                                macEntity.getName(), ip));
                        operationLogMac = new OperationLog("System", "127.0.0.1", 1, macEntity.getEntityId(), macLogStr);
                        return REPLACEENTITY;
                    } else if (macEntity == null) {
                        // 发现IP对应的Entity存在，MAC对应的Entity不存在；修改IP对应Entity的IP为其他（SRS时确定为空），然后增加设备
                        entityService.replaceEntity(ipEntity.getEntityId(), NULL_IP);
                        ipLogStr = String.format(getString("cmc.changeIpLog", ipEntity.getIp(), ipEntity.getMac(),
                                ipEntity.getName(), "null"));
                        operationLogIp = new OperationLog("System", "127.0.0.1", 1, ipEntity.getEntityId(), ipLogStr);
                        return SUCCESS;
                    } else {
                        // 发现IP对应的Entity存在，MAC对应的Entity存在
                        if (ipEntity.equals(macEntity)) {
                            // 发现IP对应的Entity和MAC对应的Entity一致，则不变(指定设备已存在)
                            return ENTITYEXISTS;
                        } else {
                            // 正在使用该ip的设备ip置为非法ip，将该ip赋值给mac对应的entity
                            // 如果IP发生更改，在系统日志中增加IP更改历史记录，包含信息：原来IP、MAC、别名，新的IP
                            String ipEntityIp = ipEntity.getIp();
                            String macEntityIp = macEntity.getIp();
                            entityService.replaceEntity(ipEntity.getEntityId(), NULL_IP);
                            entityService.replaceEntity(macEntity.getEntityId(), ipEntityIp);

                            // 增加IP变更日志
                            ipLogStr = String.format(getString("cmc.changeIpLog", ipEntityIp, ipEntity.getMac(),
                                    ipEntity.getName(), "null"));
                            macLogStr = String.format(getString("cmc.changeIpLog", macEntityIp, macEntity.getMac(),
                                    macEntity.getName(), ipEntityIp));
                            operationLogIp = new OperationLog("System", "127.0.0.1", 1, ipEntity.getEntityId(),
                                    ipLogStr);
                            operationLogMac = new OperationLog("System", "127.0.0.1", 1, ipEntity.getEntityId(),
                                    macLogStr);
                            return ENTITYEXISTS;
                        }
                    }
                } finally {
                    ipLock.unlock();
                    macLock.unlock();
                    try {
                        if (operationLogIp != null) {
                            operationService.insertOperationLog(operationLogIp);
                        }
                        if (operationLogMac != null) {
                            operationService.insertOperationLog(operationLogMac);
                        }
                    } catch (Exception e) {
                        logger.info("insert ip change config log error", e);
                    }
                }
            }
        }
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.parser.TopologyHandle#getTopoInfo()
     */
    @Override
    public TopoHandlerProperty getTopoInfo() {
        return new TopoHandlerProperty(CC_WITHAGENT_MAC_OID, TopoHandlerProperty.TOPO_OID);
    }

    public static ReentrantLock getReentrantLock(String key) {
        ReentrantLock lock = entityLockHolder.get(key);
        if (lock != null) {
            return lock;
        }
        entityLockHolder.putIfAbsent(key, new ReentrantLock());
        return entityLockHolder.get(key);
    }

    /**
     * 国际化
     * 
     * @param key
     * @param strings
     * @return
     */
    protected String getString(String key, String... strings) {
        return ResourceManager.getResourceManager("com.topvision.ems.cmc.resources").getString(key, strings);
    }

}
