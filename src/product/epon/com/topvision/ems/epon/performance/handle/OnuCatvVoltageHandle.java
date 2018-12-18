/***********************************************************************
 * $Id: OnuCatvInfoHandle.java,v1.0 2016-5-9 下午3:27:15 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoResult;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016-5-9-下午3:27:15
 *
 */
@Service("onuCatvVoltageHandle")
public class OnuCatvVoltageHandle extends PerformanceHandle {
    public static String ONU_CATV_VOLTAGE = "ONU_CATV_VOLTAGE";
    public static Integer ONU_CATV_VOLTAGE_ALERT_ID = -787;
    public static Integer ONU_CATV_VOLTAGE_EVENT_ID = -786;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.handle.PerformanceHandle#getEventParams(com.topvision.ems.facade
     * .domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuCatvOrInfoResult entry = (OnuCatvOrInfoResult) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(entry.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_CATV_VOLTAGE_ALERT_ID);
        param.setClearEventId(ONU_CATV_VOLTAGE_EVENT_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(entry.getOnuIndex()).toString());
        param.setPerfValue(entry.getVoltage());
        param.setTargetId(ONU_CATV_VOLTAGE);
        param.setTargetIndex(entry.getOnuIndex());
        param.setMessage("ONU[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuCatvVoltage", "performance") + "[" + entry.getVoltage() + " V]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_CATV_VOLTAGE, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_CATV_VOLTAGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.handle.PerformanceHandle#getTypeCode(com.topvision.ems.facade
     * .domain.PerformanceData)
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return ONU_CATV_VOLTAGE;
    }

}
