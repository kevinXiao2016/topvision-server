/***********************************************************************
 * $Id: CmtsUpLinkInSpeedHandle.java,v1.0 2013-9-29 上午9:41:50 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.performance.domain.UplinkSpeedStatic;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.framework.common.NumberUtils;

/**
 * @author fanzidong
 * @created @2013-9-29-上午9:41:50
 * 
 */
@Service("cmtsUpLinkInSpeedHandle")
public class CmtsUpLinkInSpeedHandle extends CmtsPerformanceHandle {
    public static String CMTS_UPLINK_INSPEED_FLAG = "CMTS_UPLINK_INSPEED";
    public static Integer CMTS_UPLINK_INSPEED_ALERT_ID = -900;
    public static Integer CMTS_UPLINK_INSPEED_EVENT_ID = -901;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        UplinkSpeedStatic upLinkSpeedStatic = (UplinkSpeedStatic) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UPLINK_INSPEED_ALERT_ID);
        param.setClearEventId(CMTS_UPLINK_INSPEED_EVENT_ID);
        param.setSource(getCmtsUpLinkSourceString(upLinkSpeedStatic.getEntityId(), upLinkSpeedStatic.getIfIndex()));
        param.setPerfValue(new Float(NumberUtils.TWODOT_FORMAT.format(upLinkSpeedStatic.getIfInOctetsRate()
                / NumberUtils.Mbps)));
        param.setTargetId(CMTS_UPLINK_INSPEED_FLAG);
        param.setTargetIndex(upLinkSpeedStatic.getIfIndex());
        param.setMessage("CMTS[" + getHost(upLinkSpeedStatic.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.upLinkInSpeed", "cmts") + "["
                + df.format(upLinkSpeedStatic.getIfInOctetsRate()) + "Mbps]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UPLINK_INSPEED_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UPLINK_INSPEED_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UPLINK_INSPEED_FLAG;
    }

}
