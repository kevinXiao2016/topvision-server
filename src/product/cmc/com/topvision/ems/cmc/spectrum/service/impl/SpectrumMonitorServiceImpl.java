/***********************************************************************
 * $ MonitorServiceImpl.java,v1.0 2014-1-9 15:03:39 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;

import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumIII;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.facade.SpectrumFacade;
import com.topvision.ems.cmc.spectrum.facade.domain.Spectrum;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumII;
import com.topvision.ems.cmc.spectrum.job.HisVideoJob;
import com.topvision.ems.cmc.spectrum.service.SpectrumAlertService;
import com.topvision.ems.cmc.spectrum.service.SpectrumMonitorService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.message.MonitorEvent;
import com.topvision.ems.performance.message.MonitorListener;
import com.topvision.ems.performance.service.MonitorService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.domain.PhysAddress;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author jay
 * @created @2014-1-9-15:03:39
 */
@Service("spectrumMonitorService")
public class SpectrumMonitorServiceImpl extends CmcBaseCommonService
        implements SpectrumMonitorService {

    //压缩频谱版本
    private static final String SPECTRUM_COMPRESS = "spectrumII";
    //UDP频谱版本
    private static final String SPECTRUM_OPTIMIZE = "spectrumOptimize";

    @Resource(name = "cmcPerfDao")
    private CmcPerfDao cmcPerfDao;
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private PerformanceDao performanceDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private CmcService cmcService;
    @Autowired
    private MonitorDao monitorDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    SpectrumAlertService spectrumAlertService;
    private final String HisVideoMonitorType = "HisVideoMonitorType";

    @Override
    public synchronized Boolean startSpectrumMonitor(Long cmcId, Integer period) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
        try {
            if (hasSpectrumMonitor(cmcId)) {
                Long monitorId = cmcPerfDao.getSpectrumMonitorId(cmcId);
                performanceService.stopMonitor(snmpParam, monitorId);
            }
        } catch (Exception e) {
            logger.error("StopMonitor BEFORe StartSpectrumMonitor error ", e);
            return false;
        }
        String oltVersion = cmc.getOltVersion();
        String versionName;
        String subVersionName = null;
        boolean hasSubVersion = false;
        if (oltVersion == null || "".equals(oltVersion)) {
            versionName = cmc.getDolVersion();
        } else {
            hasSubVersion = true;
            versionName = oltVersion;
            subVersionName = cmc.getDolVersion();
        }
        boolean spectrumII = DeviceFuctionSupport.isSupportFunction(cmc.getCmcDeviceStyle(), versionName, SPECTRUM_COMPRESS);
        boolean spectrumIII = DeviceFuctionSupport.isSupportFunction(cmc.getCmcDeviceStyle(), versionName, SPECTRUM_OPTIMIZE);

        boolean spectrumIIISub = false;
        if (hasSubVersion) {
            spectrumIIISub = DeviceFuctionSupport.isSupportFunction(cmc.getCmcDeviceStyle(), subVersionName, SPECTRUM_OPTIMIZE);
        }
        if (!spectrumII && !spectrumIII) {
            try {
                // 开启非压缩频谱版本的设备采集
                SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
                SpectrumCfg spectrumCfg = new SpectrumCfg();
                spectrumCfg
                        .setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmc.getTopCcmtsSysMacAddr())));
                period = Math.max(5000, period);// 非压缩频谱最小只支持5秒的采集步长，这里设置2秒或1秒，SNMP可能会BAD
                spectrumCfg.setFftMonitorTimeInterval(period / 1000); // peroid以毫秒为单位，下发设备需要改为以秒为单位
                spectrumCfg.setFftMonitorPollingStatus(SpectrumCfg.START);
                spectrumCfg.setFftMonitorFreqInterval(SpectrumCfg.FREQINTERVAL);
                cmcSpectrumFacade.spectrumCfg(snmpParam, spectrumCfg);
                // 开启网管侧采集器
                Spectrum cmcSpectsum = new Spectrum();
                cmcSpectsum.setCmcId(cmcId);
                cmcSpectsum.setEntityId(snmpParam.getEntityId());
                cmcSpectsum.setMacAddress(cmc.getTopCcmtsSysMacAddr());
                performanceService.startMonitor(snmpParam, cmcSpectsum, 0L, Long.valueOf(period),
                        PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_NO_STARTWITHSERVER);
                logger.info("Spectrum startSpectrumMonitor, not compressed spectrum monitor started!");
                spectrumAlertService.addCmc(cmcId);
                return true;
            } catch (Exception e) {
                logger.error("StartSpectrumMonitor  NOT compressed failed ", e);
            }
        } else if ((spectrumII && !spectrumIII) || (spectrumII && spectrumIII && hasSubVersion && !spectrumIIISub)){
            //modify by jay 设备snmp阻塞会导致一直没有开启成功监视器，heartbeat也发送不到网管，导致心跳超时，通过起一个线程来避免这个问题
            new Thread() {
                @Override
                public void run() {
                    try {
                        // 关闭非压缩频谱版本的设备采集
                        SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
                        SpectrumCfg spectrumCfg = new SpectrumCfg();
                        spectrumCfg
                                .setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmc.getTopCcmtsSysMacAddr())));
                        spectrumCfg.setFftMonitorPollingStatus(SpectrumCfg.STOP);
                        cmcSpectrumFacade.spectrumCfg(snmpParam, spectrumCfg);
                        logger.info("Closed NOT compressed Spectrum BEFORE starting compressed spectrum success");
                    } catch (Exception e) {
                        logger.error("Closed NOT compressed Spectrum BEFORE starting compressed spectrum success failed", e);
                    }
                }
            }.start();

            try {
                // 开启网管侧采集器
                SpectrumII cmcSpectsum = new SpectrumII();
                cmcSpectsum.setCmcId(cmcId);
                cmcSpectsum.setEntityId(snmpParam.getEntityId());
                cmcSpectsum.setMacAddress(cmc.getTopCcmtsSysMacAddr());
                
                //modify by jay不支持udp频谱 所有采集频率小于1秒的都降级为1s频谱
                if(period < 1000) {
                    period = 1000;
                }
                
                performanceService.startMonitor(snmpParam, cmcSpectsum, 0L, Long.valueOf(period),
                        PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_NO_STARTWITHSERVER);
                logger.info("Spectrum startSpectrumMonitor, compressed spectrum monitor started!");
                spectrumAlertService.addCmc(cmcId);
            } catch (Exception e) {
                logger.error("", e);
                return false;
            }
            spectrumAlertService.addCmc(cmcId);
            logger.debug("Spectrum startSpectrumMonitor, spectrumAlertService init done!");
            return true;
        } else if (spectrumIII){
            //modify by jay 设备snmp阻塞会导致一直没有开启成功监视器，heartbeat也发送不到网管，导致心跳超时，通过起一个线程来避免这个问题
            new Thread() {
                @Override
                public void run() {
                    try {
                        // 关闭非压缩频谱版本的设备采集
                        SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
                        SpectrumCfg spectrumCfg = new SpectrumCfg();
                        spectrumCfg
                                .setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmc.getTopCcmtsSysMacAddr())));
                        spectrumCfg.setFftMonitorPollingStatus(SpectrumCfg.STOP);
                        cmcSpectrumFacade.spectrumCfg(snmpParam, spectrumCfg);
                        logger.info("Closed NOT compressed Spectrum BEFORE starting compressed spectrum success");
                    } catch (Exception e) {
                        logger.error("Closed NOT compressed Spectrum BEFORE starting compressed spectrum success failed", e);
                    }
                }
            }.start();
            try {
                // 开启网管侧采集器
                SpectrumIII cmcSpectsum = new SpectrumIII();
                cmcSpectsum.setCmcId(cmcId);
                cmcSpectsum.setEntityId(snmpParam.getEntityId());
                cmcSpectsum.setCmcIndex(cmc.getCmcIndex());

                performanceService.startMonitor(snmpParam, cmcSpectsum, 0L, Long.valueOf(period),
                        PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                logger.info("Spectrum startSpectrumMonitor, compressed spectrum monitor started!");
                spectrumAlertService.addCmc(cmcId);
            } catch (Exception e) {
                logger.error("", e);
                return false;
            }
            spectrumAlertService.addCmc(cmcId);
            logger.debug("Spectrum startSpectrumMonitor, spectrumAlertService init done!");
            return true;
        }
        return false;
    }

    @Override
    public void stopSpectrumMonitor(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        // 关闭设备采集
        try {
            SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
            CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
            SpectrumCfg spectrumCfg = new SpectrumCfg();
            spectrumCfg.setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmc.getTopCcmtsSysMacAddr())));
            spectrumCfg.setFftMonitorPollingStatus(SpectrumCfg.STOP);
            cmcSpectrumFacade.spectrumCfg(snmpParam, spectrumCfg);
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            if (hasSpectrumMonitor(cmcId)) {
                Long monitorId = cmcPerfDao.getSpectrumMonitorId(cmcId);
                performanceService.stopMonitor(snmpParam, monitorId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    // Add by Victor@20160823
    @Override
    public void stopSpectrumMonitor(Long cmcId, Long monitorId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        // 关闭设备采集
        try {
            SpectrumFacade cmcSpectrumFacade = getSpectrumFacade(snmpParam.getIpAddress());
            CmcAttribute cmc = cmcService.getCmcAttributeByCmcId(cmcId);
            SpectrumCfg spectrumCfg = new SpectrumCfg();
            spectrumCfg.setFftMacIndex(new PhysAddress(MacUtils.convertToMaohaoFormat(cmc.getTopCcmtsSysMacAddr())));
            spectrumCfg.setFftMonitorPollingStatus(SpectrumCfg.STOP);
            cmcSpectrumFacade.spectrumCfg(snmpParam, spectrumCfg);
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            // 如果数据库有则采用performanceService的停止monitor
            if (hasSpectrumMonitor(cmcId)) {
                performanceService.stopMonitor(snmpParam, monitorId);
            } else {
                // TODO 只需要停止后台采集
                logger.error("stopSpectrumMonitor({},{})", cmcId, monitorId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void stopAllSpectrumMonitor() {
        List<ScheduleMessage> monitors = cmcPerfDao.getAllSpectrumMonitorId();
        for (ScheduleMessage monitor : monitors) {
            try {
                performanceService.stopMonitor(monitor.getSnmpParam(), monitor.getMonitorId());
            } catch (Exception e) {
                performanceDao.deleteByPrimaryKey(monitor.getMonitorId());
            }
        }
    }

    @Override
    public boolean hasSpectrumMonitor(Long cmcId) {
        Long monitorId = cmcPerfDao.getMonitorId(cmcId, "CC_CCSPECTRUM");
        return monitorId != null && monitorId > 0;
    }

    /*** ------------------------------------------------------- ****/
    /*** ---------------以上为频谱采集的monitor------------------- ****/
    /*** ---------------以下为历史频谱调度monitor----------------- ****/
    /*** ------------------------------------------------------- ****/

    /**
     * 系统启动时，启动历史频谱录像
     */
    @Override
    public void initialize() {
        // 停止掉上次可能因为网管服务异常关闭遗留的所有的频谱采集
        this.stopAllSpectrumMonitor();
    }

    /**
     * 添加Monitor
     * 
     * @param cmcId
     *            CMC_ID 在执行JOB时需要使用
     * @param initInterval
     *            起始时间间隔
     * @param intervalOfNormal
     *            时间间隔
     * 
     */
    @Override
    public Boolean addMonitor(Long cmcId, Long initInterval, Long intervalOfNormal) {
        logger.debug("Spectrum SpectrumMonitorServiceImpl, cmcId = " + cmcId + " initInterval = " + initInterval
                + " intervalOfNormal = " + intervalOfNormal);
        try {
            if (!this.hasHisVideoMonitor(cmcId)) {
                Monitor monitor = new Monitor();
                monitor.setCategory(this.HisVideoMonitorType);
                monitor.setJobClass(HisVideoJob.class.getName());
                monitor.setIntervalStart(initInterval); // 延迟开启Monitor字段
                monitor.setIntervalOfNormal(intervalOfNormal);
                monitor.setIntervalAfterError(intervalOfNormal);
                monitor.setEntityId(cmcId); // cmcId
                monitor.setIp(String.valueOf(cmcId));// 此IP无实际意义，仅为了正确生成JOB
                monitor.setName(String.valueOf(cmcId));
                monitor.setContent(String.valueOf(cmcId));
                monitorDao.insertEntity(monitor);
                monitorService.addMonitor(monitor);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Spectrum Create HisVideoMonitor successful. The intervalOfNormal is " + intervalOfNormal);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除历史频谱录像的Monitor
     */
    @Override
    public Boolean removeMonitor(Long cmcId) {
        try {
            Monitor monitor = this.getMonitorByCmcId(cmcId);
            if (monitor != null) {
                monitorService.stopMonitor(monitor);
                // 从数据库中删除
                List<Long> ids = new ArrayList<>(1);
                ids.add(monitor.getMonitorId());
                monitorDao.deleteByPrimaryKey(ids);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新Monitor，主要是更新轮询间隔字段
     *
     * @param intervalOfNormal
     *            更新后的轮询间隔
     */
    @Override
    public void updateAllHisMonitor(Long intervalOfNormal) {
        List<Monitor> monitors = monitorDao.getMonitorByCategory(this.HisVideoMonitorType);
        for (Monitor monitor : monitors) {
            monitor.setIntervalOfNormal(intervalOfNormal);
            monitor.setIntervalAfterError(intervalOfNormal);
            MonitorEvent evt = new MonitorEvent(monitor);
            evt.setMonitor(monitor);
            evt.setActionName("monitorChanged");
            evt.setListener(MonitorListener.class);
            messageService.addMessage(evt);
            if (logger.isDebugEnabled()) {
                logger.debug("Update HisVideoMonitor successful. The monitor Id = " + monitor.getMonitorId()
                        + "The intervalOfNormal is " + intervalOfNormal);
            }
        }
    }

    private Monitor getMonitorByCmcId(Long cmcId) {
        Map<String, String> map = new HashMap<>();
        map.put("entityId", String.valueOf(cmcId));
        map.put("category", this.HisVideoMonitorType);
        return monitorService.getMonitor(map);
    }

    private boolean hasHisVideoMonitor(Long cmcId) {
        return this.getMonitorByCmcId(cmcId) != null;
    }

    @Override
    public SpectrumFacade getSpectrumFacade(String ip) {
        return facadeFactory.getFacade(ip, SpectrumFacade.class);
    }
}
