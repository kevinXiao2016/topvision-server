/***********************************************************************
 * $Id: Cmc8800ADiscoveryServiceImpl.java,v1.0 2014-10-18 下午2:16:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmc.topology.domain.CmcDiscoveryData;
import com.topvision.ems.cmc.topology.facade.CmcDiscoveryFacade;
import com.topvision.ems.cmc.topology.service.CmcRefreshService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.message.event.CmcEntityEvent;
import com.topvision.platform.message.event.CmcEntityInfo;
import com.topvision.platform.message.event.CmcEntityListener;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2014-10-18-下午2:16:07
 * 
 */

@Service("cmc8800aDiscoveryService")
public class Cmc8800aDiscoveryServiceImpl extends CmcDiscoveryServiceImpl implements CmcEntityListener {
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private CmcRefreshService cmcRefreshService;
    @Resource(name = "cmcDiscoveryDao")
    private CmcDiscoveryDao cmcDiscoveryDao;

    @PostConstruct
    public void initialize() {
        messageService.addListener(CmcEntityListener.class, this);
        messageService.addListener(EntityListener.class, this);
    }

    @PreDestroy
    public void destroy() {
        messageService.removeListener(CmcEntityListener.class, this);
        messageService.removeListener(EntityListener.class, this);
    }

    @Override
    protected void updatePort(DiscoveryData data, Long entityId) {
        // do nothing
    }

    @Override
    public void cmcAdded(CmcEntityEvent event) {
        logger.info("CmcDiscoveryServiceImpl.cmcAdded({})", event);
        // OLT entityId
        Long entityId = event.getEntityId();
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        // CmcIndex List   
        List<Long> cmcIndexList = event.getCmcIndex();

        SnmpParam param = cmcService.getSnmpParamByEntityId(entityId); // 获取entityId
        CmcDiscoveryData data = new CmcDiscoveryData(event.getCmcEntityInfos(), cmcIndexList);

        data = getFacadeFactory().getFacade(param.getIpAddress(), CmcDiscoveryFacade.class).discoveryCmc(param, data);

        super.updateEntity(entity, data);

        syncEntityInfo(entity, data);

        // Start Perf Task
        syncPerfMoniotr(event, entityId, param);

        // Send ConfigData Topo Event
        sendSynchronizedEvent(entityId, data);

    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.CmcEntityListener#cmTopo(com.topvision.platform.message.event.CmcEntityEvent)
     */
    @Override
    public void cmTopo(CmcEntityEvent event) {
        logger.info("CmcDiscoveryServiceImpl.cmTopo({})", event);
        // OLT entityId
        Long entityId = event.getEntityId();
        Entity entity = entityDao.selectByPrimaryKey(entityId);

        SnmpParam param = cmcService.getSnmpParamByEntityId(entityId); // 获取entityId
        CmcDiscoveryData data = new CmcDiscoveryData();

        data = getFacadeFactory().getFacade(param.getIpAddress(), CmcDiscoveryFacade.class).discoveryCm(param, data);

        super.updateEntity(entity, data);
    }

    @Override
    public void syncEntityInfo(Entity entity, CmcDiscoveryData data) {
        for (CmcAttribute cmcAttribute : data.getCmcAttributes()) {
            if (cmcAttribute != null) {
                try {
                    // Modify EntitySnap 
                    EntityValueEvent snapEvent = new EntityValueEvent(cmcAttribute.getCmcId());
                    snapEvent.setEntityId(cmcAttribute.getCmcId());
                    if (CmcConstants.TOPCCMTSSYSSTATUS_ONLINE.equals(cmcAttribute.getTopCcmtsSysStatus())) {
                        snapEvent.setState(true);
                        snapEvent.setCpu(cmcAttribute.getTopCcmtsSysCPURatio().doubleValue() / 100);
                        snapEvent.setMem(cmcAttribute.getTopCcmtsSysRAMRatio().doubleValue() / 100);
                        snapEvent.setDisk(cmcAttribute.getTopCcmtsSysFlashRatio().doubleValue() / 100);
                        snapEvent.setSysUpTime(cmcAttribute.getTopCcmtsSysUpTime().toString());
                    } else {
                        snapEvent.setCpu(-1d);
                        snapEvent.setMem(-1d);
                        snapEvent.setDisk(-1d);
                        snapEvent.setSysUpTime("-1");
                        snapEvent.setState(false);
                    }
                    snapEvent.setActionName("performanceChanged");
                    snapEvent.setListener(EntityValueListener.class);
                    messageService.addMessage(snapEvent);
                } catch (Exception e) {
                    logger.error("Send EntitySnap Event Error:", e);
                }
                try {
                    entityDao.updateEntityLastRefreshTime(cmcAttribute.getCmcId(),
                            new Timestamp(data.getDiscoveryTime()));
                    // 插入cc软件版本信息到onu信息表中
                    cmcDiscoveryDao.updateCmcAttributeToOnuAttribute(cmcAttribute);
                    // Modify By Rod 更新ONU以及Entity的Name
                    cmcService.modifyOnuName(cmcAttribute.getCmcId(), cmcAttribute.getTopCcmtsSysName());
                    // modify by haojie 更新cmc mac
                    cmcService.modifyCmcMac(cmcAttribute.getCmcId(), cmcAttribute.getTopCcmtsSysMacAddr());

                } catch (Exception e) {
                    logger.error("Cmc8800A SyncEntityInfo Error: ", e);
                }
            }
        }
    }

    @Override
    public void sendSynchronizedEvent(Long entityId, CmcDiscoveryData data) {
        CmcSynchronizedEvent synchronizedEvent = new CmcSynchronizedEvent(this);
        synchronizedEvent.setActionName("insertEntityStates");
        synchronizedEvent.setEntityId(entityId);
        synchronizedEvent.setListener(CmcSynchronizedListener.class);
        synchronizedEvent.setEntityType(entityTypeService.getCcmtswithoutagentType());
        synchronizedEvent.setCmcIndexList(data.getCmcIndexs());
        messageService.addMessage(synchronizedEvent);
    }

    @Override
    public void cmcDiscovered(CmcEntityEvent event) {
    }

    @Override
    public void cmcChanged(CmcEntityEvent event) {
    }

    @Override
    public void cmcRemoved(CmcEntityEvent event) {
        logger.info("CmcDiscoveryServiceImpl.cmcRemoved({})", event);
        SnmpParam param = cmcService.getSnmpParamByEntityId(event.getEntityId()); // 获取entityId
        for (Long cmcId : event.getDel_onuId()) {
            if (logger.isTraceEnabled()) {
                logger.trace("cmcRemoved.cmcRemoved({})", cmcId);
            }
            cmcPerfService.stopCmcPerfMonitorForA(cmcId, param);
        }
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
            SnmpParam param = entityService.getSnmpParamByEntity(entityId);
            List<Long> cmcIds = cmcService.getCmcIdListByEntityId(entityId);
            cmcPerfService.stopCmCpePerfMonitor(entityId, param);
            for (Long cmcId : cmcIds) {
                cmcPerfService.stopCmcPerfMonitorForA(cmcId, param);
            }
        }
        // Delete Cmc
        if (entity != null && entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
            SnmpParam param = entityService.getSnmpParamByEntity(entity.getParentId());
            cmcPerfService.stopCmcPerfMonitorForA(entityId, param);
        }

    }

