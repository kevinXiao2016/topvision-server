/***********************************************************************
 * $Id: UsBitErroRateHandle.java,v1.0 2013-6-19 下午04:26:48 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author Rod John
 * @created @2013-6-19-下午04:26:48
 * 
 */
@Service("cmcUsBitErrorRateHandle")
public class UsBitErrorRateHandle extends CmcPerformanceHandle {
    public static String ERRORRATE_FLAG = "CC_ERRORCODE";
    public static Integer ERRORRATE_ALERT_ID = -806;
    public static Integer ERRORRATE_EVENT_ID = -807;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcSignalQuality errorRate = (CmcSignalQuality) data.getPerfData();
        if (CmcSignalQuality.CMC_SCHEDULER.equals(errorRate.getSchedulerType())) {
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(ERRORRATE_ALERT_ID);
            param.setClearEventId(ERRORRATE_EVENT_ID);
            // CMC SOURCE
            param.setSource(getCmcSourceString(errorRate.getCmcChannelIndex()));
            param.setPerfValue(errorRate.getSigQCorrectedRate());
            param.setTargetId(ERRORRATE_FLAG);
            param.setTargetIndex(errorRate.getCmcChannelIndex());
            param.setMessage("CMTS[" + getMac(data.getEntityId()) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.usBitError", "cmc") + "["
                    + getString("PerformanceAlert.usBitErrorInfo", "cmc") + ":" + errorRate.getSigQCorrecteds() + "/"
                    + errorRate.getTotalCodeNum() + "/" + errorRate.getSigQCorrectedRate() + "%]");
            return param;
        } else if (CmcSignalQuality.CMTS_SCHEDULER.equals(errorRate.getSchedulerType())) {
            Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(ERRORRATE_ALERT_ID);
            param.setClearEventId(ERRORRATE_EVENT_ID);
            // CCMTS SOURCE
            param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                    errorRate.getCmcChannelIndex()));
            param.setPerfValue(errorRate.getSigQCorrectedRate());
            param.setTargetId(ERRORRATE_FLAG);
            param.setTargetIndex(errorRate.getCmcChannelIndex());
            param.setMessage("CMTS[" + getAdditionText(entity) + "]" + param.getSource() + "_"
                    + getString("PerformanceAlert.usBitError", "cmc") + "["
                    + getString("PerformanceAlert.usBitErrorInfo", "cmc") + ":" + errorRate.getSigQCorrecteds() + "/"
                    + errorRate.getTotalCodeNum() + "/" + errorRate.getSigQCorrectedRate() + "%]");
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
        performanceStatisticsCenter.registerPerformanceHandler(ERRORRATE_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ERRORRATE_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        return ERRORRATE_FLAG;
    }

}
