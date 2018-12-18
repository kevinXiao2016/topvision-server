/***********************************************************************
 * $Id: CmtsUpLinkUsedHandle.java,v1.0 2013-9-29 下午4:02:06 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.performance.domain.IfUtilization;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author fanzidong
 * @created @2013-9-29-下午4:02:06
 *
 */
@Service("cmtsUpLinkUsedHandle")
public class CmtsUpLinkUsedHandle extends CmtsPerformanceHandle {

    public static String CMTS_UPLINK_UESD_FLAG = "CMTS_UPLINK_UESD";
    public static Integer CMTS_UPLINK_UESD_ALERT_ID = -904;
    public static Integer CMTS_UPLINK_UESD_EVENT_ID = -905;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        IfUtilization ifUtilization = (IfUtilization) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UPLINK_UESD_ALERT_ID);
        param.setClearEventId(CMTS_UPLINK_UESD_EVENT_ID);
        param.setSource(getCmtsUpLinkSourceString(ifUtilization.getEntityId(), ifUtilization.getIfIndex()));
        Double upLinkUsed = ifUtilization.getIfUtilization();
        param.setPerfValue(upLinkUsed.floatValue());
        param.setTargetId(CMTS_UPLINK_UESD_FLAG);
        param.setTargetIndex(ifUtilization.getIfIndex());
        param.setMessage("CMTS[" + getHost(ifUtilization.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.upLinkSpeed", "cmts") + "[" + df.format(upLinkUsed) + "%]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UPLINK_UESD_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UPLINK_UESD_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UPLINK_UESD_FLAG;
    }

}
