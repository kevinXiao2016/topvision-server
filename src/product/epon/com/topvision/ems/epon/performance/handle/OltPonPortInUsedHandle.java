/***********************************************************************
 * $Id: OltPonPortSpeedHandle.java,v1.0 2013-6-22 上午10:04:09 $
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
 * @created @2013-6-22-上午10:04:09
 * 
 */
@Service("oltPonPortInUsedHandle")
public class OltPonPortInUsedHandle extends PerformanceHandle {
    public static String PON_INUSED_FLAG = "OLT_PON_IN_USED";
    public static Integer PON_INUSED_ALERT_ID = -768;
    public static Integer PON_INUSED_EVENT_ID = -767;

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
        String maxMes = "";
        Long maxRate = flowPerf.getUsBandwidth()/1000;
        //端口未上线 MIB返回值为0,进行无效数据处理
        if (maxRate == 0) {
            maxMes = "--";
        } else {
            maxMes = maxRate.toString();
        }
        String realRate = df.format(flowPerf.getIfInSpeed() / 1000 / 1000);
        param.setAlertEventId(PON_INUSED_ALERT_ID);
        param.setClearEventId(PON_INUSED_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(flowPerf.getPortIndex()).toString());
        param.setPerfValue(flowPerf.getPortInUsed() * 100);
        param.setTargetId(PON_INUSED_FLAG);
        param.setTargetIndex(flowPerf.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.ponInUsed", "epon") + "["
                + getString("PerformanceAlert.portSpeedMes", "epon") + ":" + realRate + "Mbps/" + maxMes + "Mbps/"
                + df.format(flowPerf.getPortInUsed() * 100) + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(PON_INUSED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(PON_INUSED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return PON_INUSED_FLAG;
    }

}
