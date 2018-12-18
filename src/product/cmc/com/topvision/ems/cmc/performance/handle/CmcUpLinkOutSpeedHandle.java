/***********************************************************************
 * $Id: CmcUpLinkOutSpeedHandle.java,v1.0 2013-9-3 下午02:42:44 $
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
 * @created @2013-9-3-下午02:42:44
 * 
 */
@Service("cmcUpLinkOutSpeedHandle")
public class CmcUpLinkOutSpeedHandle extends CmcPerformanceHandle {

    public static String CC_UPLINK_OUTSPEED_FLAG = "CC_UPLINK_OUTSPEED";
    public static Integer CC_UPLINK_OUTSPEED_ALERT_ID = -818;
    public static Integer CC_UPLINK_OUTSPEED_EVENT_ID = -819;

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
            param.setAlertEventId(CC_UPLINK_OUTSPEED_ALERT_ID);
            param.setClearEventId(CC_UPLINK_OUTSPEED_EVENT_ID);
            param.setSource(result.getIfIndex().toString());
            // Mbps与阈值单位一致
            Float upLinkSpeed = result.getIfOutSpeed() / NumberUtils.Mbps;
            param.setPerfValue(upLinkSpeed);
            param.setTargetId(CC_UPLINK_OUTSPEED_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                    + getString("PerformanceAlert.upLinkOutSpeed", "cmc") + "[" + df.format(upLinkSpeed) + "Mbps]");
            return param;
        } else if (data.getPerfData() instanceof CmtsFlowQuality) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            CmtsFlowQuality result = (CmtsFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CC_UPLINK_OUTSPEED_ALERT_ID);
            param.setClearEventId(CC_UPLINK_OUTSPEED_EVENT_ID);
            // CCMTS UPCHANNEL SOURCE
            param.setSource(getCmtsUpLinkSourceString(entity.getEntityId(), result.getIfIndex()));
            // Mbps与阈值单位一致
            Float upLinkSpeed = result.getIfOutSpeed() / NumberUtils.Mbps;
            param.setPerfValue(upLinkSpeed);
            param.setTargetId(CC_UPLINK_OUTSPEED_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + "_"
                    + getString("PerformanceAlert.upLinkOutSpeed", "cmc") + "[" + df.format(upLinkSpeed) + "Mbps]");
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
        performanceStatisticsCenter.registerPerformanceHandler(CC_UPLINK_OUTSPEED_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_UPLINK_OUTSPEED_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_UPLINK_OUTSPEED_FLAG;
    }

}
