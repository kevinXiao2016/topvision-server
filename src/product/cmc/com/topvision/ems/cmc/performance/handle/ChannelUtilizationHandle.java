/***********************************************************************
 * $Id: ChannelUtilizationHandle.java,v1.0 2013-6-19 下午03:45:26 $
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

/**
 * @author Rod John
 * @created @2013-6-19-下午03:45:26
 * 
 */
@Service("cmcChannelUtilizationHandle")
public class ChannelUtilizationHandle extends CmcPerformanceHandle {
    public static String UTILIZATION_FLAG = "CC_UTILIZATION";
    public static Integer UTILIZATION_ALERT_ID = -802;
    public static Integer UTILIZATION_EVENT_ID = -803;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        if (data.getPerfData() instanceof CmcFlowQuality) {
            CmcFlowQuality utilization = (CmcFlowQuality) data.getPerfData();
            String maxMes = "";
            Long maxRate = utilization.getIfHighSpeed();
            //信道未开启 MIB返回值为0,进行无效数据处理
            if (maxRate == 0) {
                maxMes = "--";
            } else {
                maxMes = maxRate.toString();
            }
            String realRate = df.format((utilization.getIfInSpeed() + utilization.getIfOutSpeed()) / 1000 / 1000);

            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(UTILIZATION_ALERT_ID);
            param.setClearEventId(UTILIZATION_EVENT_ID);
            String source = getCmcSourceString(utilization.getIfIndex());
            if (source == null || utilization.getIfUtilization() >= 100) {
                return null;
            }
            param.setSource(source);
            param.setPerfValue(utilization.getIfUtilization());
            param.setTargetId(UTILIZATION_FLAG);
            param.setTargetIndex(utilization.getIfIndex());
            param.setMessage("CMTS[" + getMac(data.getEntityId()) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.channelUtilization", "cmc") + "["
                    + getString("PerformanceAlert.channelUsed", "cmc") + ":" + realRate + "Mbps/" + maxMes + "Mbps/"
                    + df.format(utilization.getIfUtilization()) + "%]");
            return param;
        } else if (data.getPerfData() instanceof CmtsFlowQuality) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            CmtsFlowQuality utilization = (CmtsFlowQuality) data.getPerfData();
            String maxMes = "";
            Long maxRate = 0L;
            if (utilization.getIfSpeed() != null) {
                maxRate = utilization.getIfSpeed() / 1000 / 1000;
            }
            //信道未开启 MIB返回值为0,进行无效数据处理
            if (maxRate == 0) {
                maxMes = "--";
            } else {
                maxMes = maxRate.toString();
            }
            String realRate = df.format((utilization.getIfInSpeed() + utilization.getIfOutSpeed()) / 1000 / 1000);
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(UTILIZATION_ALERT_ID);
            param.setClearEventId(UTILIZATION_EVENT_ID);
            // CMTS CHANNEL SOURCE
            param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                    utilization.getIfIndex()));
            param.setPerfValue(utilization.getIfUtilization());
            if (utilization.getIfUtilization() >= 100) {
                return null;
            }
            param.setTargetId(UTILIZATION_FLAG);
            param.setTargetIndex(utilization.getIfIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.channelUtilization", "cmts") + "["
                    + getString("PerformanceAlert.channelUsed", "cmts") + ":" + realRate + "Mbps/" + maxMes + "Mbps/"
                    + df.format(utilization.getIfUtilization()) + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(UTILIZATION_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(UTILIZATION_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return UTILIZATION_FLAG;
    }

}
