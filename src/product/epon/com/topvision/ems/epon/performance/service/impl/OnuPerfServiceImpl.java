/***********************************************************************
 * $Id: OnuPerfServiceImpl.java,v1.0 2015-4-22 上午11:20:08 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.performance.dao.OltPerfDao;
import com.topvision.ems.epon.performance.dao.OnuPerfDao;
import com.topvision.ems.epon.performance.dao.OnuPerfGraphDao;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoPerf;
import com.topvision.ems.epon.performance.domain.OnuFlowQualityPerf;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityPerf;
import com.topvision.ems.epon.performance.domain.OnuOnlinePerf;
import com.topvision.ems.epon.performance.domain.PerfOnuQualityHistory;
import com.topvision.ems.epon.performance.service.OnuPerfService;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.domain.DevicePerfTarget;
import com.topvision.ems.performance.domain.PerfGlobal;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.PerfThresholdTemplate;
import com.topvision.ems.performance.service.DevicePerfTargetService;
import com.topvision.ems.performance.service.PerfTargetData;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author flack
 * @created @2015-4-22-上午11:20:08
 *
 */
@Service("onuPerfService")
public class OnuPerfServiceImpl extends BaseService implements OnuPerfService, PerfTargetData {
    @Resource(name = "performanceService")
    private PerformanceService<OperClass> performanceService;
    @Autowired
    private DevicePerfTargetService devicePerfTargetService;
    @Autowired
    private OltPerfDao oltPerfDao;
    @Autowired
    private OnuPerfGraphDao onuPerfGraphDao;
    @Autowired
    private OnuDao onuDao;
    @Autowired
    private OnuPerfDao onuPerfDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private PerfThresholdService perfThresholdService;

