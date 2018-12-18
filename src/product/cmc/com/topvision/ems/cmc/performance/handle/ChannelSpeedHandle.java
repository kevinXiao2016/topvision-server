/***********************************************************************
 * $Id: ChannelSpeedHandle.java,v1.0 2013-6-19 上午10:13:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.ems.cmc.performance.facade.CmtsFlowQuality;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.framework.common.NumberUtils;

/**
 * @author Rod John
 * @created @2013-6-19-上午10:13:35
 * 
 */
@Service("cmcChannelSpeedHandle")
public class ChannelSpeedHandle extends CmcPerformanceHandle {
    public static String SPEED_FLAG = "CC_SPEED";
    public static Integer SPEED_ALERT_ID = -804;
    public static Integer SPEED_EVENT_ID = -805;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.facade.domain.PerformanceResult)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        if (data.getPerfData() instanceof CmcFlowQuality) {
            CmcFlowQuality speed = (CmcFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(SPEED_ALERT_ID);
            param.setClearEventId(SPEED_EVENT_ID);
            String source = getCmcSourceString(speed.getIfIndex());
            if (source == null) {
                return null;
            }
            param.setSource(source);
            // Mbps与阈值单位一致,对信道不区分上下行
            float value = (speed.getIfInSpeed() + speed.getIfOutSpeed()) / NumberUtils.Mbps;
            String speedString = df.format(value);
            param.setPerfValue(value);
            param.setTargetId(SPEED_FLAG);
            param.setTargetIndex(speed.getIfIndex());
            param.setMessage("CMTS[" + getMac(data.getEntityId()) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.channelRate", "cmc") + "[" + speedString + "Mbps]");
            return param;
        } else if (data.getPerfData() instanceof CmtsFlowQuality) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            CmtsFlowQuality speed = (CmtsFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(SPEED_ALERT_ID);
            param.setClearEventId(SPEED_EVENT_ID);
            // CMTS CHANNEL SOURCE
            param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(), speed.getIfIndex()));
            // Mbps与阈值单位一致,对信道不区分上下行
            float value = (speed.getIfInSpeed() + speed.getIfOutSpeed()) / NumberUtils.Mbps;
            String speedString = df.format(value);
            param.setPerfValue(value);
            param.setTargetId(SPEED_FLAG);
            param.setTargetIndex(speed.getIfIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.channelRate", "cmc") + "[" + speedString + "Mbps]");
            return param;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(SPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(SPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return SPEED_FLAG;
    }

}
