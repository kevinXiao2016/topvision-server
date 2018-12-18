/***********************************************************************
 * $Id: CmcMemUsedHandle.java,v1.0 2013-9-3 下午02:25:25 $
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
 * @created @2013-9-3-下午02:25:25
 *
 */
@Service("cmcMemUsedHandle")
public class CmcMemUsedHandle extends CmcPerformanceHandle {

    public static String CC_MEMUSED_FLAG = "CC_MEMUSED";
    public static Integer CC_MEMUSED_ALERT_ID = -812;
    public static Integer CC_MEMUSED_EVENT_ID = -813;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcServiceQuality result = (CmcServiceQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_MEMUSED_ALERT_ID);
        param.setClearEventId(CC_MEMUSED_EVENT_ID);
        param.setSource(getMac(result.getCmcId()));
        Float memUsed = result.getMemUsed();
        param.setPerfValue(memUsed);
        param.setTargetId(CC_MEMUSED_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + param.getSource() + "]" + "_" + getString("PerformanceAlert.memUsed", "cmc") + "["
                + df.format(memUsed) + "%]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_MEMUSED_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_MEMUSED_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_MEMUSED_FLAG;
    }

}
