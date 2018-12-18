/***********************************************************************
 * $Id: CmcServiceQualityPerfSaver.java,v1.0 2013-8-8 下午03:31:08 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerf;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.handle.CmcCpuUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcFlashUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcMemUsedHandle;
import com.topvision.ems.cmc.performance.handle.CmcServiceQualityHandle;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2013-8-8-下午03:31:08
 * 
 */
@Engine("cmcServiceQualityPerfSaver")
public class CmcServiceQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcServiceQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcServiceQualityPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcServiceQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcServiceQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcPerfDao perfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        CmcServiceQualityPerf perf = (CmcServiceQualityPerf) result.getDomain();
        CmcServiceQuality serviceQuality = result.getPerf();
        logger.info("CmcServiceQuality[{}] collect result: {}", cmcId, serviceQuality);
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        if (perf.getIsCpuPerf()) {
            if (serviceQuality.getTopCcmtsSysCPURatio() != null) {
                perfDao.insertCmcServiceQuality(cmcId, PerfTargetConstants.CMC_CPUUSED, result);
                PerformanceData cmcCpuUsedData = new PerformanceData(cmcId, CmcCpuUsedHandle.CC_CPUUSED_FLAG,
                        result.getPerf());
                perfDataList.add(cmcCpuUsedData);
            }
        }
        if (perf.getIsMemPerf()) {
            if (serviceQuality.getTopCcmtsSysRAMRatio() != null) {
                perfDao.insertCmcServiceQuality(cmcId, PerfTargetConstants.CMC_MEMUSED, result);
                PerformanceData cmcMemUsedData = new PerformanceData(cmcId, CmcMemUsedHandle.CC_MEMUSED_FLAG,
                        result.getPerf());
                perfDataList.add(cmcMemUsedData);
            }
        }
        if (perf.getIsFlashPerf()) {
            if (serviceQuality.getTopCcmtsSysFlashRatio() != null) {
                perfDao.insertCmcServiceQuality(cmcId, PerfTargetConstants.CMC_FLASHUSED, result);
                PerformanceData cmcFlashUsedData = new PerformanceData(cmcId, CmcFlashUsedHandle.CC_FLASHUSED_FLAG,
                        result.getPerf());
                perfDataList.add(cmcFlashUsedData);
            }
        }
        try {
            redirctPerformanceData(serviceQuality, result, cmcId, serviceQuality.getCmcIndex());
        } catch (Exception e) {
            logger.info("CmcServiceQuality redirctPerformanceData info : " + result);
            logger.info("CmcServiceQuality redirctPerformanceData error", e);
        }

        synchronizeStatusData(perfDao, serviceQuality);
        PerformanceData data = new PerformanceData(result.getEntityId(), CmcServiceQualityHandle.HANDLE_ID, result);
        perfDataList.add(data);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
    }

    private void synchronizeStatusData(CmcPerfDao perfDao, CmcServiceQuality serviceQuality) {
        EntityValueEvent event = new EntityValueEvent(serviceQuality.getCmcId());
        long cmcId = serviceQuality.getCmcId();
        event.setEntityId(cmcId);
        try {
            event.setActionName("performanceChanged");
            event.setListener(EntityValueListener.class);
            //如果设备不连通,没有采集到数据,此处理会抛出NullPointerException异常
            event.setSysUpTime(serviceQuality.getTopCcmtsSysUpTime().toString());
            if (CmcConstants.TOPCCMTSSYSSTATUS_ONLINE.equals(serviceQuality.getTopCcmtsSysStatus())) {
                event.setCpu(serviceQuality.getTopCcmtsSysCPURatio().doubleValue() / 100);
                event.setMem(serviceQuality.getTopCcmtsSysRAMRatio().doubleValue() / 100);
                event.setDisk(serviceQuality.getTopCcmtsSysFlashRatio().doubleValue() / 100);
                event.setSysUpTime(serviceQuality.getTopCcmtsSysUpTime().toString());
                event.setState(true);
                // 为广州老设备提供手动恢复断电告警
                // modify by fanzidong 2017-05-18,已经确认老设备可以正常上报trap
                // getCallback(CmcAlertClearCallback.class).clearCmcAlert(cmcId);
            } else {
                event.setCpu(-1d);
                event.setMem(-1d);
                event.setDisk(-1d);
                event.setSysUpTime("-1");
                event.setState(false);
                //不能直接在engine端更新,需要在server端更新,CmcServiceQualityHandle中统一处理
                //perfDao.changeCmStatusOffine(serviceQuality.getCmcId());
            }
            // 将cmc系统信息保存到数据库中
            //不能直接在engine端更新,需要在server端更新,CmcServiceQualityHandle中统一处理
            //perfDao.updateCmcStatus(serviceQuality);
            getCallback(PerformanceCallback.class).addServerMessage(event);
        } catch (NullPointerException e) {
            // 针对8800B设备不连通情况处理 TODO 2016.01.13暂不处理
            //event.setState(false);
            //不能直接在engine端更新,需要在server端更新,CmcServiceQualityHandle中统一处理
            //perfDao.changeCmStatusOffine(serviceQuality.getCmcId());
            //getCallback(PerformanceCallback.class).addServerMessage(event);
            logger.info("cmc synchronizeStatusData CmcServiceQuality info nullpoint error :" + serviceQuality);
            logger.debug("cmc synchronizeStatusData nullpoint error", e);
        } catch (Exception e) {
            logger.info("cmc synchronizeStatusData CmcServiceQuality info :" + serviceQuality);
            logger.error("cmc synchronizeStatusData error", e);
        }
    }
}
