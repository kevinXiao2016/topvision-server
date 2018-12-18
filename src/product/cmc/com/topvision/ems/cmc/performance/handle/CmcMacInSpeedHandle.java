/***********************************************************************
 * $Id: CmcMacInSpeedHandle.java,v1.0 2013-9-3 下午03:06:34 $
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
import com.topvision.framework.common.NumberUtils;

/**
 * @author lizongtian
 * @created @2013-9-3-下午03:06:34
 * 
 */
@Service("cmcMacInSpeedHandle")
public class CmcMacInSpeedHandle extends CmcPerformanceHandle {

    public static String CC_MAC_INSPEED_FLAG = "CC_MAC_INSPEED";
    public static Integer CC_MAC_INSPEED_ALERT_ID = -822;
    public static Integer CC_MAC_INSPEED_EVENT_ID = -823;

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
        param.setAlertEventId(CC_MAC_INSPEED_ALERT_ID);
        param.setClearEventId(CC_MAC_INSPEED_EVENT_ID);
        // param.setSource(result.getIfIndex().toString());
        param.setSource("Mac Domain[" + getMac(result.getCmcId()) + "]");
        // Mbps与阈值单位一致
        Float macInSpeed = result.getIfInSpeed() / NumberUtils.Mbps;
        param.setPerfValue(macInSpeed);
        param.setTargetId(CC_MAC_INSPEED_FLAG);
        param.setTargetIndex(result.getIfIndex());
        param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                + getString("PerformanceAlert.macInSpeed", "cmc") + "[" + df.format(macInSpeed) + "Mbps]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_MAC_INSPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_MAC_INSPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_MAC_INSPEED_FLAG;
    }

}
