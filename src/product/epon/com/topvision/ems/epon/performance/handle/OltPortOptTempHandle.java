/***********************************************************************
 * $Id: OltPortOptTempHandle.java,v1.0 2013-9-3 下午01:53:03 $
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
 * @created @2013-9-3-下午01:53:03
 * 
 */
@Service("oltPortOptTempHandle")
public class OltPortOptTempHandle extends PerformanceHandle {
    public static String OLT_OPTTEMP_FLAG = "OLT_PORT_TEMP";
    public static Integer OLT_OPTTEMP_ALERT_ID = -734;
    public static Integer OLT_OPTTEMP_EVENT_ID = -735;

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
        param.setAlertEventId(OLT_OPTTEMP_ALERT_ID);
        param.setClearEventId(OLT_OPTTEMP_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(result.getPortIndex()).toString());
        Float optTemp = result.getWorkingTemp();
        param.setPerfValue(optTemp);
        param.setTargetId(OLT_OPTTEMP_FLAG);
        param.setTargetIndex(result.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.optTemp", "epon") + "(" + optTemp.intValue() + " "
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
        performanceStatisticsCenter.registerPerformanceHandler(OLT_OPTTEMP_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_OPTTEMP_FLAG);
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
        return String.format("OLT_%s_OPT_TEMP", portType);
    }

}
