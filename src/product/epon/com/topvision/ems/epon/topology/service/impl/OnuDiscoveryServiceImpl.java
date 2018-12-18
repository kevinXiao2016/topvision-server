/***********************************************************************
 * $Id: OnuDiscoveryServiceImpl.java,v1.0 2015-4-22 上午10:37:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onuauth.service.OnuAuthService;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.epon.topology.event.OnuEntityEvent;
import com.topvision.ems.epon.topology.event.OnuEntityListener;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.epon.topology.facade.OnuDiscoveryFacade;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.gpon.onu.dao.GponOnuDao;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.impl.DiscoveryServiceImpl;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2015-4-22-上午10:37:48
 *
 */
@Service("onuDiscoveryService")
public class OnuDiscoveryServiceImpl extends DiscoveryServiceImpl<OnuDiscoveryData> implements OnuEntityListener {
    @Autowired
    protected MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private OnuPerfService onuPerfService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuAuthService onuAuthService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;

    @Autowired
    private OnuDao onuDao;
    @Autowired
    private UniDao uniDao;
    @Autowired
    private GponOnuDao gponOnuDao;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuEntityListener.class, this);
        messageService.addListener(EntityListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        messageService.removeListener(OnuEntityListener.class, this);
        messageService.removeListener(EntityListener.class, this);
        super.destroy();
    }

    @Override
    public void onuAdded(OnuEntityEvent event) {
        Long entityId = event.getEntityId();

        SnmpParam param = entityService.getSnmpParamByEntity(entityId);

        OnuDiscoveryData data = new OnuDiscoveryData(entityId);

        data.setOltOnuAttributes(event.getOnuAttributes());

        OnuDiscoveryFacade facade = getFacadeFactory().getFacade(param.getIpAddress(), OnuDiscoveryFacade.class);

        data = facade.discovery(param, data);

        data.setOnuIndexs(event.getOnuIndexList());

        // Update Onu Info With Ccmts
        updateOnuEntity(entityId, data);

        // Sync Onu
        if (event.getTopoType().equals(OnuEntityEvent.TOPVISION_OLT_TOPO)) {
            // Sync Onu Entity Info
            syncEntityInfo(null, data);

            for (OltOnuAttribute oltOnuAttribute : event.getOnuAttributes()) {
                try {
                    Long onuId = oltOnuAttribute.getOnuId();
                    Long onuIndex = oltOnuAttribute.getOnuIndex();
                    // Start Onu Perf Task
                    onuPerfService.startOnuPerfCollect(entityId, onuId, onuIndex);
                    logger.info("End to start perfCollect: {}", onuIndex);
                } catch (Exception e) {
                    logger.error("Start onu perfCollect Failed: {}", e);
                }
            }

            sendSynchronizedEvent(entityId, data);
        }
    }

    @Override
    public void onuRemoved(OnuEntityEvent event) {
        logger.info("OnuDiscoveryServiceImpl.onuRemoved({}) start");
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(event.getEntityId());
        for (Long onuId : event.getDel_onuId()) {
            try {
                // 处理ONU性能业务的关闭
                onuPerfService.stopOnuPerfCollect(onuId, snmpParam);
            } catch (Exception e) {
                logger.error("onuId:" + onuId + " remove error:", e);
            }
        }
        logger.info("OnuDiscoveryServiceImpl.onuRemoved({}) done");
    }

    @Override
    public void entityRemoved(EntityEvent event) {
        try {
            // 处理删除OLT设备时删除下面的ONU设备
            Entity entity = event.getEntity();
            // 规范entityRemove用法， 在删除OLT时处理ONU的相关逻辑
            if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
                Long entityId = entity.getEntityId();
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
                List<Long> onuIds = null;
                try {
                    onuIds = onuDao.getOnuIdList(entityId);
                } catch (Exception e) {
                    logger.error("", e);
                }
                if (onuIds != null && !onuIds.isEmpty()) {
                    for (Long onuId : onuIds) {
                        // 关闭ONU性能采集
                        onuPerfService.stopOnuPerfCollect(onuId, snmpParam);
                    }
                }
            } else if (entity != null && entityTypeService.isOnu(entity.getTypeId())) {
                // 处理删除只删除ONU的情况
                Long onuEntityId = entity.getEntityId();
                Long oltEntityId = entity.getParentId();
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltEntityId);
                if (onuEntityId != null) {
                    // 关闭ONU性能采集
                    onuPerfService.stopOnuPerfCollect(onuEntityId, snmpParam);
                }
            }
        } catch (Exception e) {
            logger.error("ONU Bussiness removed failed : {}", e);
        }
    }

    @Override
    public void onuDiscovered(OnuEntityEvent event) {

    }

    @Override
    public void onuChanged(OnuEntityEvent event) {

    }

    @Override
    public void managerChanged(OnuEntityEvent event) {

    }

    @Override
    public void entityAdded(EntityEvent event) {

    }

    @Override
    public void entityDiscovered(EntityEvent event) {

    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {

    }

    @Override
    public void entityChanged(EntityEvent event) {

    }

    @Override
    public void managerChanged(EntityEvent event) {

    }

    private void updateOnuEntity(Long entityId, OnuDiscoveryData data) {

        Map<String, Integer> onuLevelCache = onuAuthService.getOnuLevelCache();

        List<OltOnuAttribute> oltOnuAttributes = data.getOltOnuAttributes();
        for (OltOnuAttribute onu : oltOnuAttributes) {
            Long onuIndex = onu.getOnuIndex();
            Integer onuLevel = onuLevelCache.get(entityId + "_" + onuIndex);
            if (onuLevel != null) {
                onuAssemblyService.saveOnuServerLevel(onu.getOnuId(), onuLevel);
            }
            onuLevelCache.remove(entityId + "_" + onuIndex);
        }

        List<OltOnuPonAttribute> oltOnuPonAttributes = data.getOltOnuPonAttributes();

        List<OltUniAttribute> oltUniAttributes = data.getOltUniAttributes();

        List<OltUniExtAttribute> oltUniExtAttributes = data.getOltUniExtAttributes();

        List<OltOnuCapability> onuCapabilities = data.getOltOnuCapabilities();

        List<OltTopOnuCapability> topOnuCapabilities = data.getOltTopOnuCapabilities();

        List<GponOnuCapability> gponOnuCapabilities = data.getGponOnuCapabilities();

        List<TopGponOnuCapability> topGponOnuCapabilities = data.getTopGponOnuCapabilities();

        // 将TopGponOnuCapability中的POTS能力信息设置到GponOnuCapability实体中
        if (gponOnuCapabilities != null && topGponOnuCapabilities != null) {
            for (GponOnuCapability gponOnuCapability : gponOnuCapabilities) {
                Long onuIndex = gponOnuCapability.getOnuIndex();
                for (TopGponOnuCapability topGponOnuCapability : topGponOnuCapabilities) {
                    if (topGponOnuCapability.getOnuIndex().equals(onuIndex)) {
                        gponOnuCapability.setOnuTotalPotsNum(topGponOnuCapability.getTopGponOnuCapOnuPotsNum());
                    }
                }
            }
        }

        HashMap<Long, Long> onuMap;
        onuMap = (HashMap<Long, Long>) onuDao.getOnuMap(entityId);

        try {
            onuDao.syncOnuPonAttribute(oltOnuPonAttributes, onuMap);
        } catch (Exception e) {
            logger.error("Sync OnuPon Attribute error:", e);
        }

        try {
            uniDao.syncUniAttribute(oltUniAttributes, entityId, onuMap, true);
        } catch (Exception e) {
            logger.error("Sync Uni Attribute error:", e);
        }

        try {
            uniDao.syncUniExtAttribute(oltUniExtAttributes, onuMap);
        } catch (Exception e) {
            logger.error("Sync Uni ExtAttribute error:", e);
        }

        try {
            onuDao.syncOnuCapatility(onuCapabilities, topOnuCapabilities, onuMap);
        } catch (Exception e) {
            logger.error("update Onu Capability error:", e);
        }

        try {
            gponOnuDao.syncGponOnuCapability(gponOnuCapabilities, onuMap);
        } catch (Exception e) {
            logger.error("update gpon Capability error:", e);
        }
    }

    @Override
    public void sendSynchronizedEvent(Long entityId, OnuDiscoveryData data) {
        OnuSynchronizedEvent synchronizedEvent = new OnuSynchronizedEvent(this);
        synchronizedEvent.setActionName("insertEntityStates");
        synchronizedEvent.setEntityId(entityId);
        synchronizedEvent.setListener(OnuSynchronizedListener.class);
        synchronizedEvent.setOnuIndexList(data.getOnuIndexs());
        messageService.addMessage(synchronizedEvent);
    }

    /**
     * 更新系统内存信息以及设备属性
     * 
     * @param data
     */
    @Override
    public void syncEntityInfo(Entity entity, OnuDiscoveryData data) {
        for (OltOnuAttribute onuAttribute : data.getOltOnuAttributes()) {
            // StateChange
            EntityValueEvent stateEvent = new EntityValueEvent(onuAttribute);
            stateEvent.setEntityId(onuAttribute.getOnuId());
            stateEvent.setState(onuAttribute.getOnuOperationStatus() == 1);
            stateEvent.setActionName("stateChanged");
            stateEvent.setListener(EntityValueListener.class);
            messageService.addMessage(stateEvent);
        }
    }

}
