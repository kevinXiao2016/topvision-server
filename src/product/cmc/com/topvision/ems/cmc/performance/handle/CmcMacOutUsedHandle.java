/***********************************************************************
 * $Id: CmcMacUsedHandle.java,v1.0 2013-9-3 下午03:14:25 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-3-下午03:14:25
 * 
 */
@Service("cmcMacOutUsedHandle")
public class CmcMacOutUsedHandle extends CmcPerformanceHandle {
    public static String CC_MAC_OUT_USED_FLAG = "CC_MAC_OUT_USED";
    public static Integer CC_MAC_OUT_USED_ALERT_ID = -852;
    public static Integer CC_MAC_OUT_USED_EVENT_ID = -853;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcFlowQuality result = (CmcFlowQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_MAC_OUT_USED_ALERT_ID);
        param.setClearEventId(CC_MAC_OUT_USED_EVENT_ID);
        // param.setSource(result.getIfIndex().toString());
        param.setSource("Mac Domain[" + getMac(result.getCmcId()) + "]");
        if (result.getIfOutUsed() >= 100) {
            return null;
        }
        Float macUsed = result.getIfOutUsed();
        param.setPerfValue(macUsed);
        param.setTargetId(CC_MAC_OUT_USED_FLAG);
        param.setTargetIndex(result.getIfIndex());
        param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_" + getString("PerformanceAlert.macUsed", "cmc")
                + "[" + df.format(macUsed) + "%]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_MAC_OUT_USED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_MAC_OUT_USED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_MAC_OUT_USED_FLAG;
    }

}
