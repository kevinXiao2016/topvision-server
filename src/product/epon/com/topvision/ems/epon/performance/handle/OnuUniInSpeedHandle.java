/***********************************************************************
 * $Id: OnuUniInSpeedHandle.java,v1.0 2015-5-7 上午11:47:39 $
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
 * @created @2015-5-7-上午11:47:39
 *
 */
@Service("onuUniInSpeedHandle")
public class OnuUniInSpeedHandle extends PerformanceHandle {
    public static final String ONU_UNI_IN_SPEED = "ONU_UNI_IN_SPEED";
    public static final Integer ONU_UNIINSPEED_ALERT_ID = -719;
    public static final Integer ONU_UNIINSPEED_CLEAR_ID = -718;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuFlowCollectInfo flowData = (OnuFlowCollectInfo) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(flowData.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_UNIINSPEED_ALERT_ID);
        param.setClearEventId(ONU_UNIINSPEED_CLEAR_ID);
        param.setSource("ONU:" + EponIndex.getUniStringByIndex(flowData.getPortIndex()).toString());
        //将单位同bps转换成Mbps
        float uniInSpeed = flowData.getPortInSpeed() / 1000 / 1000;
        param.setPerfValue(uniInSpeed);
        param.setTargetId(ONU_UNI_IN_SPEED);
        param.setTargetIndex(flowData.getPortIndex());
        param.setMessage("ONU UNI[" + param.getSource() + "]" +
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuUniInSpeed", "performance") + "[" + df.format(uniInSpeed) + "Mbps ]");
        return param;
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_UNI_IN_SPEED, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_UNI_IN_SPEED);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return ONU_UNI_IN_SPEED;
    }

}
