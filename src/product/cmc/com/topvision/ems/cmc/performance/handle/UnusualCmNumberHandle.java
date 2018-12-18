/***********************************************************************
 * $Id: UnusualCmNumberHandle.java,v1.0 2013-9-7 下午01:20:27 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-7-下午01:20:27
 *
 */
@Service("unusualCmNumberHandle")
public class UnusualCmNumberHandle extends CmcPerformanceHandle {

    public static String CC_UNUSUALCMNUMBER_FLAG = "CC_UNUSUAL_CMNUM";
    public static Integer CC_UNUSUALCMNUMBER_ALERT_ID = -848;
    public static Integer CC_UNUSUALCMNUMBER_EVENT_ID = -849;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmNum result = (CmNum) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_UNUSUALCMNUMBER_ALERT_ID);
        param.setClearEventId(CC_UNUSUALCMNUMBER_EVENT_ID);
        param.setSource(getMac(result.getEntityId()));
        Float cmNum = Float.parseFloat(result.getOtherNum().toString());
        param.setPerfValue(cmNum);
        param.setTargetId(CC_UNUSUALCMNUMBER_FLAG);
        param.setTargetIndex(result.getEntityId());
        param.setMessage("CMTS[" + param.getSource() + "]" + "_" + getString("PerformanceAlert.cmNum", "cmc") + "["
                + getString("PerformanceAlert.cmNumInfo", "cmc") + cmNum.intValue() + "/" + result.getOnlineNum() + "/"
                + result.getOfflineNum() + "/" + result.getAllNum() + "]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_UNUSUALCMNUMBER_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_UNUSUALCMNUMBER_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_UNUSUALCMNUMBER_FLAG;
    }

}
