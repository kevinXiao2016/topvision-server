/***********************************************************************
 * $Id: CmtsChannelSpeedHandle.java,v1.0 2013-10-8 下午2:14:59 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.ChannelSpeedStatic;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.framework.common.NumberUtils;

/**
 * @author loyal
 * @created @2013-10-8-下午2:14:59
 * 
 */
@Service("cmtsChannelSpeedHandle")
public class CmtsChannelSpeedHandle extends CmtsPerformanceHandle {
    public static String CMTS_SPEED_FLAG = "CMTS_SPEED";
    public static Integer CMTS_SPEED_ALERT_ID = -908;
    public static Integer CMTS_SPEED_EVENT_ID = -909;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
        ChannelSpeedStatic channelSpeedStatic = (ChannelSpeedStatic) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_SPEED_ALERT_ID);
        param.setClearEventId(CMTS_SPEED_EVENT_ID);
        param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                channelSpeedStatic.getChannelIndex()));
        param.setPerfValue(new Float(NumberUtils.TWODOT_FORMAT.format(channelSpeedStatic.getChannelOctetsRate()
                / NumberUtils.Mbps)));
        param.setTargetId(CMTS_SPEED_FLAG);
        param.setTargetIndex(channelSpeedStatic.getChannelIndex());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.channelRate", "cmc") + "[" + channelSpeedStatic.getChannelOctetsRate()
                + "Mbps]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_SPEED_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_SPEED_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_SPEED_FLAG;
    }
}
