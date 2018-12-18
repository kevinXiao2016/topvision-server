/***********************************************************************
 * $Id: Cmc8800BDiscoveryServiceImpl.java,v1.0 2012-2-14 上午11:16:19 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.topology.facade.CmcDiscoveryFacade;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.IpAddressTable;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.CmcEntityInfo;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2012-2-14-上午11:16:19
 * 
 */
@Service("cmc8800bDiscoveryService")
public class Cmc8800bDiscoveryServiceImpl extends CmcDiscoveryServiceImpl {
    private static final Long CMC_INDEX_1_1_1 = 142671872L;
    @Value("${index_collect_type:1}")
    private Integer index_collect_type;
    @Value("${entity.unique:ip}")
    private String entityUnique;

    @PostConstruct
    @Override
    public void initialize() {
        messageService.addListener(EntityListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        messageService.removeListener(EntityListener.class, this);
    }

    @Override
    public int checkConnectivity(Entity entity, SnmpParam snmpParam) {
        int delay = onlineService.ping(entity.getIp());
        
        if (delay < 0) {
            return delay;
        }
        
        // 获取实时的mac地址
        String currentMac = getFacadeFactory().getFacade(entity.getIp(), CmcDiscoveryFacade.class).getMacAddress(snmpParam);
        
        return connectivityService.checkConnectivityUsingEntityUnique(entity, delay, currentMac);
    }

    @Override
    public CmcDiscoveryData discovery(SnmpParam snmpParam) {
        // snmpParam.setMibs("RFC1213-MIB,DOCS-IF-MIB,DOCS-QOS-MIB,DOCS-SUBMGT-MIB");
        String ipString = snmpParam.getIpAddress();
        List<Long> cmcIndexs = new ArrayList<>();
        cmcIndexs.add(CMC_INDEX_1_1_1);
        CmcDiscoveryData cmcDiscoveryData = new CmcDiscoveryData(cmcIndexs, CmcDiscoveryData.CMC_TYPE_B);
        cmcDiscoveryData.setIndexCollectType(index_collect_type);
        cmcDiscoveryData = getFacadeFactory().getFacade(ipString, CmcDiscoveryFacade.class).discoveryCmc(snmpParam,
                cmcDiscoveryData);

        cmcDiscoveryData = getFacadeFactory().getFacade(ipString, CmcDiscoveryFacade.class).discoveryCm(snmpParam,
                cmcDiscoveryData);

        return cmcDiscoveryData;
    }

    @Override
    public void updateEntity(Entity entity, CmcDiscoveryData data) {
        Long entityId = entity.getEntityId();
        data.setEntityId(entityId);
        // CmcIndex List
        List<Long> cmcIndexList = data.getCmcIndexs();
        // CmcEntityInfos
        Map<Long, CmcEntityInfo> cmcEntityInfos = new HashMap<Long, CmcEntityInfo>();

        Long onuId = entity.getParentId();
        Long cmcEntityId = null;
        if (onuId != null) {
            // OLT+ONU+CMTS
            cmcEntityId = entityService.getEntity(onuId).getParentId();
        } else {
            // Single CMTS
            cmcEntityId = entityId;
        }

        // CmcIndexList Size == 1
        for (Long cmcIndex : cmcIndexList) {
            CmcEntityInfo cmcEntityInfo = new CmcEntityInfo(entityId, onuId, cmcIndex, null, entity.getTypeId(),
                    cmcEntityId);
            cmcEntityInfos.put(cmcIndex, cmcEntityInfo);
        }
        data.setCmcEntityInfos(cmcEntityInfos);

        super.updateEntity(entity, data);

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
            updateEntityAddress(entity.getEntityId(), addresses);
        }

        syncEntityInfo(entity, data);

        if (data.getTopoType().equals(DiscoveryData.BASE_TOPO)) {
            return;
        }

        // Start Perf Task
        syncPerfMoniotr(entity);

        // SendSynchronizedEvent
        logger.info(
                "begin to discovery ConfigureData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
        sendSynchronizedEvent(entityId, data);
        logger.info("finish discovery ConfigureData ipAddress:" + data.getIp() + "  EntityId:" + entity.getEntityId());
    }

    /**
     * 更新系统内存信息以及设备属性
     * 
     * @param data
     */
    @Override
    public void syncEntityInfo(Entity entity, CmcDiscoveryData data) {
        try {
            CmcAttribute cmcAttribute = data.getCmcAttributes().get(0);
            EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
            event.setEntityId(data.getEntityId());
            event.setState(true);
            event.setCpu(cmcAttribute.getTopCcmtsSysCPURatio().doubleValue() / 100);
            event.setMem(cmcAttribute.getTopCcmtsSysRAMRatio().doubleValue() / 100);
            event.setDisk(cmcAttribute.getTopCcmtsSysFlashRatio().doubleValue() / 100);
            // B型设备使用System属性
            event.setSysUpTime(data.getSystem().getSysUpTime());
            event.setActionName("performanceChanged");
            event.setListener(EntityValueListener.class);
            messageService.addMessage(event);

            if (cmcAttribute.getTopCcmtsSysMacAddr() != null) {
                entityService.clearCache(data.getEntityId());
                cmcDiscoveryDao.updateEntityMac(data.getEntityId(), cmcAttribute.getTopCcmtsSysMacAddr());
            }
            logger.info("sync cmc info with entity done");
        } catch (Exception e) {
            logger.error("Cmc8800B SyncEntityInfo Error: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.impl.DiscoveryServiceImpl#syncPerfMoniotr(com.topvision.ems
     * .facade.domain.Entity)
     */
    @Override
    public void syncPerfMoniotr(Entity entity) {
        // 设置CC的性能采集
        try {
            Long entityId = entity.getEntityId();
            SnmpParam snmpParam = cmcService.getSnmpParamByEntityId(entityId); // 获取entityId
            cmcPerfService.startCmcPerfMonitorForB(entityId, CMC_INDEX_1_1_1, entity.getTypeId(), snmpParam);
            cmcPerfService.startCmCpePerfMonitor(entityId, snmpParam);
        } catch (Exception e) {
            logger.error("Syn startPerfMonitor error ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.impl.DiscoveryServiceImpl#sendSynchronizedEvent(java.lang.
     * Long, com.topvision.ems.facade.domain.DiscoveryData)
     */
    @Override
    public void sendSynchronizedEvent(Long entityId, CmcDiscoveryData data) {
        CmcSynchronizedEvent synchronizedEvent = new CmcSynchronizedEvent(this);
        synchronizedEvent.setActionName("insertEntityStates");
        synchronizedEvent.setEntityId(entityId);
        synchronizedEvent.setListener(CmcSynchronizedListener.class);
        synchronizedEvent.setEntityType(entityTypeService.getCcmtswithagentType());
        List<String> ipAddresses = new ArrayList<>();
        ipAddresses.add(data.getIp());
        synchronizedEvent.setIpAddress(ipAddresses);
        synchronizedEvent.setCmcIndexList(data.getCmcIndexs());
        messageService.addMessage(synchronizedEvent);
    }

    @Override
    public Entity autoRefresh(Entity entity) {
        Long entityId = entity.getEntityId();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        Long cmcId = entityId;
        List<Long> cmcIndexs = new ArrayList<>();
        cmcIndexs.add(CMC_INDEX_1_1_1);
        CmcDiscoveryData data = new CmcDiscoveryData(cmcIndexs, CmcDiscoveryData.CMC_TYPE_B, DiscoveryData.BASE_TOPO);
        data.setIndexCollectType(index_collect_type);
        // CmcDiscoveryData data = new CmcDiscoveryData(cmcIndexs, DiscoveryData.BASE_TOPO);
        data.setEntityId(entityId);
        CmcAttribute cmcattr = new CmcAttribute();
        cmcattr.setCmcId(cmcId);
        cmcattr.setCmcIndex(CMC_INDEX_1_1_1);
        cmcattr.setEntityId(entityId);
        data.addCmcAttribute(cmcattr);
        data.setCmcUpChannelBaseInfos(cmcDao.getCmcUpChannelBaseInfosForDiscovery(cmcId));
        data.setCmcDownChannelBaseInfos(cmcDao.getCmcDownChannelBaseInfosForDiscovery(cmcId));
        data = getFacadeFactory().getFacade(snmpParam.getIpAddress(), CmcDiscoveryFacade.class)
                .autoDiscoveryCC8800B(snmpParam, data);
        Timestamp lastRefreshTime = new Timestamp(System.currentTimeMillis());
        entity.setLastRefreshTime(lastRefreshTime);
        updateEntity(entity, data);
        return entity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityAdded(com.topvision.
     * platform.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityRemoved(com.topvision.
     * platform.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        Entity entity = entityService.getEntity(entityId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (entity != null && entityTypeService.isCcmtsWithAgent(entity.getTypeId())) {
            cmcPerfService.stopCmcPerfMonitorForB(entityId, snmpParam);
        }
    }

}