/***********************************************************************
 * $Id: CmcServiceQualityHandle.java,v1.0 2015年6月23日 下午8:34:26 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author Victor
 * @created @2015年6月23日-下午8:34:26
 *
 */
@Service("cmcServiceQualityHandle")
public class CmcServiceQualityHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CMC_SERVICE_QUALITY";
    @Autowired
    private CmcPerfDao cmcPerfDao;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(HANDLE_ID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(HANDLE_ID);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return HANDLE_ID;
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    @Override
    public void handle(PerformanceData data) {
        CmcServiceQualityPerfResult result = (CmcServiceQualityPerfResult) data.getPerfData();
        CmcServiceQuality serviceQuality = result.getPerf();
        long cmcId = serviceQuality.getCmcId();
        Integer cmcStatus = serviceQuality.getTopCcmtsSysStatus();
        try {
            if (cmcStatus != null) {
                //能够采集到数据,则更新CMC设备system信息
                cmcPerfDao.updateCmcSysInfo(serviceQuality);
                //CC不在线的情况下需要将下挂的所有cm状态修改为offline
                if (!CmcConstants.TOPCCMTSSYSSTATUS_ONLINE.equals(serviceQuality.getTopCcmtsSysStatus())) {
                    cmcPerfDao.changeCmStatusOffine(cmcId);
                }
            } else {
                //获取不到设备信息,默认设备下线,更新设备状态，并将下挂的所有cm状态修改为offline
                //TODO 2016.01.13 暂时不处理
                //cmcPerfDao.updateCmcStatus(cmcId, CmcConstants.TOPCCMTSSYSSTATUS_OFFLINE);
                //cmcPerfDao.changeCmStatusOffine(cmcId);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
