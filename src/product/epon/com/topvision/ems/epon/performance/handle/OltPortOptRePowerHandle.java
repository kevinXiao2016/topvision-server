/***********************************************************************
 * $Id: OltPortOptRePowerHandle.java,v1.0 2013-9-3 下午02:02:49 $
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
 * @created @2013-9-3-下午02:02:49
 *
 */
@Service("oltPortOptRePowerHandle")
public class OltPortOptRePowerHandle extends PerformanceHandle {
    public static String OLT_OPTREPOWER_FLAG = "OLT_PORT_RE_POWER";
    public static Integer OLT_OPTREPOWER_ALERT_ID = -732;
    public static Integer OLT_OPTREPOWER_EVENT_ID = -733;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision.ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        EponLinkQualityData result = (EponLinkQualityData) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_OPTREPOWER_ALERT_ID);
        param.setClearEventId(OLT_OPTREPOWER_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(result.getPortIndex()).toString());
        Float rePower = result.getRecvPower();
        param.setPerfValue(rePower);
        param.setTargetId(OLT_OPTREPOWER_FLAG);
        param.setTargetIndex(result.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.optRePower", "epon") + "[" + df.format(rePower) + "dBm ]");
        return param;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_OPTREPOWER_FLAG, this);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_OPTREPOWER_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        EponLinkQualityData result = (EponLinkQualityData) data.getPerfData();
        String portType = result.getPortType();
        return String.format("OLT_%s_RE_POWER  ", portType);
    }

}
