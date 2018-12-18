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
@Service("onuCatvTemperatureHandle")
public class OnuCatvTemperatureHandle extends PerformanceHandle {
    public static String ONU_CATV_TEMP = "ONU_CATV_TEMP";
    public static Integer ONU_CATV_TEMP_ALERT_ID = -785;
    public static Integer ONU_CATV_TEMP_EVENT_ID = -784;

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
        param.setAlertEventId(ONU_CATV_TEMP_ALERT_ID);
        param.setClearEventId(ONU_CATV_TEMP_EVENT_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(entry.getOnuIndex()).toString());
        param.setPerfValue(entry.getTemperature());
        param.setTargetId(ONU_CATV_TEMP);
        param.setTargetIndex(entry.getOnuIndex());
        param.setMessage("ONU[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuCatvTemp", "performance") + "[" + entry.getTemperature() + ResourceManager.getResourceManager("com.topvision.ems.epon.performance.oltperf").getString("COMMON.Centigrade")+ "]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_CATV_TEMP, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.handle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_CATV_TEMP);
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
        return ONU_CATV_TEMP;
    }

}
