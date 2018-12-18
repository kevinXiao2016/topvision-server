/***********************************************************************
 * $Id: CmcFlashUsedHandle.java,v1.0 2013-9-3 下午02:30:14 $
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
 * @created @2013-9-3-下午02:30:14
 *
 */
@Service("cmcFlashUsedHandle")
public class CmcFlashUsedHandle extends CmcPerformanceHandle {

    public static String CC_FLASHUSED_FLAG = "CC_FLASHUSED";
    public static Integer CC_FLASHUSED_ALERT_ID = -814;
    public static Integer CC_FLASHUSED_EVENT_ID = -815;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcServiceQuality result = (CmcServiceQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_FLASHUSED_ALERT_ID);
        param.setClearEventId(CC_FLASHUSED_EVENT_ID);
        param.setSource(getMac(result.getCmcId()));
        Float flashUsed = Float.parseFloat(result.getTopCcmtsSysFlashRatio().toString());
        param.setPerfValue(flashUsed);
        param.setTargetId(CC_FLASHUSED_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + param.getSource() + "]" + "_" + getString("PerformanceAlert.flashUsed", "cmc") + "["
                + df.format(flashUsed) + "%]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_FLASHUSED_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_FLASHUSED_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_FLASHUSED_FLAG;
    }

}
