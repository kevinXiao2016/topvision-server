/***********************************************************************
 * $Id: CmcRfModuleHandle.java,v1.0 2013-9-3 下午04:14:17 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-3-下午04:14:17
 *
 */
@Service("cmcDsModuleTempHandle")
public class CmcDsModuleTempHandle extends CmcPerformanceHandle {

    public static String CC_DS_MODULE_TEMP_FLAG = "CC_DS_MODULE_TEMP";
    public static Integer CC_DS_MODULE_TEMP_ALERT_ID = -840;
    public static Integer CC_DS_MODULE_TEMP_EVENT_ID = -841;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcTempQualityFor8800B result = (CmcTempQualityFor8800B) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_DS_MODULE_TEMP_ALERT_ID);
        param.setClearEventId(CC_DS_MODULE_TEMP_EVENT_ID);
        param.setSource(getMac(result.getCmcId()));
        Float dsTemp = Float.parseFloat(result.getDsModuleTemp().toString());
        param.setPerfValue(dsTemp);
        param.setTargetId(CC_DS_MODULE_TEMP_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + param.getSource() + "]" + getString("PerformanceAlert.dsTemp", "cmc") + "("
                + dsTemp.intValue() + " " + getString("PerformanceAlert.tempUnit", "cmc") + ")");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_DS_MODULE_TEMP_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_DS_MODULE_TEMP_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_DS_MODULE_TEMP_FLAG;
    }

}
