/***********************************************************************
 * $Id: OltUniOutSpeedHandle.java,v1.0 2013-9-3 下午03:53:31 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.facade.OltPortFlowPerf;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-9-3-下午03:53:31
 * 
 */
@Service("oltUniOutSpeedHandle")
public class OltUniOutSpeedHandle extends PerformanceHandle {
    public static String OLT_UNI_OUT_SPEED_FLAG = "OLT_UNI_OUT_SPEED";
    public static Integer OLT_UNI_OUT_ALERT_ID = -724;
    public static Integer OLT_UNI_OUT_EVENT_ID = -725;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OltPortFlowPerf flowPerf = (OltPortFlowPerf) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_UNI_OUT_ALERT_ID);
        param.setClearEventId(OLT_UNI_OUT_EVENT_ID);
        param.setSource(EponIndex.getUniStringByIndex(flowPerf.getPortIndex()).toString());
        param.setPerfValue(flowPerf.getPortOutSpeedUnitM());
        param.setTargetId(OLT_UNI_OUT_SPEED_FLAG);
        param.setTargetIndex(flowPerf.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.uniOutRate", "epon") + "[" + df.format(flowPerf.getPortOutSpeedUnitM())
                + "Mbps]");
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_UNI_OUT_SPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_UNI_OUT_SPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_UNI_OUT_SPEED_FLAG;
    }

}
