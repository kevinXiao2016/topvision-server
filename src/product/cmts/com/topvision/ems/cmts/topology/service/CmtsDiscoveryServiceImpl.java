/***********************************************************************
 * $Id: CmtsDiscoveryServiceImpl.java,v1.0 2013-7-20 下午02:20:03 $
 *
 * @author: Rod John
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.topology.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.cmts.service.CmtsPerfService;
import com.topvision.ems.cmts.topology.domain.CmtsDiscoveryData;
import com.topvision.ems.cmts.topology.event.CmtsEntityEvent;
import com.topvision.ems.cmts.topology.event.CmtsEntityInfo;
import com.topvision.ems.cmts.topology.event.CmtsEntityListener;
import com.topvision.ems.cmts.topology.facade.CmtsDiscoveryFacade;
import com.topvision.ems.facade.domain.CpuAndMemData;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.ems.facade.domain.DiscoveryData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.impl.DiscoveryServiceImpl;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.template.service.CpuAndMemService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2013-7-20-下午02:20:03
 */
@Service("cmtsDiscoveryService")
public class CmtsDiscoveryServiceImpl<T extends CmtsDiscoveryData> extends DiscoveryServiceImpl<CmtsDiscoveryData>
        implements CmtsEntityListener {
    @Resource(name = "cmcDiscoveryDao")
    protected CmcDiscoveryDao cmcDiscoveryDao;
    @Resource(name = "entityDao")
    protected EntityDao entityDao;
    @Resource(name = "entityService")
    protected EntityService entityService;
    @Resource(name = "cmcPerfService")
    protected CmcPerfService cmcPerfService;
    @Resource(name = "cpeService")
    protected CpeService cpeService;
    @Resource(name = "cmtsPerfService")
    protected CmtsPerfService cmtsPerfService;
    @Resource(name = "perfTargetService")
    protected PerfTargetService perfTargetService;
    @Resource(name = "perfThresholdService")
    protected PerfThresholdService perfThresholdService;
    @Resource(name = "entityTypeService")
    protected EntityTypeService entityTypeService;
    @Autowired
    protected CpuAndMemService cpuAndMemService;

    @Override
    @PostConstruct
    public void initialize() {
        messageService.addListener(CmtsEntityListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
    }

    @Override
    public CmtsDiscoveryData discovery(SnmpParam snmpParam) {
        Entity entity = entityService.getEntity(snmpParam.getEntityId());
        CpuAndMemData cpuAndMemData = getCpuAndMemData(entity.getTypeId());
        CmtsDiscoveryData cmtsDiscoveryData = new CmtsDiscoveryData(cpuAndMemData);
        return getFacadeFactory().getFacade(snmpParam.getIpAddress(), CmtsDiscoveryFacade.class).discovery(snmpParam,
                cmtsDiscoveryData);
    }

    @Override
    public void updateEntity(Entity entity, CmtsDiscoveryData data) {
        Long entityId = entity.getEntityId();

        Long cmcId = entity.getEntityId();
        
        Long onuId = entity.getParentId();
        
        Long cmcEntityId = null;
        if (onuId != null) {
            // OLT+ONU+CMTS
            cmcEntityId = entityService.getEntity(onuId).getParentId();
        } else {
            // Single CMTS
            cmcEntityId = entityId;
        }

        DeviceBaseInfo deviceBaseInfo = data.getSystem();
        // 更新entity表
        super.updateEntity(entity, data);

        List<CmcEntityRelation> cmcEntityRelations = new ArrayList<>();
        CmcEntityRelation cmcEntityRelation = new CmcEntityRelation();
        cmcEntityRelation.setCmcType(entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()));
        cmcEntityRelation.setCmcEntityId(cmcEntityId);
        cmcEntityRelation.setOnuId(onuId);
        // cmcEntityRelation.setCmcIndex(data.getCmcIndex());
        cmcEntityRelation.setCmcId(cmcId);
        cmcEntityRelations.add(cmcEntityRelation);

        List<CmcAttribute> cmcAttributes = new ArrayList<>();
        CmcAttribute cmcAttribute = new CmcAttribute();
        cmcAttribute.setEntityId(entityId);
        cmcAttribute.setCmcId(cmcId);
        cmcAttribute.setCmcDeviceStyle(entity.getTypeId());
        cmcAttribute.setTopCcmtsSysDescr(deviceBaseInfo.getSysDescr());
        cmcAttribute.setTopCcmtsSysUpTime(new Long(deviceBaseInfo.getSysUpTime()));
        cmcAttribute.setTopCcmtsSysContact(deviceBaseInfo.getSysContact());
        cmcAttribute.setTopCcmtsSysName(deviceBaseInfo.getSysName());
        cmcAttribute.setTopCcmtsSysLocation(deviceBaseInfo.getSysLocation());
        if (data.getCpuAndMemData() != null) {
            Map<String, Double> finalResult = data.getCpuAndMemData().getComputedMap();
            Double cpuUsed = finalResult.get("cpuUtilization");
            Double memUsed = finalResult.get("memUtilization");
            if (cpuUsed != null) {
                cmcAttribute.setCpuUsed(cpuUsed);
            } else {
                cmcAttribute.setCpuUsed(-1D);
            }
            if (memUsed != null) {
                cmcAttribute.setMemUsed(memUsed);
            } else {
                cmcAttribute.setMemUsed(-1D);
            }
        }
        cmcAttributes.add(cmcAttribute);
        cmcDiscoveryDao.syncCmcEntityInfo(entityId, cmcEntityRelations, cmcAttributes);

        // 上行信道基本信息插入或更新数据库
        if (data.getCmcUpChannelBaseInfos() != null) {
            try {
                cmcDiscoveryDao.batchInsertCmcUpChannelBaseInfo(data.getCmcUpChannelBaseInfos(), cmcId);
            } catch (Exception e) {
                logger.error("Syn CmcUpChannelBaseInfos error ", e);
            }
        }
        // 下行信道基本信息插入或更新数据库
        if (data.getCmcDownChannelBaseInfos() != null) {
            try {
                cmcDiscoveryDao.batchInsertCmcDownChannelBaseInfo(data.getCmcDownChannelBaseInfos(),
                        entity.getEntityId());
            } catch (Exception e) {
                logger.error("Syn CmcUpChannelBaseInfos error ", e);
            }
        }
        // 上行信道信号质量信息插入或更新数据库
        if (data.getCmcUpChannelSignalQualityInfos() != null) {
            try {
                cmcDiscoveryDao.batchInsertCmcUpChannelSignalQualityInfo(data.getCmcUpChannelSignalQualityInfos(),
                        entity.getEntityId());
            } catch (Exception e) {
                logger.error("Syn CmcUpChannelSignalQualityInfos error ", e);
            }
        }

        // 端口基本信息（ifTable）插入或更新数据库
        if (data.getCmcPorts() != null) {
            try {
                // cmcDiscoveryDao.batchInsertCmcPortInfo(data.getCmcPorts(), cmcId);
                if (entityTypeService.isUbrCmts(entity.getTypeId())) {
                    List<CmcPort> cmcPorts = new ArrayList<>();
                    for (CmcPort cmcPort : data.getCmcPorts()) {
                        //UBR的下行信道type不标准 是1 所以必须通过
                        if (cmcPort.getIfDescr().contains("downstream") || cmcPort.getIfDescr().contains("upstream")) {
                            cmcPorts.add(cmcPort);
                        }
                    }
                    // UBR7225上行信道类型为129
                    cmcDiscoveryDao.batchInsertCmcChannelInfoForUbr(cmcPorts, cmcId);
                } else if (entityTypeService.isArrisCmts(entity.getTypeId())) {
                    cmcDiscoveryDao.batchInsertCmcChannelInfoForArris(data.getCmcPorts(), cmcId);
                } else {
                    cmcDiscoveryDao.batchInsertCmcChannelInfo(data.getCmcPorts(), cmcId);
                }
                cmcDiscoveryDao.batchInsertCmtsPorts(cmcId, data.getCmcPorts());// 将iftable所有信息插入端口表（port）
            } catch (Exception e) {
                logger.error("Syn CmcPorts error ", e);
            }
        }

        // CM属性插入或更新数据库
        if (data.getCmAttributes() != null) {
            try {
                List<CmAttribute> cmAttributes = data.getCmAttributes();
                for (CmAttribute cm : cmAttributes) {
                    cm.setCmcId(cmcId);
                }
                cmcDiscoveryDao.batchInsertCmAttribute8800b(cmAttributes, cmcId, entity.getEntityId());
            } catch (Exception e) {
                logger.error("Syn CmAttribute error ", e);
            }
        }

        syncEntityInfo(entity, data);

        if (data.getTopoType().equals(DiscoveryData.BASE_TOPO)) {
            return;
        }

        startPerfMonitor(entityId, data.getSnmpParam());

        sendSynchronizedEvent(entityId, data);
    }

    // 设置CMTS的性能采集
    private void startPerfMonitor(Long entityId, SnmpParam snmpParam) {
        try {
            Entity entity = entityDao.selectByPrimaryKey(entityId);
            PerfGlobal global = perfThresholdService.getCmtsPerfGlobal();
            PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(entity.getTypeId());
            //判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
            boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(entityId);
            if (!isEntityApplyTemplate) {
                // 全局配置_是否关联默认阈值模板
                if (global.getIsRelationWithDefaultTemplate() == 1) {//关联默认阈值模板
                    perfThresholdService.applyEntityThresholdTemplate(entityId, template.getTemplateId(),
                            global.getIsPerfThreshold());
                } else {
                    perfThresholdService.applyEntityThresholdTemplate(entityId, EponConstants.TEMPLATE_ENTITY_UNLINK,
                            EponConstants.PERF_PTHRESHOLD_OFF);
                }
            }
            // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
            if (global.getIsPerfOn() != 1) {
                return;
            }
            // 在线状态
            cmtsPerfService.startCmtsOnlineQuality(entityId, entity.getTypeId(), snmpParam);
            // 信号质量
            cmtsPerfService.startCmtsSignalQuality(entityId, snmpParam, entity.getTypeId());
            // 信道速率
            cmtsPerfService.startCmtsFlowQuality(entityId, snmpParam, entity.getTypeId());
            //内蒙分支,增加CMTS内存利用率和CPU的采集
            cmtsPerfService.startCmtsServiceQuality(entityId, snmpParam, entity.getTypeId());

            CmCollectConfig cmCollectConfig = cpeService.getCmCollectConfig();
            boolean cmStatusMonitor = CmCollectConfig.START.intValue() == cmCollectConfig.getCmCollectStatus()
                    .intValue();
            if (cmStatusMonitor) {
                // 启动CM采集
                cmcPerfService.startCmStatusMonitor(entityId, cmCollectConfig.getCmCollectPeriod(), snmpParam);
            }
        } catch (Exception e) {
            logger.error("Start cmts[{}] perf collect failed: {}", entityId, e);
        }

    }

    @Override
    public void syncEntityInfo(Entity entity, CmtsDiscoveryData data) {
        try {
            CmcAttribute cpuAndMem = data.getCmcAttribute();
            Double cpuUsed = cpuAndMem.getCpuUsed();
            Double memUsed = cpuAndMem.getMemUsed();
            EntityValueEvent event = new EntityValueEvent(entity.getEntityId());
            event.setEntityId(entity.getEntityId());
            event.setState(true);
            if (data.getSystem() != null) {
                event.setSysUpTime(data.getSystem().getSysUpTime());
            }
            if (cpuUsed >= 0 && cpuUsed < 1D) {
                event.setCpu(cpuUsed);
            }
            if (memUsed >= 0 && memUsed < 1D) {
                event.setMem(memUsed);
            }
            event.setActionName("performanceChanged");
            event.setListener(EntityValueListener.class);
            messageService.addMessage(event);
            // YangYi修改 2013-10-22 修正CMTS刷新之后状态错误
            EntitySnap snap = entityDao.getEntitySnapById(entity.getEntityId());
            if (snap == null) {
                snap = new EntitySnap();
                snap.setEntityId(entity.getEntityId());
                snap.setState(true);
                snap.setSnapTimeMillis(System.currentTimeMillis());
                if (StringUtils.isNumeric(event.getSysUpTime())) {
                    snap.setSysUpTime(event.getSysUpTime());
                }
                if (cpuUsed >= 0 && cpuUsed < 1D) {
                    snap.setCpu(cpuUsed);
                }
                if (memUsed >= 0 && memUsed < 1D) {
                    snap.setMem(memUsed);
                }
                entityDao.insertEntitySnap(snap);
            } else {
                snap.setState(true);
                snap.setSnapTimeMillis(System.currentTimeMillis());
                if (StringUtils.isNumeric(event.getSysUpTime())) {
                    snap.setSysUpTime(event.getSysUpTime());
                }
                if (cpuUsed >= 0 && cpuUsed < 1D) {
                    snap.setCpu(cpuUsed);
                }
                if (memUsed >= 0 && memUsed < 1D) {
                    snap.setMem(memUsed);
                }
                entityDao.updateEntitySnap(snap);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public Entity autoRefresh(Entity entity) {
        Long entityId = entity.getEntityId();
        SnmpParam snmpParam = entityDao.getSnmpParamByEntityId(entityId);
        CpuAndMemData cpuAndMemData = getCpuAndMemData(entity.getTypeId());
        CmtsDiscoveryData data = new CmtsDiscoveryData(DiscoveryData.BASE_TOPO);
        data.setCpuAndMemData(cpuAndMemData);
        data = getFacadeFactory().getFacade(snmpParam.getIpAddress(), CmtsDiscoveryFacade.class).autoDiscovery(
                snmpParam, data);
        Timestamp lastRefreshTime = new Timestamp(System.currentTimeMillis());
        entity.setLastRefreshTime(lastRefreshTime);
        updateEntity(entity, data);
        return entity;
    }

    /**
     * 获取CPU和内存采集配置数据
     * @param typeId
     * @return
     */
    protected CpuAndMemData getCpuAndMemData(Long typeId) {
        try {
            CpuAndMemData cpuAndMemData = cpuAndMemService.getCpuAndMemData(EponConstants.MODULE_CMTS, typeId);
            return cpuAndMemData;
        } catch (Throwable e) {
            logger.error("get CpuAndMemData failed: {}", e);
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.topology.event.CmtsEntityListener#cmtsAdded(com.topvision.ems.cmts.topology.event.CmtsEntityEvent)
     */
    @Override
    public void cmtsAdded(CmtsEntityEvent event) {
        List<CmtsEntityInfo> cmtsEntityInfos = event.getCmtsEntityInfos();
        for(CmtsEntityInfo cmtsEntityInfo : cmtsEntityInfos){
            Entity entity = entityService.getEntity(cmtsEntityInfo.getEntityId());
            SnmpParam snmpParam = new SnmpParam();
            snmpParam.setEntityId(cmtsEntityInfo.getEntityId());
            snmpParam.setIpAddress(cmtsEntityInfo.getIpAddress());
            CmtsDiscoveryData data = discovery(snmpParam);
            updateEntity(entity, data);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmts.topology.event.CmtsEntityListener#cmtsRemoved(com.topvision.ems.cmts.topology.event.CmtsEntityEvent)
     */
    @Override
    public void cmtsRemoved(CmtsEntityEvent event) {
        // TODO Auto-generated method stub
        
    }
}
