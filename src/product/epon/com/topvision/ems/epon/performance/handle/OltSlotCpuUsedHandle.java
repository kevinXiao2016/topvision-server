/***********************************************************************
 * $Id: OltCpuUsedHandle.java,v1.0 2013-9-3 上午08:42:06 $
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
 * @created @2013-9-3-上午08:42:06
 * 
 */
@Service("oltSlotCpuUsedHandle")
public class OltSlotCpuUsedHandle extends PerformanceHandle {

    public static String OLT_CPUUSED_FLAG = "OLT_CPU_USED";
    public static Integer OLT_CPUUSED_ALERT_ID = -757;
    public static Integer OLT_CPUUSED_EVENT_ID = -758;

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
        param.setAlertEventId(OLT_CPUUSED_ALERT_ID);
        param.setClearEventId(OLT_CPUUSED_EVENT_ID);
        param.setSource("SLOT:" + result.getSlotId().toString());
        Integer cpuUsed = result.getTopSysBdCPUUseRatio();
        param.setPerfValue(Float.parseFloat(cpuUsed.toString()));
        param.setTargetId(OLT_CPUUSED_FLAG);
        param.setTargetIndex(result.getSlotIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] " + param.getSource() + "_"
                + getString("PerformanceAlert.cpuUsed", "epon") + "[" + df.format(cpuUsed) + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_CPUUSED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_CPUUSED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_CPUUSED_FLAG;
    }

}
