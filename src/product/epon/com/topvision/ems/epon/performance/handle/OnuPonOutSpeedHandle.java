/***********************************************************************
 * $Id: OnuPonOutSpeedHandle.java,v1.0 2015-5-7 上午11:46:31 $
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
 * @created @2015-5-7-上午11:46:31
 *
 */
@Service("onuPonOutSpeedHandle")
public class OnuPonOutSpeedHandle extends PerformanceHandle {
    public static final String ONU_PON_OUT_SPEED = "ONU_PON_OUT_SPEED";
    public static final Integer ONU_PONOUTSPEED_ALERT_ID = -721;
    public static final Integer ONU_PONOUTSPEED_CLEAR_ID = -720;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuFlowCollectInfo flowData = (OnuFlowCollectInfo) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(flowData.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_PONOUTSPEED_ALERT_ID);
        param.setClearEventId(ONU_PONOUTSPEED_CLEAR_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(flowData.getOnuIndex()).toString());
        //将单位同bps转换成Mbps
        float ponOutSpeed = flowData.getPortOutSpeed() / 1000 / 1000;
        param.setPerfValue(ponOutSpeed);
        param.setTargetId(ONU_PON_OUT_SPEED);
        param.setTargetIndex(flowData.getOnuIndex());
        param.setMessage("ONU PON[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuPonOutSpeed", "performance") + "[" + df.format(ponOutSpeed) + "Mbps]");
        return param;
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_PON_OUT_SPEED, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_PON_OUT_SPEED);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return ONU_PON_OUT_SPEED;
    }

}
