/***********************************************************************
 * $Id: CmcUpLinkInUsedHandle.java,v1.0 2015-1-27 下午02:53:05 $
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

/**
 * @author lizongtian
 * @created @2015-1-27-下午02:53:05
 * 
 */
@Service("cmcUpLinkInUsedHandle")
public class CmcUpLinkInUsedHandle extends CmcPerformanceHandle {

    public static String CC_UPLINK_IN_UESD_FLAG = "CC_UPLINK_IN_UESD";
    public static Integer CC_UPLINK_IN_UESD_ALERT_ID = -850;
    public static Integer CC_UPLINK_IN_UESD_EVENT_ID = -851;

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
            if (result == null) {
                return null;
            }
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CC_UPLINK_IN_UESD_ALERT_ID);
            param.setClearEventId(CC_UPLINK_IN_UESD_EVENT_ID);
            param.setSource(result.getIfIndex().toString());
            if (result.getIfInUsed() == null || result.getIfInUsed() >= 100) {
                return null;
            }
            Float upLinkUsed = Float.parseFloat(result.getIfInUsed().toString());
            String maxMes = "";
            Long maxRate = result.getIfSpeed() / 1000 / 1000;
            //端口未上线 MIB返回值为0,进行无效数据处理
            if (maxRate == 0) {
                maxMes = "--";
            } else {
                maxMes = maxRate.toString();
            }
            String realRate = df.format(result.getIfInSpeed() / 1000 / 1000);
            param.setPerfValue(upLinkUsed);
            param.setTargetId(CC_UPLINK_IN_UESD_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getMac(result.getCmcId()) + "]" + "_"
                    + getString("PerformanceAlert.upLinkSpeed", "cmc") + "["
                    + getString("PerformanceAlert.upLinkSpeedInfo", "cmc") + ":" + realRate + "Mbps/" + maxMes
                    + "Mbps/" + df.format(upLinkUsed) + "%]");
            return param;
        } else if (data.getPerfData() instanceof CmtsFlowQuality) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            CmtsFlowQuality result = (CmtsFlowQuality) data.getPerfData();
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CC_UPLINK_IN_UESD_ALERT_ID);
            param.setClearEventId(CC_UPLINK_IN_UESD_EVENT_ID);
            // CCMTS UPCHANNEL SOURCE
            param.setSource(getCmtsUpLinkSourceString(entity.getEntityId(), result.getIfIndex()));
            if (result.getIfInUsed() >= 100) {
                return null;
            }
            Float upLinkUsed = Float.parseFloat(result.getIfInUsed().toString());
            String maxMes = "";
            Long maxRate = 0L;
            if (result.getIfSpeed() != null) {
                maxRate = result.getIfSpeed() / 1000 / 1000;
            }
            //端口未上线 MIB返回值为0,进行无效数据处理
            if (maxRate == 0) {
                maxMes = "--";
            } else {
                maxMes = maxRate.toString();
            }
            String realRate = df.format(result.getIfInSpeed() / 1000 / 1000);
            param.setPerfValue(upLinkUsed);
            param.setTargetId(CC_UPLINK_IN_UESD_FLAG);
            param.setTargetIndex(result.getIfIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + "_"
                    + getString("PerformanceAlert.upLinkSpeed", "cmts") + "["
                    + getString("PerformanceAlert.upLinkSpeedInfo", "cmc") + ":" + realRate + "Mbps/" + maxMes
                    + "Mbps/" + df.format(upLinkUsed) + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(CC_UPLINK_IN_UESD_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_UPLINK_IN_UESD_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_UPLINK_IN_UESD_FLAG;
    }

}
