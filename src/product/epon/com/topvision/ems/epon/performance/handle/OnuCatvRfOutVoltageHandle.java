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
import com.topvision.platform.ResourceManager;

/**
 * @author Rod John
 * @created @2016-5-9-下午3:27:15
 *
 */
@Service("onuCatvRfOutVoltageHandle")
public class OnuCatvRfOutVoltageHandle extends PerformanceHandle {
    public static String ONU_CATV_OUT_VOLTAGE = "ONU_CATV_RF";
    public static Integer ONU_CATV_OUT_VOLTAGE_ALERT_ID = -783;
    public static Integer ONU_CATV_OUT_VOLTAGE_EVENT_ID = -782;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.handle.PerformanceHandle#getEventParams(com.topvision.ems.facade
     * .domain.PerformanceData)
     */
    private ResourceManager getResourece(){
        return ResourceManager.getResourceManager("com.topvision.ems.epon.performance.oltperf");
    } 
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuCatvOrInfoResult entry = (OnuCatvOrInfoResult) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(entry.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_CATV_OUT_VOLTAGE_ALERT_ID);
        param.setClearEventId(ONU_CATV_OUT_VOLTAGE_EVENT_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(entry.getOnuIndex()).toString());
        param.setPerfValue(entry.getOutVoltage());
        param.setTargetId(ONU_CATV_OUT_VOLTAGE);
        param.setTargetIndex(entry.getOnuIndex());
        param.setMessage("ONU[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuCatvRf", "performance") + "[" + entry.getOutVoltage() +  getResourece().getString("olt.dbuv")+"]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_CATV_OUT_VOLTAGE, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_CATV_OUT_VOLTAGE);
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
        return ONU_CATV_OUT_VOLTAGE;
    }

}
