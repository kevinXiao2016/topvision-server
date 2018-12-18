/***********************************************************************
 * $Id: EponServiceQualityPerfSaver.java,v1.0 2013-8-5 下午02:08:40 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPerf;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPertResult;
import com.topvision.ems.epon.performance.engine.OltPerfDao;
import com.topvision.ems.epon.performance.facade.OltFanPerf;
import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.epon.performance.handle.OltFanSpeedHandle;
import com.topvision.ems.epon.performance.handle.OltSlotCpuUsedHandle;
import com.topvision.ems.epon.performance.handle.OltSlotFlashUsedHandle;
import com.topvision.ems.epon.performance.handle.OltSlotMemUsedHandle;
import com.topvision.ems.epon.performance.handle.OltSlotTempHandle;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.annotation.Engine;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;

/**
 * @author Rod John
 * @created @2013-8-5-下午02:08:40
 * 
 */
@Engine("eponServiceQualityPerfSaver")
public class EponServiceQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<EponServiceQualityPertResult, OperClass> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(EponServiceQualityPertResult result) {
        OltPerfDao perfDao = engineDaoFactory.getEngineDao(OltPerfDao.class);
        Long entityId = result.getEntityId();
        EponServiceQualityPerf perf = (EponServiceQualityPerf) result.getDomain();
        List<PerformanceData> pList = new ArrayList<PerformanceData>();
        if (result.getPerfs() != null) {
            if (perf.getIsCpuPerf()) {
                for (OltServiceQualityPerf qualityPerf : result.getPerfs()) {
                    // 板卡CPU数据发送性能阈值处理中心
                    Long slotId = perfDao.getSlotNoByIndex(qualityPerf.getSlotIndex(), entityId);
                    qualityPerf.setSlotId(slotId);
                    PerformanceData slotCpuData = new PerformanceData(entityId, OltSlotCpuUsedHandle.OLT_CPUUSED_FLAG,
                            qualityPerf);
                    pList.add(slotCpuData);
                }
                perfDao.insertEponServiceQuality(entityId, PerfTargetConstants.OLT_CPUUSED, result);
            }
            if (perf.getIsMemPerf()) {
                for (OltServiceQualityPerf qualityPerf : result.getPerfs()) {
                    Long slotId = perfDao.getSlotNoByIndex(qualityPerf.getSlotIndex(), entityId);
                    qualityPerf.setSlotId(slotId);
                    // 板卡MEM数据发送性能阈值处理中心
                    if (qualityPerf.getTopSysBdlMemSize() == 0) {
                        qualityPerf.setMemUsed(0F);
                    } else {
                        qualityPerf
                                .setMemUsed(100F
                                        * (qualityPerf.getTopSysBdlMemSize() - qualityPerf.getTopSysBdFreeMemSize() / 1024 / 1024)
                                        / qualityPerf.getTopSysBdlMemSize());
                    }
                    PerformanceData slotMemData = new PerformanceData(entityId, OltSlotMemUsedHandle.OLT_MEMUSED_FLAG,
                            qualityPerf);
                    pList.add(slotMemData);
                    // redirctPerformanceData(qualityPerf, result, entityId,
                    // qualityPerf.getSlotIndex());
                }

                perfDao.insertEponServiceQuality(entityId, PerfTargetConstants.OLT_MEMUSED, result);
            }
            if (perf.getIsFlashPerf()) {
                for (OltServiceQualityPerf qualityPerf : result.getPerfs()) {
                    Long slotId = perfDao.getSlotNoByIndex(qualityPerf.getSlotIndex(), entityId);
                    qualityPerf.setSlotId(slotId);
                    // 板卡FLASH数据发送性能阈值处理中心
                    if (qualityPerf.getTopSysBdTotalFlashOctets() == 0) {
                        qualityPerf.setFlashUsed(0F);
                    } else {
                        qualityPerf
                                .setFlashUsed(100F
                                        * (qualityPerf.getTopSysBdTotalFlashOctets() - qualityPerf
                                                .getTopSysBdFreeFlashOctets())
                                        / qualityPerf.getTopSysBdTotalFlashOctets());
                    }
                    PerformanceData slotFlashUsedData = new PerformanceData(entityId,
                            OltSlotFlashUsedHandle.OLT_FLASHUSED_FLAG, qualityPerf);
                    pList.add(slotFlashUsedData);
                    // redirctPerformanceData(qualityPerf, result, entityId,
                    // qualityPerf.getSlotIndex());
                }
                perfDao.insertEponServiceQuality(entityId, PerfTargetConstants.OLT_FLASHUSED, result);
            }
            if (perf.getIsBoardTemp()) {
                for (OltServiceQualityPerf qualityPerf : result.getPerfs()) {
                    Long slotId = perfDao.getSlotNoByIndex(qualityPerf.getSlotIndex(), entityId);
                    qualityPerf.setSlotId(slotId);
                    // 板卡温度数据发送性能阈值处理中心
                    if (EponUtil.isValidBoardTemp(qualityPerf.getTopSysBdCurrentTemperature())) {
                        PerformanceData slotTempData = new PerformanceData(entityId,
                                OltSlotTempHandle.OLT_SLOTTEMP_FLAG, qualityPerf);
                        pList.add(slotTempData);
                    }
                    // redirctPerformanceData(qualityPerf, result, entityId,
                    // qualityPerf.getSlotIndex());
                }
                perfDao.insertEponServiceQuality(entityId, PerfTargetConstants.OLT_BOARDTEMP, result);
            }
            if (perf.getIsFanSpeed()) {
                for (OltFanPerf qualityPerf : result.getFanSpeedPerfs()) {
                    // 风扇转速数据发送性能阈值处理中心
                    PerformanceData fanSpeedData = new PerformanceData(entityId, OltFanSpeedHandle.OLT_FANSPEED_FLAG,
                            qualityPerf);
                    pList.add(fanSpeedData);
                    redirctPerformanceData(qualityPerf, result, entityId, qualityPerf.getFanIndex());
                }
                perfDao.insertEponServiceQuality(entityId, PerfTargetConstants.OLT_FANSPEED, result);
            }
            for (OltServiceQualityPerf qualityPerf : result.getPerfs()) {
                try {
                    //处理北向接口推送失败的日志记录
                    redirctPerformanceData(qualityPerf, result, entityId, qualityPerf.getSlotIndex());
                } catch (Exception e) {
                    logger.info("OltServiceQualityPerf redirctPerformanceData info : " + qualityPerf);
                    logger.info("OltServiceQualityPerf redirctPerformanceData error", e);
                }
            }
            synchronizeStatusData(entityId, result.getPerfs(), perfDao);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        }
    }

    private void synchronizeStatusData(Long entityId, List<OltServiceQualityPerf> oltServiceQualityPerfs,
            OltPerfDao perfDao) {
        try {
            perfDao.updateSlotStatusWithServiceQuality(oltServiceQualityPerfs);
            for (OltServiceQualityPerf oltServiceQuality : oltServiceQualityPerfs) {
                if (oltServiceQuality.getSlotIndex() == 0) {
                    EntityValueEvent event = new EntityValueEvent(entityId);
                    event.setEntityId(entityId);
                    event.setActionName("performanceChanged");
                    event.setListener(EntityValueListener.class);
                    try {
                        if (oltServiceQuality.getTopSysBdCPUUseRatio() != null) {
                            event.setCpu(oltServiceQuality.getTopSysBdCPUUseRatio().doubleValue() / 100);
                        }
                        if (oltServiceQuality.getMemUsed() != null) {
                            event.setMem(oltServiceQuality.getMemUsed().doubleValue() / 100);
                        }
                        if (oltServiceQuality.getFlashUsed() != null) {
                            event.setDisk(oltServiceQuality.getFlashUsed().doubleValue() / 100);
                        }
                        getCallback(PerformanceCallback.class).addServerMessage(event);
                        break;
                    } catch (Exception exp) {
                        logger.info("synchronizeStatusData error, oltServiceQuality info :", oltServiceQuality);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("sendEponSnapMessage error:", e);
        }
    }
}
