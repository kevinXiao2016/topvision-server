/***********************************************************************
 * $Id: CmcCpeServiceImpl.java,v1.0 2013-6-18 下午1:14:09 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cmc.cpe.domain.CmFdbTable;
import com.topvision.ems.cmc.cpe.domain.CmFdbTableRemoteQuery;
import com.topvision.ems.cmc.cpe.domain.CmServiceFlow;
import com.topvision.ems.cmc.performance.domain.TopCmControl;
import com.topvision.ems.cmc.performance.facade.callback.CmcPerformanceCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.dao.CmcDao;
import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.cm.dao.CmDao;
import com.topvision.ems.cmc.cm.domain.RealtimeCpe;
import com.topvision.ems.cmc.cm.facade.CmFacade;
import com.topvision.ems.cmc.cpe.dao.CpeDao;
import com.topvision.ems.cmc.cpe.domain.CmLocateInfo;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.Area;
import com.topvision.ems.cmc.domain.CmCmcRunningInfo;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CmCpeCollectDataPolicy;
import com.topvision.ems.cmc.domain.CmCpeNumInArea;
import com.topvision.ems.cmc.domain.CmOltRunningInfo;
import com.topvision.ems.cmc.domain.CmcCmReatimeNum;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.domain.CmcUserNumHisPerf;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.CmcEntityRelation;
import com.topvision.ems.cmc.perf.service.CmcPerfService;
import com.topvision.ems.cmc.performance.domain.CmCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.NetworkSnapManager;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.service.MonitorServiceException;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.util.highcharts.domain.Point;
import com.topvision.platform.zetaframework.util.ZetaUtil;

/**
 * @author loyal
 * @created @2013-6-18-下午1:14:09
 * 
 */
@Service("cpeService")
public class CpeServiceImpl extends CmcBaseCommonService implements CpeService {
    @Resource(name = "cpeDao")
    private CpeDao cpeDao;
    @Resource(name = "cmcDao")
    private CmcDao cmcDao;
    @Resource(name = "cmDao")
    private CmDao cmDao;
    @Resource(name = "cmcListDao")
    private CmcListDao cmcListDao;
    @Resource(name = "cmFacade")
    private CmFacade cmFacade;
    @Resource(name = "systemPreferencesDao")
    private SystemPreferencesDao systemPreferencesDao;
    @Resource(name = "cmcPerfService")
    private CmcPerfService cmcPerfService;
    @Resource(name = "performanceDao")
    private PerformanceDao performanceDao;
    @Resource(name = "entityService")
    private EntityService entityService;
    @Resource(name = "networkSnapManager")
    private NetworkSnapManager networkSnapManager;
    @Resource(name = "entityTypeService")
    private EntityTypeService entityTypeService;
    @Autowired
    private CmcPerformanceCallback cmcPerformanceService;

    @Override
    public CmLocateInfo queryCmLocate(String cmMac) {
        CmLocateInfo info = null;
        StringBuilder sb = new StringBuilder();
        String[] $oids = cmMac.split(":");
        for (String $o : $oids) {
            sb.append(".").append(Integer.parseInt($o, 16));
        }
        String oid = sb.toString();
        List<Long> entityIds = cmDao.queryEntityIdByCmMac(cmMac);
        if (entityIds != null && !entityIds.isEmpty()) {
            for (Long entityId : entityIds) {
                Entity entity = entityService.getEntity(entityId);
                info = queryCmLocate(entity, oid);
                if (info != null) {
                    break;
                }
            }
        }
        if (info == null) {
            List<Entity> list = entityService.getCentralEntity();
            while (!list.isEmpty() && info == null) {
                Entity entity = list.remove(0);
                info = queryCmLocate(entity, oid);
            }
        }
        return info;
    }

