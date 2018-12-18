/***********************************************************************
 * $Id: CmcPortOptRtPowerHandle.java,v1.0 2013-9-3 下午03:36:25 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.CmcLinkQualityData;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author lizongtian
 * @created @2013-9-3-下午03:36:25
 * 
 */
@Service("cmcPortOptRtPowerHandle")
public class CmcPortOptRtPowerHandle extends CmcPerformanceHandle {

    public static String CC_PON_RT_POWER_FLAG = "CC_PON_RT_POWER";
    public static Integer CC_PON_RT_POWER_ALERT_ID = -830;
    public static Integer CC_PON_RT_POWER_EVENT_ID = -831;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcLinkQualityData result = (CmcLinkQualityData) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_PON_RT_POWER_ALERT_ID);
        param.setClearEventId(CC_PON_RT_POWER_EVENT_ID);
        // param.setSource(result.getPortIndex().toString());
        param.setSource(getMac(result.getCmcId()));
        Float optRtPowerFloat = result.getOptTxPowerFloat();
        param.setPerfValue(optRtPowerFloat);
        param.setTargetId(CC_PON_RT_POWER_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                + getString("PerformanceAlert.optRtPower", "cmc") + "[" + df.format(optRtPowerFloat) + "dBm]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_PON_RT_POWER_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_PON_RT_POWER_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        CmcLinkQualityData result = (CmcLinkQualityData) data.getPerfData();
        //区分是ONU PON口还是CC的上联口接上了PON模块
        Entity entity = entityDao.selectByPrimaryKey(result.getCmcId());
        if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId())) {
            return "CC_ONU_MODULE_TX_POWER";
        } else {
            return "CC_PON_MODULE_TX_POWER";
        }
    }

}
