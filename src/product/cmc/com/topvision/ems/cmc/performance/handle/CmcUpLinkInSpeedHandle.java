/***********************************************************************
 * $Id: CmcUpLinkInSpeedHandle.java,v1.0 2013-9-3 下午02:33:41 $
 * 
 * @author: lizongtian
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
 * @author lizongtian
 * @created @2013-9-3-下午02:33:41
 * 
 */
@Service("cmcUpLinkInSpeedHandle")
public class CmcUpLinkInSpeedHandle extends CmcPerformanceHandle {
    public static String CC_UPLINK_INSPEED_FLAG = "CC_UPLINK_INSPEED";
    public static Integer CC_UPLINK_INSPEED_ALERT_ID = -816;
    public static Integer CC_UPLINK_INSPEED_EVENT_ID = -817;

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
            CmcFlowQuality result = (CmcFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CC_UPLINK_INSPEED_ALERT_ID);
            param.setClearEventId(CC_UPLINK_INSPEED_EVENT_ID);
            param.setSource(result.getIfIndex().toString());
            // Mbps与阈值单位一致
            Float upLinkSpeed = result.getIfInSpeed() / NumberUtils.Mbps;
            param.setPerfValue(upLinkSpeed);
            param.setTargetId(CC_UPLINK_INSPEED_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                    + getString("PerformanceAlert.upLinkInSpeed", "cmc") + "[" + df.format(upLinkSpeed) + "Mbps]");
            return param;
        } else if (data.getPerfData() instanceof CmtsFlowQuality) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            CmtsFlowQuality result = (CmtsFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CC_UPLINK_INSPEED_ALERT_ID);
            param.setClearEventId(CC_UPLINK_INSPEED_EVENT_ID);
            // CCMTS UPCHANNEL SOURCE
            param.setSource(getCmtsUpLinkSourceString(entity.getEntityId(), result.getIfIndex()));
            // Mbps与阈值单位一致
            Float upLinkSpeed = result.getIfInSpeed() / NumberUtils.Mbps;
            param.setPerfValue(upLinkSpeed);
            param.setTargetId(CC_UPLINK_INSPEED_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + "_"
                    + getString("PerformanceAlert.upLinkInSpeed", "cmc") + "[" + df.format(upLinkSpeed) + "Mbps]");
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
        performanceStatisticsCenter.registerPerformanceHandler(CC_UPLINK_INSPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_UPLINK_INSPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_UPLINK_INSPEED_FLAG;
    }

}