    private CmLocateInfo queryCmLocate(Entity entity, String oid) {
        long entityId = entity.getEntityId();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        String result = null;
        List<CmLocateInfo> infoList = null;
        String ip = null;
        long start = System.currentTimeMillis();
        try {
            result = cmFacade.getCmLocate(snmpParam, oid);
            if (logger.isDebugEnabled()) {
                logger.debug("ip:{} time: {}", entity.getIp(), (System.currentTimeMillis() - start));
            }
            if (result != null && !"noSuchInstance".equals(result) || !"noSuchObject".equalsIgnoreCase(result)) {
                ip = cmFacade.getCmIpAddress(snmpParam, result);
            }
        } catch (Exception e) {
            // nothing need to do
        }

        CmLocateInfo cmLocateInfo = null;
        // 如果没有查到或者设备不在线等原因，则遍历全系统
        if (result == null || "noSuchInstance".equals(result) || "noSuchObject".equalsIgnoreCase(result)) {
            // break;
        } else {// 否则则表示已经在该设备中查询到该CM
            start = System.currentTimeMillis();
            long typeId = entity.getTypeId();
            long index = Long.parseLong(result);
            if (entityTypeService.isOlt(typeId)) {
                Long ccIndex = CmcIndexUtils.getCmcIndexFromCmIndex(index);
                infoList = cmDao.queryCmLocate(entityId, ccIndex);
            } else if (entityTypeService.isCmts(typeId)) {
                infoList = cmDao.queryCmLocate(entityId, null);
            } else {// B型CC的处理办法和CMTS类似
                infoList = cmDao.queryCmLocate(entityId, null);
            }

            if (infoList != null && infoList.size() > 0) {
                String location = CmcIndexUtils.getMarkFromIndex(index) + "/" + CmcIndexUtils.getCmId(index);
                cmLocateInfo = infoList.get(0);
                cmLocateInfo.setLocation(location);
                cmLocateInfo.setEntityId(entityId);
                cmLocateInfo.setIp(ip);
                StringBuilder sb = new StringBuilder();
                for (CmLocateInfo $o : infoList) {
                    sb.append(",").append(ZetaUtil.getStaticString($o.getFolderName(), "network"));
                }
                if (!entityTypeService.isOlt(typeId)) {
                    cmLocateInfo.setCcName(cmLocateInfo.getGovName());
                }
                cmLocateInfo.setFolderName(sb.substring(1));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("db query time: " + (System.currentTimeMillis() - start));
            }
        }
        return cmLocateInfo;
    }

    @Override
    public CmCollectConfig getCmCollectConfig() {
        CmCollectConfig cmCollectConfig = new CmCollectConfig();
        List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("cmcTerminal");
        List<SystemPreferences> systemPreferencesCollectTimeConfigList = systemPreferencesDao
                .selectByModule("CollectTimeConfig");
        systemPreferencesList.addAll(systemPreferencesCollectTimeConfigList);
        for (SystemPreferences systemPreferences : systemPreferencesList) {
            if ("cmCollectStatus".equals(systemPreferences.getName())) {
                cmCollectConfig.setCmCollectStatus(new Integer(systemPreferences.getValue()));
            } else if ("cmCollectPeriod".equals(systemPreferences.getName())) {
                cmCollectConfig.setCmCollectPeriod(new Long(systemPreferences.getValue()));
            } else if ("cmTypeStatisticStatus".equals(systemPreferences.getName())) {
                cmCollectConfig.setCmTypeStatisticStatus(new Integer(systemPreferences.getValue()));
            } else if ("cmActionStatisticStatus".equals(systemPreferences.getName())) {
                cmCollectConfig.setCmActionStatisticStatus(new Integer(systemPreferences.getValue()));
            } else if ("cmStatisticSource".equals(systemPreferences.getName())) {
                cmCollectConfig.setCmStatisticSource(new Integer(systemPreferences.getValue()));
            }
        }
        return cmCollectConfig;
    }

