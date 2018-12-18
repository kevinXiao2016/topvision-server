/***********************************************************************
 * $Id: OltPortOptCurrentHandle.java,v1.0 2013-9-3 下午02:02:21 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.domain.EponLinkQualityData;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-9-3-下午02:02:21
 * 
 */
@Service("oltPortOptCurrentHandle")
public class OltPortOptCurrentHandle extends PerformanceHandle {
    public static String OLT_OPTCURRENT_FLAG = "OLT_PORT_CURRENT";
    public static Integer OLT_OPTCURRENT_ALERT_ID = -730;
    public static Integer OLT_OPTCURRENT_EVENT_ID = -731;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        EponLinkQualityData result = (EponLinkQualityData) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_OPTCURRENT_ALERT_ID);
        param.setClearEventId(OLT_OPTCURRENT_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(result.getPortIndex()).toString());
        Float current = result.getBiasCurrent();
        param.setPerfValue(current);
        param.setTargetId(OLT_OPTCURRENT_FLAG);
        param.setTargetIndex(result.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.optCurrent", "epon") + "[" + df.format(current) + "mA]");
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_OPTCURRENT_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_OPTCURRENT_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_OPTCURRENT_FLAG;
    }

}
