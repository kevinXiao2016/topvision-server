/***********************************************************************
 * $Id: CmcPortVotageHandle.java,v1.0 2013-9-3 下午03:52:48 $
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
 * @created @2013-9-3-下午03:52:48
 * 
 */
@Service("cmcPortVoltageHandle")
public class CmcPortVoltageHandle extends CmcPerformanceHandle {

    public static String CC_PON_VOLTAGE_FLAG = "CC_PON_VOLTAGE";
    public static Integer CC_PON_VOLTAGE_ALERT_ID = -836;
    public static Integer CC_PON_VOLTAGE_EVENT_ID = -837;

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
        param.setAlertEventId(CC_PON_VOLTAGE_ALERT_ID);
        param.setClearEventId(CC_PON_VOLTAGE_EVENT_ID);
        // param.setSource(result.getPortIndex().toString());
        param.setSource(getMac(result.getCmcId()));
        Float optVoltageFloat = result.getOptVoltageFloat();
        param.setPerfValue(optVoltageFloat);
        param.setTargetId(CC_PON_VOLTAGE_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                + getString("PerformanceAlert.optVoltage", "cmc") + "[" + df.format(optVoltageFloat) + "V]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_PON_VOLTAGE_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_PON_VOLTAGE_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_PON_VOLTAGE_FLAG;
    }

}
