/***********************************************************************
 * $Id: OltFanSpeedHandle.java,v1.0 2013-9-3 上午10:47:06 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.facade.OltFanPerf;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author lizongtian
 * @created @2013-9-3-上午10:47:06
 * 
 */
@Service("oltFanSpeedHandle")
public class OltFanSpeedHandle extends PerformanceHandle {
    public static String OLT_FANSPEED_FLAG = "OLT_FAN_SPEED";
    public static Integer OLT_FANSPEED_ALERT_ID = -761;
    public static Integer OLT_FANSPEED_EVENT_ID = -762;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        try {
            OltFanPerf result = (OltFanPerf) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(OLT_FANSPEED_ALERT_ID);
            param.setClearEventId(OLT_FANSPEED_EVENT_ID);
            param.setSource(EponIndex.getSlotNo(result.getFanIndex()).toString());
            Float fanSpeed = Float.parseFloat(result.getFanSpeed().toString());
            param.setPerfValue(fanSpeed);
            param.setTargetId(OLT_FANSPEED_FLAG);
            param.setTargetIndex(result.getFanIndex());
            param.setMessage("OLT[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.fanSpeed", "epon") + "[" + fanSpeed.intValue() + "rps]");
            return param;
        } catch (Exception e) {
            logger.info("OltFanSpeedHandle error ", e);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_FANSPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_FANSPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_FANSPEED_FLAG;
    }

}
