/***********************************************************************
 * $Id: OnuCpeServiceImpl.java,v1.0 2016年7月5日 下午3:30:21 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.onu.service.OnuAssemblyService;
import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.onucpe.dao.OnuCpeDao;
import com.topvision.ems.epon.onucpe.domain.OnuCpeConfig;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.onucpe.facade.OnuCpeFacade;
import com.topvision.ems.epon.onucpe.facade.OnuCpeUtil;
import com.topvision.ems.epon.onucpe.service.OnuCpeService;
import com.topvision.ems.epon.performance.dao.OnuPerfDao;
import com.topvision.ems.epon.performance.domain.GponOnuUniCpeList;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuCpeIpInfo;
import com.topvision.ems.epon.performance.domain.OnuCpeStatusPerf;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.MonitorServiceException;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Bravin
 * @created @2016年7月5日-下午3:30:21
 *
 */
@Service
public class OnuCpeServiceImpl extends BaseService implements OnuCpeService, EntityListener {
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private PerformanceService<?> performanceService;
    @Autowired
    private OnuPerfDao onuPerfDao;
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OnuCpeDao onuCpeDao;
    @Autowired
    private OnuService onuService;
    @Autowired
    private OnuAssemblyService onuAssemblyService;
    @Autowired
    private OnuCpeFacade onuCpeFacade;

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onucpe.service.OnuCpeService#updateOnuCpeConfig(com.topvision.ems.
     * epon.onucpe.domain.OnuCpeConfig)
     */
    @Override
    public void updateOnuCpeConfig(OnuCpeConfig config) {
        Long ipEntityType = entityTypeService.getOltType();
        List<Entity> entities = entityService.getEntityByType(ipEntityType);
        if (config.getOnuCpeStatus() > 0) {
            for (Entity entity : entities) {
                try {
                    startOnuCpeStatus(entity.getEntityId(), config.getOnuCpeInterval().longValue());
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
        } else {
            for (Entity entity : entities) {
                try {
                    SnmpParam snmpParam = entityService.getSnmpParamByEntity(entity.getEntityId());
                    stopOnuCpeStatus(entity.getEntityId(), snmpParam);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
        }

        List<SystemPreferences> systemPreferencesList = new ArrayList<SystemPreferences>();
        SystemPreferences intervalpreference = new SystemPreferences();
        intervalpreference.setModule("onuCpe");
        intervalpreference.setName("onuCpeInterval");
        intervalpreference.setValue(config.getOnuCpeInterval().toString());
        systemPreferencesList.add(intervalpreference);
        SystemPreferences statusPreferences = new SystemPreferences();
        statusPreferences.setModule("onuCpe");
        statusPreferences.setName("onuCpeStatus");
        statusPreferences.setValue(config.getOnuCpeStatus().toString());
        systemPreferencesList.add(statusPreferences);
        systemPreferencesService.savePreferences(systemPreferencesList);
    }

    @Override
    public synchronized void startOnuCpeStatus(Long entityId, Long period) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OnuCpeStatusPerf onuCpeStatusPerf = new OnuCpeStatusPerf();
        onuCpeStatusPerf.setEntityId(entityId);
        Long monitorId = onuPerfDao.getMonitorIdByIdentifyKeyAndCategory(entityId, onuCpeStatusPerf.getCategory());
        try {
            if (monitorId != null && monitorId > 0) {
                performanceService.stopMonitor(snmpParam, monitorId);
            }
            performanceService.startMonitor(snmpParam, onuCpeStatusPerf, period, period,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        } catch (Exception exception) {
            logger.error("startOnuCpeStatus[{}] failed: {}", entityId, exception);
        }
    }

    @Override
    public void stopOnuCpeStatus(Long entityId, SnmpParam snmpParam) {
        OnuCpeStatusPerf onuCpeStatusPerf = new OnuCpeStatusPerf();
        Long monitorId = onuPerfDao.getMonitorIdByIdentifyKeyAndCategory(entityId, onuCpeStatusPerf.getCategory());
        if (monitorId != null && monitorId > 0) {
            performanceService.stopMonitor(snmpParam, monitorId);
        } else {
            throw new MonitorServiceException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.impl.DiscoveryServiceImpl#entityRemoved(com.topvision.platform
     * .message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        Entity entity = entityService.getEntity(entityId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            stopOnuCpeStatus(entityId, snmpParam);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityAdded(com.topvision.platform.message
     * .event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        Entity entity = entityService.getEntity(entityId);
        if (entity != null && entityTypeService.isOlt(entity.getTypeId())) {
            SystemPreferences sp = systemPreferencesService.selectByModuleAndName("onuCpe", "onuCpeStatus");
            Integer onuCpeStatus = Integer.parseInt(sp.getValue());
            if (onuCpeStatus > 0) {
                sp = systemPreferencesService.selectByModuleAndName("onuCpe", "onuCpeInterval");
                Long onuCpeInterval = Long.parseLong(sp.getValue());
                startOnuCpeStatus(entityId, onuCpeInterval);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityDiscovered(com.topvision.platform
     * .message.event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.message.event.EntityListener#attributeChanged(long,
     * java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#entityChanged(com.topvision.platform.
     * message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.platform.message.event.EntityListener#managerChanged(com.topvision.platform
     * .message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onucpe.service.OnuCpeService#loadOnuUniCpeList(java.lang.Long)
     */
    @Override
    public List<OnuUniCpe> loadOnuUniCpeList(Long onuId) {
        return onuCpeDao.selectOnuUniCpeList(onuId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onucpe.service.OnuCpeService#refreshOnuUniCpe(java.lang.Long)
     */
    @Override
    public void refreshOnuUniCpe(Long onuId) {
        OltOnuAttribute onuAttribute = onuService.getOnuAttribute(onuId);
        Long entityId = onuAttribute.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<UniPort> list = onuAssemblyService.loadUniList(onuId);
        onuCpeDao.deleteCpeListByOnuId(onuId);
        onuCpeDao.deleteCpeCountListByOnuId(onuId);
        List<OnuUniCpeCount> onuUniCpeCounts = new ArrayList<>();
        Long onuIndex = onuAttribute.getOnuIndex();
        Long slot = EponIndex.getSlotNo(onuIndex);
        Long pon = EponIndex.getPonNo(onuIndex);
        Long onu = EponIndex.getOnuNo(onuIndex);
        List<OnuCpeIpInfo> ipInfos = null;

        if ("E".equalsIgnoreCase(onuAttribute.getOnuEorG())) {
            try {
                String message = onuCpeFacade.fetchOnuCpeLocationList(snmpParam, slot + "." + pon + "." + onu);
                ipInfos = OnuCpeUtil.parseOnuCpeIpInfoList(message);
            } catch (Exception e) {
                logger.trace("device may not support this version", e);
            }
        }
        for (UniPort uniPort : list) {
            Long uniIndex = uniPort.getUniIndex();
            OnuUniCpeList onuUniCpeList = null;
            if (EponConstants.EPON_ONU.equalsIgnoreCase(onuAttribute.getOnuEorG())) {
                try {
                    onuUniCpeList = onuCpeFacade.refreshEponOnuUniCpe(snmpParam, uniIndex);
                } catch (SnmpNoSuchInstanceException e) {
                    logger.info("refreshEponOnuUniCpe fail {0}", e);
                } catch (Exception e) {
                    logger.info("refreshEponOnuUniCpe error {0}", e);
                }
            } else {
                GponOnuUniCpeList gponOnuUniCpeList = onuCpeFacade.refreshGponOnuUniCpe(snmpParam, uniIndex);
                onuUniCpeList = OnuCpeUtil.makeOnuUniCpeList(gponOnuUniCpeList);
            }
            List<OnuCpe> cpeList = OnuCpeUtil.makeOnuCpeList(onuUniCpeList, entityId, ipInfos);
            OnuUniCpeCount onuUniCpeCount = OnuCpeUtil.makeOnuUniCpeCount(onuUniCpeList, cpeList);
            onuCpeDao.batchinsertOnuCpe(cpeList);
            onuUniCpeCount.setEntityId(entityId);
            onuUniCpeCounts.add(onuUniCpeCount);
        }
        // 统计UNI口下的CPE个数是以OLT为单位来处理的
        onuCpeDao.batchInsertOnuCpeCount(onuUniCpeCounts);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onucpe.service.OnuCpeService#loadOltUniCpeList(java.lang.Long,
     * int, int)
     */
    @Override
    public List<OnuUniCpe> loadOltUniCpeList(Long entityId, int start, int limit) {
        return onuCpeDao.selectOltUniCpeList(entityId, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onucpe.service.OnuCpeService#loadOltUniCpeListCount(java.lang.Long)
     */
    @Override
    public int loadOltUniCpeListCount(Long oltId) {
        return onuCpeDao.selectOltUniCpeListCount(oltId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onucpe.service.OnuCpeService#execDataClean()
     */
    @Override
    public void execDataClean() {

    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }
}
