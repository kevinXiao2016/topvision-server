/***********************************************************************
 * $Id: CmtsUnusualCmNumberHandle.java,v1.0 2013-10-8 下午4:33:41 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author loyal
 * @created @2013-10-8-下午4:33:41
 * 
 */
@Service("cmtsUnusualCmNumberHandle")
public class CmtsUnusualCmNumberHandle extends CmtsPerformanceHandle {
    public static String CMTS_UNUSUALCMNUMBER_FLAG = "CMTS_UNUSUAL_CMNUM";
    public static Integer CMTS_UNUSUALCMNUMBER_ALERT_ID = -916;
    public static Integer CMTS_UNUSUALCMNUMBER_EVENT_ID = -916;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmNum cmNum = (CmNum) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UNUSUALCMNUMBER_ALERT_ID);
        param.setClearEventId(CMTS_UNUSUALCMNUMBER_EVENT_ID);
        param.setSource(getHost(cmNum.getEntityId()));
        param.setPerfValue(cmNum.getOtherNum() + 0F);
        param.setTargetId(CMTS_UNUSUALCMNUMBER_FLAG);
        param.setTargetIndex(cmNum.getEntityId());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.cmNum", "cmc") + "[" + getString("PerformanceAlert.cmNumInfo", "cmc")
                + cmNum.getOtherNum() + "/" + cmNum.getOnlineNum() + "/" + cmNum.getOfflineNum() + "/"
                + cmNum.getAllNum() + "]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UNUSUALCMNUMBER_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UNUSUALCMNUMBER_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UNUSUALCMNUMBER_FLAG;
    }
}
