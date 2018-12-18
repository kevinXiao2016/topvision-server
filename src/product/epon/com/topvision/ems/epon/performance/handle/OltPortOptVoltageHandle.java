/***********************************************************************
 * $Id: OltPortOptVoltageHandle.java,v1.0 2013-9-3 下午02:00:34 $
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
 * @created @2013-9-3-下午02:00:34
 * 
 */
@Service("oltPortOptVoltageHandle")
public class OltPortOptVoltageHandle extends PerformanceHandle {
    public static String OLT_OPTVOL_FLAG = "OLT_PORT_VOLTAGE";
    public static Integer OLT_OPTVOL_ALERT_ID = -738;
    public static Integer OLT_OPTVOL_EVENT_ID = -739;

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
        param.setAlertEventId(OLT_OPTVOL_ALERT_ID);
        param.setClearEventId(OLT_OPTVOL_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(result.getPortIndex()).toString());
        Float optVoltage = result.getOptVoltageUnitV();
        param.setPerfValue(optVoltage);
        param.setTargetId(OLT_OPTVOL_FLAG);
        param.setTargetIndex(result.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.optVoltage", "epon") + "[" + df.format(optVoltage) + "V]");
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_OPTVOL_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_OPTVOL_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return OLT_OPTVOL_FLAG;
    }

}