    private void syncPerfMoniotr(CmcEntityEvent event, Long entityId, SnmpParam param) {
        // 设置CC的性能采集
        for (Entry<Long, CmcEntityInfo> entry : event.getCmcEntityInfos().entrySet()) {
            try {
                Long cmcIndex = entry.getKey();
                CmcEntityInfo cmcEntityInfo = entry.getValue();
                Long cmcId = cmcEntityInfo.getCmcId();
                Long onuIndex = cmcEntityInfo.getOnuIndex();
                Long typeId = cmcEntityInfo.getTypeId();
                cmcPerfService.startCmcPerfMonitorForA(cmcId, cmcIndex, onuIndex, param, entityId, typeId);
            } catch (Exception e) {
                logger.error("startCmcPerfMonitorForA error ", e);
            }
        }
        logger.info("startCmcPerfMonitorForA finished!");

        try {
            cmcPerfService.startCmCpePerfMonitor(entityId, param);
        } catch (Exception e) {
            logger.error("startCmCpePerfMonitor error ", e);
        }
        logger.info("startCmCpePerfMonitor finished!");

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityAdded(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    @Override
    public void managerChanged(CmcEntityEvent event) {
    }

    @Override
    public Entity autoRefresh(Entity entity) {
        Long cmcId = entity.getEntityId();
        Long entityId = cmcService.getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        cmcRefreshService.autoRefreshCC8800A(entityId, cmcIndex, cmcId);
        return entity;
    }

}
