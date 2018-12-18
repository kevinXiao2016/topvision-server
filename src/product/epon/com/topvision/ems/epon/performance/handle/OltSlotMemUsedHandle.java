/***********************************************************************
 * $Id: OltMemUsedHandle.java,v1.0 2013-9-3 上午09:29:51 $
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
 * @created @2013-9-3-上午09:29:51
 * 
 */
@Service("oltSlotMemUsedHandle")
public class OltSlotMemUsedHandle extends PerformanceHandle {

    public static String OLT_MEMUSED_FLAG = "OLT_MEM_USED";
    public static Integer OLT_MEMUSED_ALERT_ID = -753;
    public static Integer OLT_MEMUSED_EVENT_ID = -754;

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
        param.setAlertEventId(OLT_MEMUSED_ALERT_ID);
        param.setClearEventId(OLT_MEMUSED_EVENT_ID);
        param.setSource("SLOT:" + result.getSlotId().toString());
        Float memUsed = result.getMemUsed();
        param.setPerfValue(memUsed);
        param.setTargetId(OLT_MEMUSED_FLAG);
        param.setTargetIndex(result.getSlotIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] " + param.getSource() + "_"
                + getString("PerformanceAlert.memUsed", "epon") + "[" + df.format(memUsed) + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_MEMUSED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_MEMUSED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_MEMUSED_FLAG;
    }

}
