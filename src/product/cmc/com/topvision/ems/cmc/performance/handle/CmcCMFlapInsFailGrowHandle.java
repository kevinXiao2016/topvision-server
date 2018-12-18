/***********************************************************************
 * $Id: CmcCMFlapInsFailGrow.java,v1.0 2014-1-9 上午11:05:32 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author bryan
 * @created @2014-1-9-上午11:05:32
 *
 */
@Service("cmcCMFlapInsFailGrowHandle")
public class CmcCMFlapInsFailGrowHandle extends CmcPerformanceHandle {

    public static String CC_CMFLAP_INSFAILGROW_FLAG = "CM_FLAP_INS";
    public static Integer CC_CMFLAP_INSFAILGROW_ALERT_ID = -918;
    public static Integer CC_CMFLAP_INSFAILGROW_EVENT_ID = -919;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CMFlapHis result = (CMFlapHis) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_CMFLAP_INSFAILGROW_ALERT_ID);
        param.setClearEventId(CC_CMFLAP_INSFAILGROW_EVENT_ID);
        param.setSource(result.getCmMac());
        Float f = Float.parseFloat(result.getIncreaseInsNum().toString());
        param.setPerfValue(f);
        param.setTargetId(CC_CMFLAP_INSFAILGROW_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + getMac(data.getEntityId()) + "]" + "_" + "CM[" + result.getCmMac() + "]"
        //+result.getIncreaseInsNum()+getString("cmc.flap.counter", "cmc")
                + getString("cmc.flap.failOnlineCounter", "cmc") + "[" + f.intValue() + "]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_CMFLAP_INSFAILGROW_FLAG, this);

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_CMFLAP_INSFAILGROW_FLAG);

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_CMFLAP_INSFAILGROW_FLAG;
    }

}