    @Override
    public CpeCollectConfig getCpeCollectConfig() {
        CpeCollectConfig cpeCollectConfig = new CpeCollectConfig();
        List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("cmcTerminal");
        List<SystemPreferences> systemPreferencesCollectTimeConfigList = systemPreferencesDao
                .selectByModule("CollectTimeConfig");
        systemPreferencesList.addAll(systemPreferencesCollectTimeConfigList);
        for (SystemPreferences systemPreferences : systemPreferencesList) {
            if ("cpeCollectStatus".equals(systemPreferences.getName())) {
                cpeCollectConfig.setCpeCollectStatus(new Integer(systemPreferences.getValue()));
            } else if ("cpeCollectPeriod".equals(systemPreferences.getName())) {
                cpeCollectConfig.setCpeCollectPeriod(new Long(systemPreferences.getValue()));
            } else if ("cpeNumStatisticStatus".equals(systemPreferences.getName())) {
                cpeCollectConfig.setCpeNumStatisticStatus(new Integer(systemPreferences.getValue()));
            } else if ("cpeActionStatisticStatus".equals(systemPreferences.getName())) {
                cpeCollectConfig.setCpeActionStatisticStatus(new Integer(systemPreferences.getValue()));
            }
        }
        return cpeCollectConfig;
    }

    @Override
    public CmCpeCollectDataPolicy getCmCpeCollectDataPolicy() {
        CmCpeCollectDataPolicy cmCpeCollectDataPolicy = new CmCpeCollectDataPolicy();
        List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("cmcTerminal");
        for (SystemPreferences systemPreferences : systemPreferencesList) {
            if ("initDataSavePolicy".equals(systemPreferences.getName())) {
                cmCpeCollectDataPolicy.setInitDataSavePolicy(new Long(systemPreferences.getValue()));
            } else if ("statisticDataSavePolicy".equals(systemPreferences.getName())) {
                cmCpeCollectDataPolicy.setStatisticDataSavePolicy(new Long(systemPreferences.getValue()));
            } else if ("actionDataSavePolicy".equals(systemPreferences.getName())) {
                cmCpeCollectDataPolicy.setActionDataSavePolicy(new Long(systemPreferences.getValue()));
            } else if ("cmHistorySavePolicy".equals(systemPreferences.getName())) {
                cmCpeCollectDataPolicy.setCmHistorySavePolicy(new Long(systemPreferences.getValue()));
            }
        }
        return cmCpeCollectDataPolicy;
    }