    @Override
    public synchronized void startOnuOnlineQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId) {
        try {
            if (this.hasOnuPerfMonitor(onuId, "onuOnlinePerf")) {
                return;
            }
            List<DevicePerfTarget> targetList = devicePerfTargetService.getTargetByTypeIdAndGroup(typeId,
                    PerfTargetConstants.ONU_DEVICESTATUS);
            for (DevicePerfTarget target : targetList) {
                if (devicePerfTargetService.isTargetDataExists(onuId, target.getPerfTargetName())) {
                    //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                    continue;
                }
                if (PerfTargetConstants.ONU_ONLINESTATUS.equals(target.getPerfTargetName())) {
                    if (target.getGlobalEnable() == PerfTargetConstants.TARGET_ENABLE_ON) {
                        OnuOnlinePerf onlinePerf = new OnuOnlinePerf();
                        onlinePerf.setEntityId(entityId);
                        onlinePerf.setOnuId(onuId);
                        onlinePerf.setOnuIndex(onuIndex);
                        OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);
                        onlinePerf.setOnuEorG(onuAttribute.getOnuEorG());
                        long period = target.getGlobalInterval() * 60000;
                        try {
                            performanceService.startMonitor(snmpParam, onlinePerf, period, period,
                                    PerformanceConstants.PERFORMANCE_DOMAIN,
                                    PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                            // 插入对应性能指标数据
                            this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                        } catch (Exception e) {
                            // 采集开启失败的情况下，将性能指标状态置为关闭
                            logger.error(e.getMessage());
                            this.handleSpecialTarget(target, onuId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                        }
                    } else {
                        // 全局没有开启的情况下也需要插入
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("startOnuOnlineQuality[{}] failed: {}", onuId, e);
        }
    }

    @Override
    public synchronized void startOnuLinkQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId) {
        try {
            if (this.hasOnuPerfMonitor(onuId, "onuLinkQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isTargetDataExists(onuId, PerfTargetConstants.ONU_OPTLINK)) {
                //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            }
            DevicePerfTarget target = devicePerfTargetService.getGlobalTargetByType(PerfTargetConstants.ONU_OPTLINK,
                    typeId);
            if (target != null) {
                if (target.getGlobalEnable() == PerfTargetConstants.TARGET_ENABLE_ON) {
                    OnuLinkQualityPerf linkQualityPerf = new OnuLinkQualityPerf();
                    linkQualityPerf.setEntityId(entityId);
                    linkQualityPerf.setOnuId(onuId);
                    linkQualityPerf.setOnuIndex(onuIndex);
                    OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);
                    linkQualityPerf.setOnuEorG(onuAttribute.getOnuEorG());
                    long period = target.getGlobalInterval() * 60000;
                    try {
                        performanceService.startMonitor(snmpParam, linkQualityPerf, period, period,
                                PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                    } catch (Exception e) {
                        // 采集开启失败的情况下，将性能指标状态置为关闭
                        logger.error(e.getMessage());
                        this.handleSpecialTarget(target, onuId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                } else {
                    // 全局没有开启的情况下也需要插入
                    // 插入对应性能指标数据
                    this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                }
            }
        } catch (Exception e) {
            logger.error("startOnuLinkQuality[{}] failed: {}", onuId, e);
        }
    }

    @Override
    public synchronized void startOnuFlowQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId) {
        try {
            if (this.hasOnuPerfMonitor(onuId, "onuFlowQualityPerf")) {
                return;
            } else if (devicePerfTargetService.isTargetDataExists(onuId, PerfTargetConstants.ONU_PORTFLOW)) {
                //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            }
            DevicePerfTarget target = devicePerfTargetService.getGlobalTargetByType(PerfTargetConstants.ONU_PORTFLOW,
                    typeId);
            if (target != null) {
                if (target.getGlobalEnable() == PerfTargetConstants.TARGET_ENABLE_ON) {
                    OnuFlowQualityPerf flowQualityPerf = new OnuFlowQualityPerf();
                    flowQualityPerf.setEntityId(entityId);
                    flowQualityPerf.setOnuId(onuId);
                    flowQualityPerf.setOnuIndex(onuIndex);
                    OltOnuAttribute onuAttribute = onuDao.getOnuEntityById(onuId);
                    flowQualityPerf.setOnuEorG(onuAttribute.getOnuEorG());
                    flowQualityPerf.setUniIndexList(onuPerfDao.queryUniIndexByOnuId(onuId));
                    flowQualityPerf.setPonIndexList(onuPerfDao.queryPonIndexByOnuId(onuId));
                    long period = target.getGlobalInterval() * 60000;
                    try {
                        performanceService.startMonitor(snmpParam, flowQualityPerf, period, period,
                                PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                    } catch (Exception e) {
                        // 采集开启失败的情况下，将性能指标状态置为关闭
                        logger.error(e.getMessage());
                        this.handleSpecialTarget(target, onuId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                } else {
                    // 插入对应性能指标数据(全局没有开启的情况下也需要插入)
                    this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                }
            }
        } catch (Exception e) {
            logger.error("startOnuFlowQuality[{}] failed: {}", onuId, e);
        }
    }

    @Override
    public void stopOnuFlowQuality(Long onuId, SnmpParam snmpParam) {
        List<Integer> result = getOnuPerfMonitor(onuId, "onuFlowQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, tmp.longValue());
            }
        }
    }

    @Override
    public void stopOnuOnlineQuality(Long onuId, SnmpParam snmpParam) {
        List<Integer> result = getOnuPerfMonitor(onuId, "onuOnlinePerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, tmp.longValue());
            }
        }
    }

    @Override
    public void stopOnuLinkQuality(Long onuId, SnmpParam snmpParam) {
        List<Integer> result = getOnuPerfMonitor(onuId, "onuLinkQualityPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, tmp.longValue());
            }
        }
    }

    @Override
    public boolean hasOnuPerfMonitor(Long onuId, String category) {
        List<Integer> result = getOnuPerfMonitor(onuId, category);
        if (result == null || result.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<Integer> getOnuPerfMonitor(Long onuId, String category) {
        List<Integer> result = oltPerfDao.getEponPerformanceMonitorId(onuId, category);
        return result;
    }

    @Override
    public Object perfTargetChangeData(Long entityId, String targetName) {
        if (PerfTargetConstants.ONU_PORTFLOW.equals(targetName)) {
            //需要返回onuInfo(onu与olt关联信息),onu ponIndex, uniIndex
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("onuInfo", onuDao.queryOltOnuRelation(entityId));
            dataMap.put("ponIndex", onuPerfDao.queryPonIndexByOnuId(entityId));
            dataMap.put("uniIndex", onuPerfDao.queryUniIndexByOnuId(entityId));
            return dataMap;
        } else {
            return onuDao.queryOltOnuRelation(entityId);
        }
    }

    /**
     * 处理设备性能指标的插入 主要是处理单个性能指标插入的情况
     * 
     * @param target
     * @param entityId
     * @param typeId
     * @param targetEnable
     */
    private void handleSpecialTarget(DevicePerfTarget target, Long entityId, Long typeId, Integer targetEnable) {
        DevicePerfTarget deviceTarget = new DevicePerfTarget(entityId, target.getPerfTargetName(),
                target.getParentType(), target.getEntityType(), typeId, target.getGlobalInterval(), targetEnable);
        devicePerfTargetService.addDevicePerfTarget(deviceTarget);
    }

    @Override
    public List<Point> queryOnuOptLinkPerfPoints(Long onuId, String perfTarget, String startTime, String endTime) {
        return onuPerfGraphDao.selectOnuOptPerfPoints(onuId, perfTarget, startTime, endTime);
    }

    @Override
    public List<Long> getOnuPonIndexList(Long onuId) {
        return onuPerfDao.queryPonIndexByOnuId(onuId);
    }

    @Override
    public List<Long> getOnuUniIndexList(Long onuId) {
        return onuPerfDao.queryUniIndexByOnuId(onuId);
    }

    @Override
    public List<Point> getOnuFlowPerfPoints(Map<String, Object> paramMap) {
        return onuPerfGraphDao.queryOnuFlowPerfPoints(paramMap);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.service.OnuPerfService#stopOnuPerfCollect(java.lang.Long, com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopOnuPerfCollect(Long onuId, SnmpParam snmpParam) {
        //关闭Onu在线状态采集
        try {
            stopOnuOnlineQuality(onuId, snmpParam);
        } catch (Exception e) {
            logger.error("stopOnuOnlineQuality failed: {}", e);
        }
        //关闭Onu链路质量采集
        try {
            stopOnuLinkQuality(onuId, snmpParam);
        } catch (Exception e) {
            logger.error("stopOnuLinkQuality failed: {}", e);
        }
        //关闭Onu速率采集
        try {
            stopOnuFlowQuality(onuId, snmpParam);
        } catch (Exception e) {
            logger.error("stopOnuFlowQuality failed: {}", e);
        }
        //关闭Onu Catv采集
        try {
            stopOnuCatvQuality(onuId, snmpParam);
        } catch (Exception e) {
            logger.error("stopOnuFlowQuality failed: {}", e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.service.OnuPerfService#startOnuPerfCollect(java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public void startOnuPerfCollect(Long entityId, Long onuId, Long onuIndex) {
        //处理ONU性能的开启
        Long onuType = entityTypeService.getOnuType();
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        PerfGlobal global = perfThresholdService.getPerfGlobalByType(onuType);
        PerfThresholdTemplate template = perfThresholdService.getDefaultTemplateByEntityType(onuType);
        //判断当前是否存在设备与模板关联记录,如果存在，则不处理;如果不存在，刚根据全局的配置进行添加设备与阈值模板的关联关系。
        boolean isEntityApplyTemplate = perfThresholdService.isEntityApplyTemplate(onuId);
        if (!isEntityApplyTemplate) {
            // 全局配置_是否关联默认阈值模板
            if (global.getIsRelationWithDefaultTemplate() == 1) {//关联默认阈值模板
                perfThresholdService.applyEntityThresholdTemplate(onuId, template.getTemplateId(),
                        global.getIsPerfThreshold());
            } else {
                perfThresholdService.applyEntityThresholdTemplate(onuId, EponConstants.TEMPLATE_ENTITY_UNLINK,
                        EponConstants.PERF_PTHRESHOLD_OFF);
            }
        }
        // 全局配置_是否启动性能采集,全局配置必须放在最后，保证上述两个全局配置生效
        if (global.getIsPerfOn() != 1) {
            return;
        }
        logger.info("Begin to start perfCollect: {}", onuId);
        //开启Onu在线状态采集
        startOnuOnlineQuality(entityId, onuId, onuIndex, snmpParam, onuType);
        //开启Onu链路质量采集
        startOnuLinkQuality(entityId, onuId, onuIndex, snmpParam, onuType);
        //开启Onu速率采集
        startOnuFlowQuality(entityId, onuId, onuIndex, snmpParam, onuType);
        //开启Onu Catv采集
        startOnuCatvQuality(entityId, onuId, onuIndex, snmpParam, onuType);
        logger.info("End to start perfCollect: {}", onuId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.service.OnuPerfService#startOnuCatvQuality(java.lang.Long, java.lang.Long, java.lang.Long, com.topvision.framework.snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public synchronized void startOnuCatvQuality(Long entityId, Long onuId, Long onuIndex, SnmpParam snmpParam, Long typeId) {
        // ONU Catv For PN8626
        Long onuTypeId = entityService.getEntity(onuId).getTypeId();
        if (onuTypeId != 38) {
            return;
        }
        try {
            if (this.hasOnuPerfMonitor(onuId, "onuCatvPerf")) {
                return;
            } else if (devicePerfTargetService.isTargetDataExists(onuId, PerfTargetConstants.ONU_CATV)) {
                //如果指标数据已经存在,则不用在这里开启性能采集———防止在手动关闭性能采集后刷新设备时自动开启了性能采集
                return;
            }
            DevicePerfTarget target = devicePerfTargetService.getGlobalTargetByType(PerfTargetConstants.ONU_CATV,
                    typeId);
            if (target != null) {
                if (target.getGlobalEnable() == PerfTargetConstants.TARGET_ENABLE_ON) {
                    OnuCatvOrInfoPerf catvOrInfoPerf = new OnuCatvOrInfoPerf();
                    catvOrInfoPerf.setEntityId(entityId);
                    catvOrInfoPerf.setOnuId(onuId);
                    catvOrInfoPerf.setOnuIndex(onuIndex);
                    long period = target.getGlobalInterval() * 60000;
                    try {
                        performanceService.startMonitor(snmpParam, catvOrInfoPerf, period, period,
                                PerformanceConstants.PERFORMANCE_DOMAIN,
                                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                        // 插入对应性能指标数据
                        this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                    } catch (Exception e) {
                        // 采集开启失败的情况下，将性能指标状态置为关闭
                        logger.error(e.getMessage());
                        this.handleSpecialTarget(target, onuId, typeId, PerfTargetConstants.TARGET_ENABLE_OFF);
                    }
                } else {
                    // 全局没有开启的情况下也需要插入
                    // 插入对应性能指标数据
                    this.handleSpecialTarget(target, onuId, typeId, target.getGlobalEnable());
                }
            }
        } catch (Exception e) {
            logger.error("startOnuCatvQuality[{}] failed: {}", onuId, e);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.service.OnuPerfService#stopOnuCatvQuality(java.lang.Long, com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void stopOnuCatvQuality(Long onuId, SnmpParam snmpParam) {
        List<Integer> result = getOnuPerfMonitor(onuId, "onuCatvPerf");
        if (result != null) {
            for (Integer tmp : result) {
                performanceService.stopMonitor(snmpParam, tmp.longValue());
            }
        }
    }
    
    @Override
    public PerfOnuQualityHistory queryMinPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo){
    	return onuPerfDao.queryMinPonRevPower(onuLinkCollectInfo);
    }

    @Override
    public PerfOnuQualityHistory queryMaxPonRevPower(OnuLinkCollectInfo onuLinkCollectInfo){
    	return onuPerfDao.queryMaxPonRevPower(onuLinkCollectInfo);
    }
    
    @Override
    public PerfOnuQualityHistory queryMinCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo){
    	return onuPerfDao.queryMinCATVRevPower(onuLinkCollectInfo);
    }
    
    @Override
    public PerfOnuQualityHistory queryMaxCATVRevPower(OnuLinkCollectInfo onuLinkCollectInfo){
    	return onuPerfDao.queryMaxCATVRevPower(onuLinkCollectInfo);
    }
    
    @Override
    public void insertOrUpdateMinReceivedPower(PerfOnuQualityHistory perfOnuQualityHistory){
    	onuPerfDao.insertOrUpdateMinReceivedPower(perfOnuQualityHistory);
    }
}
