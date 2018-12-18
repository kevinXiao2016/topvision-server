/***********************************************************************
 * $Id: StandardOltDiscoveryServiceImpl.java,v1.0 2015-7-31 上午10:12:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.epon.topology.event.OnuEntityEvent;
import com.topvision.ems.epon.topology.event.OnuEntityListener;
import com.topvision.ems.epon.topology.facade.OltDiscoveryFacade;
import com.topvision.ems.exception.TopvisionDataException;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.domain.TopologyRefreshLock;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2015-7-31-上午10:12:47
 *
 */
@Service("standardOltDiscoveryService")
public class StandardOltDiscoveryServiceImpl extends OltDiscoveryServiceImpl {

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public OltDiscoveryData discovery(SnmpParam snmpParam) {
        List<String> excludeOids = new ArrayList<>();
        excludeOids.add(DiscoveryData.TOPVISION_OID);
        OltDiscoveryData oltDiscoveryData = new OltDiscoveryData(excludeOids);
        return getFacadeFactory().getFacade(snmpParam.getIpAddress(), OltDiscoveryFacade.class).discovery(snmpParam,
                oltDiscoveryData);
    }

    @Override
    public void updateEntity(Entity entity, OltDiscoveryData data) {
        ReentrantLock lock = TopologyRefreshLock.getReentrantLock(entity.getEntityId());
        if (lock.tryLock()) {
            try {
                logger.info(
                        "begin to save TopologyData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
                if (entityTypeService.isOlt(entity.getTypeId())) {
                    EntityType type = entityTypeService.getEntityTypeBySysObjId(data.getSystem().getSysObjectID());
                    if (type != null) {
                        entity.setTypeId(type.getTypeId());
                    }
                }

                super.updateEntity(entity, data);
                // 产品内部实现IP地址表的更新，后续可以在DiscoveryServiceImpl统一实现
                if (data.getIpAddressTables() != null) {
                    List<EntityAddress> addresses = new ArrayList<EntityAddress>();
                    for (IpAddressTable tmp : data.getIpAddressTables()) {
                        EntityAddress address = new EntityAddress();
                        address.setEntityId(entity.getEntityId());
                        String ipAddress = tmp.getIpAdEntAddr();
                        if (!ipAddress.equals("127.0.0.1")) {
                            address.setIp(ipAddress);
                            addresses.add(address);
                        }
                    }
                    super.updateEntityAddress(entity.getEntityId(), addresses);
                }
                logger.info("save EntityAddress finished!");
                // TODO 确认并发时的数据正确性
                HashMap<Long, Long> oltMap;
                oltMap = (HashMap<Long, Long>) oltDao.getOltMap(entity.getEntityId());
                if (data.getAttribute() != null) {
                    oltDao.insertOrUpdateOltAttribute(data.getAttribute());
                }
                logger.info("save insertOrUpdateOltAttribute finished!");
                if (data.getSlots() != null) {
                    oltSlotService.batchInsertOrUpdateSlotAttribute(data.getSlots(), oltMap);
                    logger.info("save batchInsertSlotAttribute finished!");
                    if (data.getSlotStatus() != null) {
                        oltSlotDao.batchInsertOltSlotStatus(data.getSlotStatus());
                    }
                    logger.info("save SlotStatus finished!");
                }
                logger.info("save Slots finished!");
                if (data.getPowers() != null) {
                    try {
                        oltSlotDao.batchInsertOltPowerAttribute(entity.getEntityId(), data.getPowers());
                        if (data.getPowerStatus() != null) {
                            oltSlotDao.batchInsertOltPowerStatus(data.getPowerStatus());
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                logger.info("save Powers finished!");

                if (data.getFans() != null) {
                    try {
                        oltSlotDao.batchInsertOltFanAttribute(entity.getEntityId(), data.getFans());
                        if (data.getFanStatus() != null) {
                            oltSlotDao.batchInsertOltFanStatus(data.getFanStatus());
                        }
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                logger.info("save Fans finished!");
                if (data.getSnis() != null) {
                    try {
                        oltSniDao.batchInsertSniAttribute(entity.getEntityId(), data.getSnis(), oltMap);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                logger.info("save snis finished!");
                if (data.getPons() != null) {
                    try {
                        oltPonDao.batchInsertPonAttribute(data.getPons(), oltMap);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                logger.info("save pons finished!");

                if (data.getOnus() != null) {
                    //TODO TEST
                    for (OltOnuAttribute onuAttribute : data.getOnus()) {
                        if (onuAttribute.getOnuMibIndex().longValue() == 16909057) {
                            onuAttribute.setOnuIpAddress("172.17.2.148");
                        }
                    }
                    List<OltOnuAttribute> deleteOnuList = onuDao.getOnuList(data.getEntityId(), null, null, null, null,
                            null);
                    List<OltOnuAttribute> existOnuAttributes = new ArrayList<>(deleteOnuList);
                    onuDao.syncSubordinateEntityAttribute(existOnuAttributes, data.getOnus(), oltMap, entity);
                    try {
                        for (OltOnuAttribute onuAttribute : data.getOnus()) {
                            onuDao.deleteEntityRelationAndLink(onuAttribute.getOnuId());
                            if (!onuAttribute.getOnuIpAddress().equals("0.0.0.0")) {
                                try {
                                    Entity onuEntity = entityDao.selectByPrimaryKey(onuAttribute.getOnuId());
                                    Entity subEntity = entityDao.getEntityByIp(onuAttribute.getOnuIpAddress());
                                    if (subEntity != null) {
                                        //Create Relation And Link
                                        onuDao.createEntityRelationAndLink(onuEntity, subEntity);
                                        super.refresh(subEntity.getEntityId());
                                    } else {
                                        long delay = onlineService.ping(onuAttribute.getOnuIpAddress());
                                        if (delay < 0) {
                                            continue;
                                        }
                                        Long onuId = onuAttribute.getOnuId();
                                        Entity cmtsEntity = new Entity();
                                        cmtsEntity.setIp(onuAttribute.getOnuIpAddress());
                                        SnmpParam snmpParam = new SnmpParam();
                                        cmtsEntity.setParam(snmpParam);
                                        EntityType entityType = entityService.topoEntityTypeId(cmtsEntity, snmpParam);
                                        if (entityType.getTypeId() == EntityType.UNKNOWN_TYPE) {
                                            continue;
                                        }
                                        String entityName = entityService.topoEntitySysName(cmtsEntity, snmpParam);
                                        cmtsEntity.setName(entityName == null ? "" : entityName);
                                        cmtsEntity.setIp(onuAttribute.getOnuIpAddress());
                                        cmtsEntity.setParentId(onuId);
                                        cmtsEntity.setTypeId(entityType.getTypeId());
                                        newCmtsEntity(cmtsEntity);
                                        onuDao.createEntityRelationAndLink(onuEntity, cmtsEntity);
                                    }
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                        }
                    } catch (TopvisionDataException e) {
                        logger.error("syncSubordinateEntityAttribute error:", e);
                        return;
                    }

                    //  Topo ONU Info
                    OnuEntityEvent onuEntityEvent = new OnuEntityEvent(data.getOnus(),
                            OnuEntityEvent.STANDARD_OLT_TOPO);
                    onuEntityEvent.setEntityId(entity.getEntityId());
                    onuEntityEvent.setActionName("onuAdded");
                    onuEntityEvent.setListener(OnuEntityListener.class);
                    for (OltOnuAttribute onuAttribute : data.getOnus()) {
                        Long onuId = oltMap.get(onuAttribute.getOnuIndex());
                        onuAttribute.setOnuId(onuId);
                        onuEntityEvent.addOnuEntity(onuAttribute);
                    }
                    messageService.fireMessage(onuEntityEvent);

                    /*if (data.getOnuPons() != null) {
                        try {
                            onuDao.syncOnuPonAttribute(data.getOnuPons(), oltMap);
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                    if (data.getUnis() != null) {
                        try {
                            uniDao.syncUniAttribute(data.getUnis(), oltMap);
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }*/

                }
                logger.info("save onus finished!");

                //Sync EntitySnap Info
                syncEntityInfo(entity, data);
                // Start PerfTask
                syncPerfMoniotr(entity);

                logger.info(
                        "finish save TopologyData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
            } catch (Exception e) {
                logger.error("StandardOltDiscoveryServiceImpl updateEntity error:", e);
                throw e;
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void syncEntityInfo(Entity entity, OltDiscoveryData data) {
        //发送信息更新entitysnap数据，并且将设备snap信息放入snapMapping
        //目前只处理状态 state, 在线时长sysuptime, mem, cpu, flash未做处理
        EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
        event.setEntityId(entity.getEntityId());
        event.setState(true);
        event.setSysUpTime(data.getSystem().getSysUpTime());
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
    }

    private void newCmtsEntity(Entity entity) {
        try {
            EntityType type = entityTypeService.getEntityType(entity.getTypeId());
            if (type != null) {
                entity.setCorpId(entityTypeService.getCorpBySysObjId(type.getSysObjectID()));
                entity.setTypeId(type.getTypeId());
                entity.setIcon48(type.getIcon48());
            }
            // 设置其默认为可管理的。
            entity.setStatus(Boolean.TRUE);
            // Add by Rod On Authority
            entityService.txCreateEntity(entity);
            // 设备添加后发送消息
            entityService.txCreateMessage(entity);
        } catch (ExistEntityException eee) {
            //logger.debug("New Entity.", eee);
        }
    }

    @Override
    public void syncPerfMoniotr(Entity entity) {
        // Start Olt Perf Collect
        try {
            eponStatsService.startOltPerfCollect(entity);
        } catch (Exception e) {
            logger.error("Syn startOltPerfCollect error ", e);
        }
    }
}