    @Override
    public void modifyCmCollectConfig(CmCollectConfig cmCollectConfig) {
        List<SystemPreferences> systemPreferencesList = new ArrayList<SystemPreferences>();
        SystemPreferences cmCollectStatus = new SystemPreferences();
        cmCollectStatus.setModule("cmcTerminal");
        cmCollectStatus.setName("cmCollectStatus");
        cmCollectStatus.setValue(cmCollectConfig.getCmCollectStatus().toString());
        systemPreferencesList.add(cmCollectStatus);
        Long ipEntityType = entityTypeService.getEntitywithipType();
        try {
            if (cmCollectConfig.getCmCollectStatus() > 0) {
                // 关闭CM采集
                List<Entity> entities = cpeDao.selectEntityWithIp(ipEntityType);
                for (Entity entity : entities) {
                    try {
                        cmcPerfService.stopCmStatusMonitor(entity.getEntityId(), null);
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                }

            } else {
                // 开启CM采集
                List<Entity> entities = cpeDao.selectEntityWithIp(ipEntityType);
                for (Entity entity : entities) {
                    try {
                        cmcPerfService.startCmStatusMonitor(entity.getEntityId(), cmCollectConfig.getCmCollectPeriod(),
                                null);
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                }
            }
        } catch (MonitorServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        SystemPreferences cmCollectPeriod = new SystemPreferences();
        cmCollectPeriod.setModule("CollectTimeConfig");
        cmCollectPeriod.setName("cmCollectPeriod");
        cmCollectPeriod.setValue(cmCollectConfig.getCmCollectPeriod().toString());
        systemPreferencesList.add(cmCollectPeriod);

        performanceDao.updateSchedulePeriod(cmCollectConfig.getCmCollectPeriod(), "CC_CMSTATUS");

        SystemPreferences cmTypeStatisticStatus = new SystemPreferences();
        cmTypeStatisticStatus.setModule("cmcTerminal");
        cmTypeStatisticStatus.setName("cmTypeStatisticStatus");
        cmTypeStatisticStatus.setValue(cmCollectConfig.getCmTypeStatisticStatus().toString());
        systemPreferencesList.add(cmTypeStatisticStatus);

        SystemPreferences cmActionStatisticStatus = new SystemPreferences();
        cmActionStatisticStatus.setModule("cmcTerminal");
        cmActionStatisticStatus.setName("cmActionStatisticStatus");
        cmActionStatisticStatus.setValue(cmCollectConfig.getCmActionStatisticStatus().toString());
        systemPreferencesList.add(cmActionStatisticStatus);

        SystemPreferences statisticSource = new SystemPreferences();
        statisticSource.setModule("cmcTerminal");
        statisticSource.setName("cmStatisticSource");
        statisticSource.setValue(cmCollectConfig.getCmStatisticSource().toString());
        systemPreferencesList.add(statisticSource);
        // 关闭分析线程
        try {
            if (cmCollectConfig.getCmStatisticSource() < 1) {
                // 开启信道cm数采集
                Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
                Long ccType = entityTypeService.getCcmtsType();
                cmcQueryMap.put("type", ccType);
                List<CmcAttribute> cmcAttributes = cmcListDao.queryCmcList(cmcQueryMap, 0, Integer.MAX_VALUE);
                for (CmcAttribute cmcAttribute : cmcAttributes) {
                    try {
                        cmcPerfService.startChannelCmMonitor(cmcAttribute.getCmcId(),
                                cmCollectConfig.getCmCollectPeriod(), null);
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                }
            } else {
                // 关闭信道cm数采集
                Map<String, Object> cmcQueryMap = new HashMap<String, Object>();
                List<CmcAttribute> cmcAttributes = cmcListDao.queryCmcList(cmcQueryMap, 0, Integer.MAX_VALUE);
                for (CmcAttribute cmcAttribute : cmcAttributes) {
                    try {
                        cmcPerfService.stopChannelCmMonitor(cmcAttribute.getCmcId(), null);
                    } catch (Exception e) {
                        logger.debug("", e);
                    }
                }
            }
        } catch (MonitorServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        systemPreferencesDao.updateEntity(systemPreferencesList);
    }

    @Override
    public void modifyCpeCollectConfig(CpeCollectConfig cpeCollectConfig) {
        List<SystemPreferences> systemPreferencesList = new ArrayList<SystemPreferences>();
        SystemPreferences cpeCollectStatus = new SystemPreferences();
        cpeCollectStatus.setModule("cmcTerminal");
        cpeCollectStatus.setName("cpeCollectStatus");
        cpeCollectStatus.setValue(cpeCollectConfig.getCpeCollectStatus().toString());
        systemPreferencesList.add(cpeCollectStatus);
        Long ipEntityType = entityTypeService.getEntitywithipType();
        try {
            if (cpeCollectConfig.getCpeCollectStatus() > 0) {
                // 关闭CPE采集
                List<Entity> entities = cpeDao.selectEntityWithIp(ipEntityType);
                for (Entity entity : entities) {
                    if (entityTypeService.isOlt(entity.getTypeId()) || entityTypeService.isCcmts(entity.getTypeId())) {
                        try {
                            cmcPerfService.stopCpeStatusMonitor(entity.getEntityId(), null);
                        } catch (Exception e) {
                            logger.debug("", e);
                        }
                    }
                }

            } else {
                // 开启CM采集
                List<Entity> entities = cpeDao.selectEntityWithIp(ipEntityType);
                for (Entity entity : entities) {
                    if (entityTypeService.isOlt(entity.getTypeId()) || entityTypeService.isCcmts(entity.getTypeId())) {
                        try {
                            cmcPerfService.startCpeStatusMonitor(entity.getEntityId(),
                                    cpeCollectConfig.getCpeCollectPeriod(), null);
                        } catch (Exception e) {
                            logger.debug("", e);
                        }
                    }
                }
            }
        } catch (MonitorServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("", e);
            }
        }
        SystemPreferences cpeCollectPeriod = new SystemPreferences();
        cpeCollectPeriod.setModule("CollectTimeConfig");
        cpeCollectPeriod.setName("cpeCollectPeriod");
        cpeCollectPeriod.setValue(cpeCollectConfig.getCpeCollectPeriod().toString());
        performanceDao.updateSchedulePeriod(cpeCollectConfig.getCpeCollectPeriod(), "CPE_CMSTATUS");
        systemPreferencesList.add(cpeCollectPeriod);
        SystemPreferences cpeNumStatisticStatus = new SystemPreferences();
        cpeNumStatisticStatus.setModule("cmcTerminal");
        cpeNumStatisticStatus.setName("cpeNumStatisticStatus");
        cpeNumStatisticStatus.setValue(cpeCollectConfig.getCpeNumStatisticStatus().toString());
        systemPreferencesList.add(cpeNumStatisticStatus);

        SystemPreferences cpeActionStatisticStatus = new SystemPreferences();
        cpeActionStatisticStatus.setModule("cmcTerminal");
        cpeActionStatisticStatus.setName("cpeActionStatisticStatus");
        cpeActionStatisticStatus.setValue(cpeCollectConfig.getCpeActionStatisticStatus().toString());
        systemPreferencesList.add(cpeActionStatisticStatus);

        systemPreferencesDao.updateEntity(systemPreferencesList);
    }

    @Override
    public void modifyCmCpeCollectDataPolicy(CmCpeCollectDataPolicy cmCpeCollectDataPolicy) {
        List<SystemPreferences> systemPreferencesList = new ArrayList<SystemPreferences>();
        SystemPreferences initData = new SystemPreferences();
        initData.setModule("cmcTerminal");
        initData.setName("initDataSavePolicy");
        initData.setValue(cmCpeCollectDataPolicy.getInitDataSavePolicy().toString());
        systemPreferencesList.add(initData);

        SystemPreferences statisticData = new SystemPreferences();
        statisticData.setModule("cmcTerminal");
        statisticData.setName("statisticDataSavePolicy");
        statisticData.setValue(cmCpeCollectDataPolicy.getStatisticDataSavePolicy().toString());
        systemPreferencesList.add(statisticData);

        SystemPreferences actionData = new SystemPreferences();
        actionData.setModule("cmcTerminal");
        actionData.setName("actionDataSavePolicy");
        actionData.setValue(cmCpeCollectDataPolicy.getActionDataSavePolicy().toString());
        systemPreferencesList.add(actionData);

        SystemPreferences cmHistoryData = new SystemPreferences();
        cmHistoryData.setModule("cmcTerminal");
        cmHistoryData.setName("cmHistorySavePolicy");
        cmHistoryData.setValue(cmCpeCollectDataPolicy.getCmHistorySavePolicy().toString());
        systemPreferencesList.add(cmHistoryData);

        systemPreferencesDao.updateEntity(systemPreferencesList);
    }

    @Override
    public List<Area> getAllAreas() {
        return cpeDao.selectAllAreas();
    }

    @Override
    public List<Entity> getEntityWithIp() {
        Long ipEntityType = entityTypeService.getEntitywithipType();
        return cpeDao.selectEntityWithIp(ipEntityType);
    }

    @Override
    public List<CmcEntity> getCcmstWithOutAgent() {
        Long type = entityTypeService.getCcmtswithoutagentType();
        return cpeDao.selectCcmstWithOutAgent(type);
    }

    @Override
    public List<CmcUserNumHisPerf> getAllUserNumHis(Map<String, Object> queryMap) {
        return cpeDao.selectAllUserNumHis(queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByArea(Long regionId) {
        return cpeDao.selectUserNumHisByArea(regionId);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByDevice(Map<String, Object> queryMap) {
        return cpeDao.selectUserNumHisByDevice(queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByCmc(Map<String, Object> queryMap) {
        return cpeDao.selectUserNumHisByCmc(queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByChannel(Map<String, Object> queryMap) {
        return cpeDao.selectUserNumHisByChannel(queryMap);
    }

    @Override
    public List<CmCpeNumInArea> getCmCpeNumInRegion() {
        return cpeDao.selectCmCpeNumInRegion();
    }

    @Override
    public List<CmNum> getAllAllDeviceCmNum(Map<String, Object> map) {
        Long entityWithIpType = entityTypeService.getEntitywithipType();
        map.put("type", entityWithIpType);
        return cpeDao.selectAllAllDeviceCmNum(map);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByCmcWithOutAgent(Map<String, Object> queryMap) {
        return cpeDao.selectUserNumHisByCmcWithOutAgent(queryMap);
    }

    @Override
    public CmcEntityRelation getCmcByIndexAndEntityId(Long cmcIndex, Long entityId) {
        return cpeDao.selectCmcByIndexAndEntityId(cmcIndex, entityId);
    }

    @Override
    public List<CmcCmReatimeNum> getCcmtsCmNumInfo(Long entityId) {
        return cpeDao.selectCcmtsCmNumInfo(entityId);
    }

    @Override
    public List<Point> getCpuHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCpuHisPerf(queryMap);
    }

    @Override
    public List<Point> getSnrHisPerf(Map<String, Object> queryMap) {
        if (entityTypeService.isCmts((long) queryMap.get("entityType"))) {
            return cpeDao.selectCmtsSnrHisPerf(queryMap);
        } else {
            return cpeDao.selectSnrHisPerf(queryMap);
        }
    }

    @Override
    public List<Point> getUpChannelFlowHisPerf(Map<String, Object> queryMap) {
        if (entityTypeService.isCmts((long) queryMap.get("entityType"))) {
            return cpeDao.selectCmtsUpChannelFlowHisPerf(queryMap);
        } else {
            return cpeDao.selectUpChannelFlowHisPerf(queryMap);
        }
    }

    @Override
    public List<Point> getDownChannelFlowHisPerf(Map<String, Object> queryMap) {
        if (entityTypeService.isCmts((long) queryMap.get("entityType"))) {
            return cpeDao.selectCmtsDownChannelFlowHisPerf(queryMap);
        } else {
            return cpeDao.selectDownChannelFlowHisPerf(queryMap);
        }
    }

    @Override
    public CmCmcRunningInfo getCmCmcRunningInfo(Long cmId) {
        return cpeDao.selectCmCmcRunningInfo(cmId);
    }

    @Override
    public CmCmcRunningInfo getCmCmtsRunningInfo(Long cmId) {
        return cpeDao.selectCmCmtsRunningInfo(cmId);
    }

    @Override
    public List<CmCpeAttribute> queryCmCpeListByCondition(String cmMac, String cpeMac, String cpeIp,
            String cmLocation) {
        return cpeDao.queryCmCpeListByCondition(cmMac, cpeMac, cpeIp, cmLocation);
    }

    @Override
    public List<CmCpeAttribute> queryCmCpeListByConditionHis(String cmMac, Long cpeMacLong, Long cpeIpLong,
            String cmLocation) {
        return cpeDao.queryCmCpeListByConditionHis(cmMac, cpeMacLong, cpeIpLong, cmLocation);
    }

    @Override
    public List<Point> getBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectBitErrorRateHisPerf(queryMap);
    }

    @Override
    public List<Point> getCmtsBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmtsBitErrorRateHisPerf(queryMap);
    }

    @Override
    public List<Point> getUnBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectUnBitErrorRateHisPerf(queryMap);
    }

    @Override
    public List<Point> getCmtsUnBitErrorRateHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmtsUnBitErrorRateHisPerf(queryMap);
    }

    @Override
    public List<Point> getCmOnlineNumHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmOnlineNumHisPerf(queryMap);
    }

    @Override
    public List<Point> getCmOfflineNumHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmOfflineNumHisPerf(queryMap);
    }

    @Override
    public CmOltRunningInfo getCmOltRunningInfo(Long cmId) {
        return cpeDao.selectCmOltRunningInfo(cmId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cpe.service.CpeService#getCmtsSnrHisPerf(java.util.Map)
     */
    @Override
    public List<Point> getCmtsSnrHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmtsSnrHisPerf(queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.cpe.service.CpeService#getCmtsChannelFlowHisPerf(java.util.Map)
     */
    @Override
    public List<Point> getCmtsUpChannelFlowHisPerf(Map<String, Object> queryMap) {
        return cpeDao.selectCmtsUpChannelFlowHisPerf(queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByCmcNew(Map<String, Object> queryMap) {
        return cpeDao.getUserNumHisByCmcNew(queryMap);
    }

    @Override
    public List<CmcUserNumHisPerf> getUserNumHisByPon(Map<String, Object> queryMap) {
        return cpeDao.getUserNumHisByPon(queryMap);
    }

    @Override
    public Long getPonIndexByPonId(Long ponId) {
        return cpeDao.getPonIndexByPonId(ponId);
    }

    @Override
    public Long getCmcIndexByCmcId(Long cmcId) {
        return cpeDao.getCmcIndexByCmcId(cmcId);
    }

    @Override
    public List<Long> getAllOnlineIds() {
        return cpeDao.getAllOnlineIds();
    }

    @Override
    public List<CmCpe> getCpeListByCmId(Long cmId) {
        return cpeDao.getCpeListByCmId(cmId);
    }

    @Override
    public List<RealtimeCpe> getRealCpeListByCmMac(Long cmcId, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        Long cmIndex = cmFacade.getCmIndexByCmMac(snmpParam, cmMac);
        List<RealtimeCpe> realtimeCpes = cmFacade.getCpeListByCmIndex(snmpParam, cmIndex);
        return realtimeCpes;
    }

    @Override
    public List<RealtimeCpe> getRealCpeListByCmId(Long cmId) {
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmAttribute.getEntityId());
        Long cmIndex = cmFacade.getCmIndexByCmMac(snmpParam, cmAttribute.getStatusMacAddress());
        List<RealtimeCpe> realtimeCpes = cmFacade.getCpeListByCmIndex(snmpParam, cmIndex);
        return realtimeCpes;
    }

    @Override
    public CmCpe getCpeByCpeMac(Long cmId, String cpeMac) {
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmAttribute.getEntityId());
        CmCpe cmCpe = cmFacade.getCpeByCpeMac(snmpParam, cpeMac);
        return cmCpe;
    }

    @Override
    public CmCpe refreshCpeByCmcAndCpeMac(Long cmcId, String cpeMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        CmCpe cmCpe = cmFacade.getCpeByCpeMac(snmpParam, cpeMac);
        return cmCpe;
    }

    @Override
    public boolean isCmPollStart() {
        Long cmPollStatus = getCmPollParams("cmPollStatus");
        return cmPollStatus == 1 ? true : false;
    }

    @Override
    public Long getCmPollInterval() {
        return getCmPollParams("cmPollInterval");
    }

    @Override
    public void modifyCmSnmpParamConfig(String readConmunity, String writeConmunity) {
        List<SystemPreferences> systemPreferencesList = new ArrayList<SystemPreferences>();
        SystemPreferences read = new SystemPreferences();
        read.setModule("cmcTerminal");
        read.setName("cmReadCommunity");
        read.setValue(readConmunity);
        systemPreferencesList.add(read);

        SystemPreferences write = new SystemPreferences();
        write.setModule("cmcTerminal");
        write.setName("cmWriteCommunity");
        write.setValue(writeConmunity);
        systemPreferencesList.add(write);

        systemPreferencesDao.updateEntity(systemPreferencesList);
    }

    @Override
    public String getCmReadCommunity() {
        return systemPreferencesDao.selectByModuleAndName("cmcTerminal", "cmReadCommunity").getValue();
    }

    @Override
    public String getCmWriteCommunity() {
        return systemPreferencesDao.selectByModuleAndName("cmcTerminal", "cmWriteCommunity").getValue();
    }

    @Override
    public Integer loadCmStatusOnCmts(Long cmcId, String cmMac) {
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        Long cmcIndex = getCmcIndexByCmcId(cmcId);
        if (snmpParam == null || cmcIndex == null) {
            return CmAttribute.OFFLINESTATUS;
        }
        try {
            CmAttribute cmAttribute = cmFacade.getCmAttributeOnDol(snmpParam,cmcIndex,cmMac);
            if (cmAttribute != null) {
                return cmAttribute.getStatusValue();
            } else {
                return CmAttribute.OFFLINESTATUS;
            }
        } catch (Exception e) {
            logger.debug("",e);
            return CmAttribute.OFFLINESTATUS;
        }
    }

    @Override
    public Integer loadCmPreStatusOnCmts(Long cmcId, Long cmIndex) {
        boolean isSupportCmPreStatus = cmcPerformanceService.isSupportCmPreStatus(cmcId);
        if (!isSupportCmPreStatus) {
            return TopCmControl.VERSIONNOTSUPPORTED;
        }
        SnmpParam snmpParam = getSnmpParamByCmcId(cmcId);
        Long cmcIndex = getCmcIndexByCmcId(cmcId);
        if (snmpParam == null || cmcIndex == null) {
            return CmAttribute.OFFLINESTATUS;
        }
        try {
            TopCmControl topCmControl = cmFacade.getCmPreStatusOnCmts(snmpParam, cmcIndex, cmIndex);
            if (topCmControl != null) {
                return topCmControl.getTopCmPreStatus();
            } else {
                return TopCmControl.NOTCOLLECTED;
            }
        } catch (Exception e) {
            logger.debug("",e);
            return CmAttribute.OFFLINESTATUS;
        }
    }

    @Override
    public List<CmServiceFlow> getCmServiceFlowByCmId(Long cmId) {
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmAttribute.getEntityId());
        Long cmIndex = cmFacade.getCmIndexByCmMac(snmpParam, cmAttribute.getStatusMacAddress());
        List<CmServiceFlow> cmsf = cmFacade.getCmServiceFlowByCmIndex(snmpParam, cmIndex);
        return cmsf;
    }

    @Override
    public List<CmFdbTableRemoteQuery> getFdbTableRemoteQuery(Long cmId) {
        CmAttribute cmAttribute = cmDao.getCmAttributeByCmId(cmId);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(cmAttribute.getEntityId());
        Long cmIndex = cmFacade.getCmIndexByCmMac(snmpParam, cmAttribute.getStatusMacAddress());
        List<CmFdbTableRemoteQuery> cmFdbAddressList = cmFacade.getCmFdbAddressByCmIndex(snmpParam, cmIndex);
        return cmFdbAddressList;
    }

    @Override
    public List<CmFdbTable> getFdbTable(SnmpParam snmpParam) {
        return cmFacade.getFdbTable(snmpParam);
    }

    private Long getCmPollParams(String name) {
        SystemPreferences perferences = systemPreferencesDao.selectByModuleAndName("cmPoll", name);
        return new Long(perferences.getValue());
    }

    public CpeDao getCpeDao() {
        return cpeDao;
    }

    public void setCpeDao(CpeDao cpeDao) {
        this.cpeDao = cpeDao;
    }

    public SystemPreferencesDao getSystemPreferencesDao() {
        return systemPreferencesDao;
    }

    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    public CmcPerfService getCmcPerfService() {
        return cmcPerfService;
    }

    public void setCmcPerfService(CmcPerfService cmcPerfService) {
        this.cmcPerfService = cmcPerfService;
    }

    @Override
    public CmcDao getCmcDao() {
        return cmcDao;
    }

    @Override
    public void setCmcDao(CmcDao cmcDao) {
        this.cmcDao = cmcDao;
    }

    public PerformanceDao getPerformanceDao() {
        return performanceDao;
    }

    public void setPerformanceDao(PerformanceDao performanceDao) {
        this.performanceDao = performanceDao;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public NetworkSnapManager getNetworkSnapManager() {
        return networkSnapManager;
    }

    public void setNetworkSnapManager(NetworkSnapManager networkSnapManager) {
        this.networkSnapManager = networkSnapManager;
    }

} 
