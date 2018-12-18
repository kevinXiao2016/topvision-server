/***********************************************************************
 * $Id: CmcCpuUsedHandle.java,v1.0 2013-9-3 下午01:57:14 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-3-下午01:57:14
 *
 */
@Service("cmcCpuUsedHandle")
public class CmcCpuUsedHandle extends CmcPerformanceHandle {

    public static String CC_CPUUSED_FLAG = "CC_CPUUSED";
    public static Integer CC_CPUUSED_ALERT_ID = -810;
    public static Integer CC_CPUUSED_EVENT_ID = -811;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcServiceQuality result = (CmcServiceQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_CPUUSED_ALERT_ID);
        param.setClearEventId(CC_CPUUSED_EVENT_ID);
        param.setSource(getMac(result.getCmcId()));
        Float cpuUsed = result.getCpuUsed();
        param.setPerfValue(cpuUsed);
        param.setTargetId(CC_CPUUSED_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + param.getSource() + "]" + "_" + getString("PerformanceAlert.cpuUsed", "cmc") + "["
                + df.format(cpuUsed) + "%]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_CPUUSED_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_CPUUSED_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_CPUUSED_FLAG;
    }

}
