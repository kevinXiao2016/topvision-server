/***********************************************************************
 * $Id: CmcPortCurrentHandle.java,v1.0 2013-9-3 下午03:48:45 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-3-下午03:48:45
 * 
 */
@Service("cmcPortCurrentHandle")
public class CmcPortCurrentHandle extends CmcPerformanceHandle {

    public static String CC_PON_CURRENT_FLAG = "CC_PON_CURRENT";
    public static Integer CC_PON_CURRENT_ALERT_ID = -834;
    public static Integer CC_PON_CURRENT_EVENT_ID = -835;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcLinkQualityData result = (CmcLinkQualityData) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_PON_CURRENT_ALERT_ID);
        param.setClearEventId(CC_PON_CURRENT_EVENT_ID);
        // param.setSource(result.getPortIndex().toString());
        param.setSource(getMac(result.getCmcId()));
        Float optCurrentFloat = result.getOptCurrentFloat();
        param.setPerfValue(optCurrentFloat);
        param.setTargetId(CC_PON_CURRENT_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                + getString("PerformanceAlert.optCurrent", "cmc") + "[" + df.format(optCurrentFloat) + "mA]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_PON_CURRENT_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_PON_CURRENT_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_PON_CURRENT_FLAG;
    }

}
