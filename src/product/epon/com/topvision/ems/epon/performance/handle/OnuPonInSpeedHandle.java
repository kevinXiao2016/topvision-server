/***********************************************************************
 * $Id: OnuPonInSpeedHandle.java,v1.0 2015-5-7 上午11:44:49 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2015-5-7-上午11:44:49
 *
 */
@Service("onuPonInSpeedHandle")
public class OnuPonInSpeedHandle extends PerformanceHandle {
    public static final String ONU_PON_IN_SPEED = "ONU_PON_IN_SPEED";
    public static final Integer ONU_PONINSPEED_ALERT_ID = -723;
    public static final Integer ONU_PONINSPEED_CLEAR_ID = -722;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuFlowCollectInfo flowData = (OnuFlowCollectInfo) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(flowData.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_PONINSPEED_ALERT_ID);
        param.setClearEventId(ONU_PONINSPEED_CLEAR_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(flowData.getOnuIndex()).toString());
        //将单位同bps转换成Mbps
        float ponInSpeed = flowData.getPortInSpeed() / 1000 / 1000;
        param.setPerfValue(ponInSpeed);
        param.setTargetId(ONU_PON_IN_SPEED);
        param.setTargetIndex(flowData.getOnuIndex());
        param.setMessage("ONU PON[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuPonInSpeed", "performance") + "[" + df.format(ponInSpeed) + "Mbps ]");
        return param;
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_PON_IN_SPEED, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_PON_IN_SPEED);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return "ONU_PON_IN_SPEED";
    }

}
