/***********************************************************************
 * $Id: TopvisionOltDiscoverServiceImpl.java,v1.0 2015年6月18日 上午9:50:36 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
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
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.message.event.CmcEntityEvent;
import com.topvision.platform.message.event.CmcEntityListener;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;

/**
 * OltDiscoveryServiceImpl 支持其他厂商广电标准MIB的OLT拓扑
 * 本类实现本公司的OLT拓扑
 * 
 * @author Victor
 * @created @2015年6月18日-上午9:50:36
 *
 */
@Service("topvisionOltDiscoveryService")
public class TopvisionOltDiscoverServiceImpl extends OltDiscoveryServiceImpl {

    @Override
    public void initialize() {
        messageService.addListener(EntityListener.class, this);
    }

    @Override
    public void destroy() {
        messageService.removeListener(EntityListener.class, this);
    }

    @Override
    public OltDiscoveryData discovery(SnmpParam snmpParam) {
        // Base Topo And No Exclude Oids
        OltDiscoveryData oltDiscoveryData = new OltDiscoveryData();
        return getFacadeFactory().getFacade(snmpParam.getIpAddress(), OltDiscoveryFacade.class).discovery(snmpParam,
                oltDiscoveryData);
    }

    @Override
    public void updateEntity(Entity entity, OltDiscoveryData data) {
        logger.info("begin to save TopologyData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
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
        boolean isOltUpdateFromVersion16ToVersion17 = false;
        if (data.getSlots() != null) {
            String preOltSoftVersion = oltDao.selectOltSoftVersion(data.getEntityId());
            List<OltSlotAttribute> osas = oltSlotDao.getOltSlotList(entity.getEntityId());
            Map<Long, OltSlotAttribute> tmp = make(osas);
            for (OltSlotAttribute oltSlotAttribute : data.getSlots()) {
                OltSlotAttribute oltSlot = tmp.get(oltSlotAttribute.getSlotIndex());
                if (oltSlot != null && oltSlotAttribute.getTopSysBdPreConfigType() != null
                        && oltSlot.getTopSysBdPreConfigType() != null
                        && !oltSlot.getTopSysBdPreConfigType().equals(oltSlotAttribute.getTopSysBdPreConfigType())) {
                    logger.info("OltSlotAttribute info {}", oltSlotAttribute);
                    logger.info("OltSlot info {}", oltSlot);
                    oltSlotService.deleteOltSlot(oltSlot.getEntityId(), oltSlot.getSlotId(),
                            oltSlotAttribute.getTopSysBdPreConfigType());
                    oltMap = (HashMap<Long, Long>) oltDao.getOltMap(entity.getEntityId());
                    //oltMap.remove(oltSlot.getSlotIndex());
                }
                if (oltSlotAttribute.getbAttribute() == 1) {
                    String sufOltSoftVersion = oltSlotAttribute.getBSoftwareVersion();
                    if (preOltSoftVersion != null && preOltSoftVersion.contains("V1.6") && sufOltSoftVersion != null
                            && sufOltSoftVersion.contains("V1.7")) {
                        isOltUpdateFromVersion16ToVersion17 = true;
                    }
                }
            }
            oltSlotService.batchInsertOrUpdateSlotAttribute(data.getSlots(), oltMap);
            if (isOltUpdateFromVersion16ToVersion17) {
                onuDao.synOnuIndexForCmcAtVersionUpdate(data.getEntityId());
                oltMap = (HashMap<Long, Long>) oltDao.getOltMap(entity.getEntityId());
            }
            logger.info("save batchInsertSlotAttribute finished!");
            if (data.getSlotStatus() != null) {
                oltSlotDao.batchInsertOltSlotStatus(data.getSlotStatus());
                for (OltSlotStatus status : data.getSlotStatus()) {
                    if (status.getAttribute() == 1) {
                        EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
                        event.setEntityId(entity.getEntityId());
                        event.setState(true);
                        event.setCpu(status.getTopSysBdCpuUseRatio().doubleValue() / 100);
                        event.setMem((status.getTopSysBdlMemSize() - status.getTopSysBdFreeMemSize())
                                / status.getTopSysBdlMemSize().doubleValue());
                        event.setDisk((status.getTopTotalFlashOctets() - status.getTopSysBdFreeFlashOctets())
                                / status.getTopTotalFlashOctets().doubleValue());
                        event.setSysUpTime(data.getSystem().getSysUpTime());
                        event.setActionName("performanceChanged");
                        event.setListener(EntityValueListener.class);
                        messageService.addMessage(event);
                        break;
                    }
                }
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
            try {
                // @Modify by Rod 首先进行删除多余ONU的操作, 取出原先的ONU列表和现在的采集列表进行比较，多余的则表示需要删除的ONU
                List<OltOnuAttribute> deleteOnuList = onuDao.getOnuList(data.getEntityId(), null, null, null, null,
                        null);
                List<OltOnuAttribute> existOnuAttributes = new ArrayList<>(deleteOnuList);
                if (logger.isTraceEnabled()) {
                    for (OltOnuAttribute oltOnuAttribute : existOnuAttributes) {
                        logger.trace("existOnuAttributes [" + oltOnuAttribute.getOnuMac() + "] ["
                                + oltOnuAttribute.getOnuSerialNum() + "] [" + oltOnuAttribute.getOnuPreType() + "]");
                    }
                }
                // 获得需要删除的ONU列表
                deleteOnuList.removeAll(data.getOnus());
                if (logger.isTraceEnabled()) {
                    for (OltOnuAttribute oltOnuAttribute : data.getOnus()) {
                        logger.trace("data.getOnus [" + oltOnuAttribute.getOnuMac() + "] ["
                                + oltOnuAttribute.getOnuSerialNum() + "] [" + oltOnuAttribute.getOnuPreType() + "]");
                    }
                }
                // 处理ONU删除需要的业务
                List<OltOnuAttribute> del_cmcList = new ArrayList<OltOnuAttribute>();
                List<OltOnuAttribute> del_onuList = new ArrayList<OltOnuAttribute>();
                CmcEntityEvent del_cmcEntityEvent = new CmcEntityEvent(del_cmcList);
                OnuEntityEvent del_onuEntityEvent = new OnuEntityEvent(del_onuList);

                for (OltOnuAttribute deleteOnuAttribute : deleteOnuList) {
                    if ("".equals(deleteOnuAttribute.getOnuPreType())) {
                        if (logger.isTraceEnabled()) {
                            logger.trace("null equals deleteOnuAttribute.getOnuPreType ["
                                    + deleteOnuAttribute.getOnuMac() + "] [" + deleteOnuAttribute.getOnuSerialNum()
                                    + "]");
                        }
                        // 为具有独立IP的ONU进行预留
                    } else if (deleteOnuAttribute.getOnuPreType() == null
                            || OltOnuAttribute.isOnuBelongToCCMTS_A(deleteOnuAttribute.getOnuPreType())
                            || OltOnuAttribute.isOnuBelongToCCMTS_E(deleteOnuAttribute.getOnuPreType())) {
                        del_cmcList.add(deleteOnuAttribute);
                        del_cmcEntityEvent.addDelOnuInfo(oltMap.get(deleteOnuAttribute.getOnuIndex()));
                    } else if (!EponConstants.CC_ONUFLAG_TYPE.contains(deleteOnuAttribute.getOnuPreType())) {
                        //处理ONU业务删除
                        del_onuList.add(deleteOnuAttribute);
                        del_onuEntityEvent.addDelOnuInfo(oltMap.get(deleteOnuAttribute.getOnuIndex()));
                    } else {
                        if (logger.isTraceEnabled()) {
                            logger.trace("else deleteOnuAttribute.getOnuPreType [" + deleteOnuAttribute.getOnuMac()
                                    + "] [" + deleteOnuAttribute.getOnuSerialNum() + "] ["
                                    + deleteOnuAttribute.getOnuPreType() + "]");
                        }
                    }
                }
                if (logger.isTraceEnabled()) {
                    logger.trace("del_cmcList:" + del_cmcList.size());
                    logger.trace("del_onuList:" + del_onuList.size());
                }
                // Cmc Remove 
                try {
                    del_cmcEntityEvent.setActionName("cmcRemoved");
                    del_cmcEntityEvent.setEntityId(entity.getEntityId());
                    del_cmcEntityEvent.setListener(CmcEntityListener.class);
                    messageService.fireMessage(del_cmcEntityEvent);
                    logger.info("topology remove cmc done");
                } catch (Exception e) {
                    logger.error("topology remove cmc error:", e);
                }
                // Onu Remove
                try {
                    del_onuEntityEvent.setActionName("onuRemoved");
                    del_onuEntityEvent.setEntityId(entity.getEntityId());
                    del_onuEntityEvent.setListener(OnuEntityListener.class);
                    messageService.fireMessage(del_onuEntityEvent);
                    logger.info("topology remove onu done");
                } catch (Exception e) {
                    logger.error("topology remove onu error:", e);
                }

                List<OltOnuAttribute> add_cmcList = new ArrayList<OltOnuAttribute>();
                List<OltOnuAttribute> add_onuList = new ArrayList<OltOnuAttribute>();
                for (OltOnuAttribute onuAttribute : data.getOnus()) {
                    if (onuAttribute instanceof GponOnuAttribute) {
                        GponOnuAttribute gponOnuAttribute = (GponOnuAttribute) onuAttribute;
                        String onuEquipmentID = gponOnuAttribute.getOnuEquipmentID();
                        if (onuEquipmentID != null) {
                            // EntityType entityType = entityTypeService.getEntityTypeByDisplayName(onuEquipmentID);
                            if (GponOnuAttribute.GPON_CC_TYPE.containsKey(onuEquipmentID)) {
                                Long typeId = GponOnuAttribute.GPON_CC_TYPE.get(onuEquipmentID);
                                onuAttribute.setEntityType(entityTypeService.getEntityType(typeId));
                                add_cmcList.add(onuAttribute);
                            } else {
                                try {
                                    Long typeId = EponConstants.UNKNOWN_ONU_TYPE.longValue();
                                    if (onuEquipmentID.length() > 0 && onuEquipmentID.contains("PN")) {
                                        typeId = Long.parseLong(
                                                onuEquipmentID.substring(onuEquipmentID.length() - 2,
                                                        onuEquipmentID.length()), 16);
                                        //typeId = gponOnuAttribute.getOnuPreType().longValue();
                                    }
                                    onuAttribute.setOnuPreType(typeId.intValue());
                                    onuAttribute.setEntityType(entityTypeService.getEntityType(typeId));
                                } catch (Exception e) {
                                    onuAttribute.setOnuPreType(OltOnuAttribute.ONU_TYPE_UNKNOWN);
                                    onuAttribute.setEntityType(entityTypeService
                                            .getEntityType(OltOnuAttribute.ONU_TYPE_UNKNOWN));
                                }
                                add_onuList.add(onuAttribute);
                            }
                        }
                        //add_onuList.add(onuAttribute);
                    } else if (onuAttribute instanceof OltOnuAttribute) {
                        if (onuAttribute.getOnuPreType() != null) {
                            if (onuAttribute.getOnuPreType() > OltOnuAttribute.ONU_TYPE_UNKNOWN) {
                                //将设备标识为none的统一为255表示的未知类型
                                if (onuAttribute.getOnuPreType().intValue() == OltOnuAttribute.ONU_TYPE_NONE) {
                                    //onuAttribute.setOnuPreType(OltOnuAttribute.ONU_TYPE_UNKNOWN);
                                    onuAttribute.setEntityType(entityTypeService
                                            .getEntityType(OltOnuAttribute.ONU_TYPE_UNKNOWN));
                                } else {
                                    onuAttribute.setEntityType(entityTypeService
                                            .getEntityType(OltOnuAttribute.ONU_ENTITYTYPE_OTHERCORP));
                                }
                            } else {
                                onuAttribute
                                        .setEntityType(entityTypeService.getEntityType(onuAttribute.getOnuPreType()));
                            }
                            if (OltOnuAttribute.isOnuBelongToCCMTS_A(onuAttribute.getOnuPreType())) {
                                try {
                                    EntityType entityType = onuService.getCmcEntityType(
                                            EponIndex.getCmcIndexByOnuMibIndex(onuAttribute.getOnuMibIndex()),
                                            entity.getEntityId());
                                    if (entityType != null) {
                                        onuAttribute.setEntityType(entityType);
                                    } else {
                                        onuAttribute.setEntityType(entityTypeService
                                                .getEntityType(OltOnuAttribute.ONU_ENTIYTTYPE_CCMTS_A));
                                    }
                                    add_cmcList.add(onuAttribute);
                                } catch (Exception e) {
                                    logger.error("", e);
                                    onuAttribute.setEntityType(entityTypeService
                                            .getEntityType(OltOnuAttribute.ONU_ENTIYTTYPE_CCMTS_A));
                                    add_cmcList.add(onuAttribute);
                                }
                            } else if (OltOnuAttribute.isOnuBelongToCCMTS_E(onuAttribute.getOnuPreType())) {
                                try {
                                    /*EntityType entityType = onuService.getCmcEntityType(
                                        EponIndex.getCmcIndexByOnuMibIndex(onuAttribute.getOnuMibIndex()),
                                        entity.getEntityId(), EntityTypeStandard.STRONG_DISTRIBUTE_STANDARD);*/
                                    EntityType entityType = onuService.getCmcEntityType(onuAttribute.getOnuPreType());

                                    if (entityType != null) {
                                        onuAttribute.setEntityType(entityType);
                                    } else {
                                        onuAttribute.setEntityType(entityTypeService
                                                .getEntityType(OltOnuAttribute.ONU_ENTIYTTYPE_CCMTS_A));
                                    }
                                    add_cmcList.add(onuAttribute);
                                } catch (Exception e) {
                                    logger.error("", e);
                                }
                            } else {
                                add_onuList.add(onuAttribute);
                            }
                        }
                    }
                }
                try {
                    onuDao.syncSubordinateEntityAttribute(existOnuAttributes, data.getOnus(), oltMap, entity);
                } catch (TopvisionDataException e) {
                    logger.error("syncSubordinateEntityAttribute error:", e);
                    return;
                }
                if (logger.isTraceEnabled()) {
                    logger.info("add_onuList:" + add_onuList.size());
                    logger.info("add_cmcList:" + add_cmcList.size());
                }
                //  Topo ONU Info
                if (add_onuList.size() > 0) {
                    OnuEntityEvent onuEntityEvent = new OnuEntityEvent(add_onuList, OnuEntityEvent.TOPVISION_OLT_TOPO);
                    onuEntityEvent.setEntityId(entity.getEntityId());
                    onuEntityEvent.setActionName("onuAdded");
                    onuEntityEvent.setListener(OnuEntityListener.class);
                    for (OltOnuAttribute onuAttribute : add_onuList) {
                        Long onuId = oltMap.get(onuAttribute.getOnuIndex());
                        onuAttribute.setOnuId(onuId);
                        onuEntityEvent.addOnuEntity(onuAttribute);
                    }
                    messageService.fireMessage(onuEntityEvent);
                } else if (add_cmcList.size() > 0) {
                    // Topo CC-ONU Info when ONU size = 0
                    OnuEntityEvent onuEntityEvent = new OnuEntityEvent(add_onuList, OnuEntityEvent.STANDARD_OLT_TOPO);
                    onuEntityEvent.setEntityId(entity.getEntityId());
                    onuEntityEvent.setActionName("onuAdded");
                    onuEntityEvent.setListener(OnuEntityListener.class);
                    messageService.fireMessage(onuEntityEvent);
                }

                //  Topo CC Info
                if (add_cmcList.size() > 0) {
                    CmcEntityEvent cmcEntityEvent = new CmcEntityEvent(add_cmcList);
                    cmcEntityEvent.setEntityId(entity.getEntityId());
                    cmcEntityEvent.setActionName("cmcAdded");
                    cmcEntityEvent.setListener(CmcEntityListener.class);
                    for (OltOnuAttribute cmcAttribute : add_cmcList) {
                        Long onuIndex = cmcAttribute.getOnuIndex();
                        Long cmcIndex = CmcIndexUtils.getCmcIndexFromOnuIndex(onuIndex);
                        Long cmcId = oltMap.get(cmcAttribute.getOnuIndex());
                        cmcEntityEvent.addCmcEntity(cmcIndex, cmcId, onuIndex, cmcAttribute.getEntityType());
                    }
                    messageService.fireMessage(cmcEntityEvent);

                    // Topo CM Info
                    CmcEntityEvent cmEntityEvent = new CmcEntityEvent(add_cmcList);
                    cmEntityEvent.setEntityId(entity.getEntityId());
                    cmEntityEvent.setActionName("cmTopo");
                    cmEntityEvent.setListener(CmcEntityListener.class);
                    messageService.addMessage(cmEntityEvent);
                }
            } catch (Exception e) {
                logger.error("", e);
                throw e;
            }
        }

        logger.info("save onus finished!");

        logger.info("finish save TopologyData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());

        if (data.getTopoType().equals(DiscoveryData.BASE_TOPO)) {
            return;
        }

        // Start PerfTask
        syncPerfMoniotr(entity);

        // SendSynchronizedEvent
        logger.info("begin to discovery ConfigureData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
        sendSynchronizedEvent(entity.getEntityId(), data);
        logger.info("finish discovery ConfigureData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
    }

    private Map<Long, OltSlotAttribute> make(List<OltSlotAttribute> osas) {
        Map<Long, OltSlotAttribute> re = new HashMap<Long, OltSlotAttribute>();
        for (OltSlotAttribute oltSlotAttribute : osas) {
            re.put(oltSlotAttribute.getSlotIndex(), oltSlotAttribute);
        }
        return re;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Entity autoRefresh(Entity entity) {
        Long entityId = entity.getEntityId();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        if (checkOnuInfoChange(entityId, snmpParam)) {
            try {
                super.refresh(entityId);
            } catch (Exception e) {
                logger.error("", e);
            }
            return entity;
        }
        OltDiscoveryData data = new OltDiscoveryData(OltDiscoveryData.BASE_TOPO);
        data = getFacadeFactory().getFacade(snmpParam.getIpAddress(), OltDiscoveryFacade.class).autoDiscovery(
                snmpParam, data);
        Timestamp lastRefreshTime = new Timestamp(System.currentTimeMillis());
        entity.setLastRefreshTime(lastRefreshTime);
        updateEntity(entity, data);

        List<Entity> onuEntity = entityService.getSubEntityByEntityId(entityId);
        for (Entity onu : onuEntity) {
            try {
                DiscoveryService ds = null;
                EntityType subEntityType = entityTypeService.getEntityType(onu.getTypeId());
                String discoveryService = String.format("%sDiscoveryService", subEntityType.getName());
                if (!beanFactory.containsBean(discoveryService)) {
                    discoveryService = subEntityType.getDiscoveryBean();
                    if (discoveryService == null) {
                        continue;
                    }
                }
                ds = (DiscoveryService) beanFactory.getBean(discoveryService);
                if (ds == null) {
                    continue;
                }
                ds.autoRefresh(onu);
            } catch (BeansException e) {
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return entity;
    }

    /**
     * check onu info change state
     * 
     * @param entityId
     * @param snmpParam
     * @return
     */
    private boolean checkOnuInfoChange(Long entityId, SnmpParam snmpParam) {
        Map<String, String> onuMacAddressOld = onuService.getAllOnuMacAndIndex(entityId);
        Map<String, String> onuMacAddressNew = new HashMap<String, String>();
        OltDiscoveryFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), OltDiscoveryFacade.class);
        try {
            onuMacAddressNew = facade.getOnuMacAddress(snmpParam);
        } catch (Exception e) {
            return false;
        }
        // compare onuMacAddress
        if (onuMacAddressNew != null && onuMacAddressNew.size() != onuMacAddressOld.size()) {
            return true;
        }

        for (Map.Entry<String, String> entry : onuMacAddressNew.entrySet()) {
            String oldValue = onuMacAddressOld.get(entry.getKey());
            if (oldValue == null) {
                return true;
            }
            if (!oldValue.equalsIgnoreCase(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#syncPerfMoniotr(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void syncPerfMoniotr(Entity entity) {
        // Start Olt Perf Collect
        try {
            eponStatsService.startOltPerfCollect(entity);
        } catch (Exception e) {
            logger.error("Syn startOltPerfCollect error ", e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#sendSynchronizedEvent(java.lang.Long, com.topvision.ems.facade.domain.DiscoveryData)
     */
    @Override
    public void sendSynchronizedEvent(Long entityId, OltDiscoveryData data) {
        Entity entity = entityService.getEntity(entityId);
        SynchronizedEvent synchronizedEvent = new SynchronizedEvent(this);
        synchronizedEvent.setAction(SynchronizedEvent.ADD_SYNCHRONIZED);
        synchronizedEvent.setActionName("insertEntityStates");
        synchronizedEvent.setEntityId(entityId);
        synchronizedEvent.setEventType("OltManagement");
        synchronizedEvent.setIpAddress(entity.getIp());
        synchronizedEvent.setData(data);
        synchronizedEvent.setListener(SynchronizedListener.class);
        messageService.addMessage(synchronizedEvent);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityRemoved(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        Entity entity = entityService.getEntity(entityId);
        // Delete Olt
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            eponStatsService.stopOltPerfCollect(entity);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityAdded(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

}
