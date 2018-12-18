/***********************************************************************
 * $Id: OltSniPortSpeedHandle.java,v1.0 2013-6-22 上午10:05:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-6-22-上午10:05:32
 * 
 */
@Service("oltSniPortInSpeedHandle")
public class OltSniPortInSpeedHandle extends PerformanceHandle {
    public static String SNI_INSPPED_FLAG = "OLT_SNI_IN_SPEED";
    public static Integer SNI_INSPPED_ALERT_ID = -710;
    public static Integer SNI_INSPPED_EVENT_ID = -711;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OltFlowQuality flowPerf = (OltFlowQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(SNI_INSPPED_ALERT_ID);
        param.setClearEventId(SNI_INSPPED_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(flowPerf.getPortIndex()).toString());
        param.setPerfValue(flowPerf.getIfInSpeed() / 1000 / 1000);
        param.setTargetId(SNI_INSPPED_FLAG);
        param.setTargetIndex(flowPerf.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.sniInRate", "epon") + "["
                + df.format(flowPerf.getIfInSpeed() / 1000 / 1000) + "Mbps]");
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
        performanceStatisticsCenter.registerPerformanceHandler(SNI_INSPPED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(SNI_INSPPED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return SNI_INSPPED_FLAG;
    }

}
