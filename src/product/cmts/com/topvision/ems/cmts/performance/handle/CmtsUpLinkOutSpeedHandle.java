/***********************************************************************
 * $Id: CmtsUpLinkOutSpeedHandle.java,v1.0 2013-9-29 下午3:08:18 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.performance.domain.UplinkSpeedStatic;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.framework.common.NumberUtils;

/**
 * @author fanzidong
 * @created @2013-9-29-下午3:08:18
 *
 */
@Service("cmtsUpLinkOutSpeedHandle")
public class CmtsUpLinkOutSpeedHandle extends CmtsPerformanceHandle {

    public static String CMTS_UPLINK_OUTSPEED_FLAG = "CMTS_UPLINK_OUTSPEED";
    public static Integer CMTS_UPLINK_OUTSPEED_ALERT_ID = -902;
    public static Integer CMTS_UPLINK_OUTSPEED_EVENT_ID = -903;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        UplinkSpeedStatic upLinkSpeedStatic = (UplinkSpeedStatic) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UPLINK_OUTSPEED_ALERT_ID);
        param.setClearEventId(CMTS_UPLINK_OUTSPEED_EVENT_ID);
        param.setSource(getCmtsUpLinkSourceString(upLinkSpeedStatic.getEntityId(), upLinkSpeedStatic.getIfIndex()));
        param.setPerfValue(new Float(NumberUtils.TWODOT_FORMAT.format(upLinkSpeedStatic.getIfOutOctetsRate()
                / NumberUtils.Mbps)));
        param.setTargetId(CMTS_UPLINK_OUTSPEED_FLAG);
        param.setTargetIndex(upLinkSpeedStatic.getIfIndex());
        param.setMessage("CMTS[" + getHost(upLinkSpeedStatic.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.upLinkOutSpeed", "cmts") + "["
                + df.format(upLinkSpeedStatic.getIfOutOctetsRate()) + "Mbps]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UPLINK_OUTSPEED_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UPLINK_OUTSPEED_FLAG);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UPLINK_OUTSPEED_FLAG;
    }

}
