/***********************************************************************
 * $Id: OltSlotTempHandle.java,v1.0 2013-9-3 上午10:37:32 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author lizongtian
 * @created @2013-9-3-上午10:37:32
 * 
 */
@Service("oltSlotTempHandle")
public class OltSlotTempHandle extends PerformanceHandle {

    public static String OLT_SLOTTEMP_FLAG = "OLT_BOARD_TEMP";
    public static Integer OLT_SLOTTEMP_ALERT_ID = -755;
    public static Integer OLT_SLOTTEMP_EVENT_ID = -756;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OltServiceQualityPerf result = (OltServiceQualityPerf) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_SLOTTEMP_ALERT_ID);
        param.setClearEventId(OLT_SLOTTEMP_EVENT_ID);
        param.setSource("SLOT:" + result.getSlotId().toString());
        Float slotTemp = Float.parseFloat(result.getTopSysBdCurrentTemperature().toString());
        param.setPerfValue(slotTemp);
        param.setTargetId(OLT_SLOTTEMP_FLAG);
        param.setTargetIndex(result.getSlotIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] " + param.getSource() + "_"
                + getString("PerformanceAlert.slotTemp", "epon") + "(" + slotTemp.intValue() + " "
                + getString("PerformanceAlert.tempUnit", "epon") + ")");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_SLOTTEMP_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_SLOTTEMP_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_SLOTTEMP_FLAG;
    }

}
