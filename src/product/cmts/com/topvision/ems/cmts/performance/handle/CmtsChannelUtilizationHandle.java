/***********************************************************************
 * $Id: CmtsChannelUtilizationHandle.java,v1.0 2013-10-8 下午2:15:08 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.ChannelUtilization;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author loyal
 * @created @2013-10-8-下午2:15:08
 * 
 */
@Service("cmtsChannelUtilizationHandle")
public class CmtsChannelUtilizationHandle extends CmtsPerformanceHandle {
    public static String CMTS_UTILIZATION_FLAG = "CMTS_UTILIZATION";
    public static Integer CMTS_UTILIZATION_ALERT_ID = -906;
    public static Integer CMTS_UTILIZATION_EVENT_ID = -907;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
        ChannelUtilization channelUtilization = (ChannelUtilization) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UTILIZATION_ALERT_ID);
        param.setClearEventId(CMTS_UTILIZATION_EVENT_ID);
        param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                channelUtilization.getChannelIndex()));
        param.setPerfValue(channelUtilization.getChannelUtilization() + 0F);
        param.setTargetId(CMTS_UTILIZATION_FLAG);
        param.setTargetIndex(channelUtilization.getChannelIndex());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.channelUtilization", "cmc") + "["
                + channelUtilization.getChannelUtilization() + "%]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UTILIZATION_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UTILIZATION_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UTILIZATION_FLAG;
    }
}
