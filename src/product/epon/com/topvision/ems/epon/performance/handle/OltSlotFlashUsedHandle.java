/***********************************************************************
 * $Id: OltSlotFlashUsedHandle.java,v1.0 2013-9-3 上午09:58:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author lizongtian
 * @created @2013-9-3-上午09:58:28
 * 
 */
@Service("oltSlotFlashUsedHandle")
public class OltSlotFlashUsedHandle extends PerformanceHandle {

    public static String OLT_FLASHUSED_FLAG = "OLT_FLASH_USED";
    public static Integer OLT_FLASHUSED_ALERT_ID = -751;
    public static Integer OLT_FLASHUSED_EVENT_ID = -752;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OltServiceQualityPerf result = (OltServiceQualityPerf) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_FLASHUSED_ALERT_ID);
        param.setClearEventId(OLT_FLASHUSED_EVENT_ID);
        param.setSource("SLOT:" + result.getSlotId().toString());
        Float flashUsed = result.getFlashUsed();
        param.setPerfValue(flashUsed);
        param.setTargetId(OLT_FLASHUSED_FLAG);
        param.setTargetIndex(result.getSlotIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] " + param.getSource() + "_"
                + getString("PerformanceAlert.flashUsed", "epon") + "[" + df.format(flashUsed) + "%]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_FLASHUSED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_FLASHUSED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_FLASHUSED_FLAG;
    }

}
