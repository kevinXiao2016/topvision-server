/***********************************************************************
 * $Id: CmtsUsBitUnErrorRateHandle.java,v1.0 2013-10-8 上午9:39:38 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.UsBitErrorRate;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author loyal
 * @created @2013-10-8-上午9:39:38
 * 
 */
@Service("cmtsUsBitUnErrorRateHandle")
public class CmtsUsBitUnErrorRateHandle extends CmtsPerformanceHandle {
    public static String CMTS_UNERRORRATE_FLAG = "CMTS_UNERRORCODE";
    public static Integer CMTS_UNERRORRATE_ALERT_ID = -914;
    public static Integer CMTS_UNERRORRATE_EVENT_ID = -915;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
        UsBitErrorRate usBitErrorRate = (UsBitErrorRate) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_UNERRORRATE_ALERT_ID);
        param.setClearEventId(CMTS_UNERRORRATE_EVENT_ID);
        param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                usBitErrorRate.getChannelIndex()));
        param.setPerfValue(usBitErrorRate.getUnBitErrorRate() + 0F);
        param.setTargetId(CMTS_UNERRORRATE_FLAG);
        param.setTargetIndex(usBitErrorRate.getChannelIndex());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.usBitUnError", "cmc") + "[" + usBitErrorRate.getUnBitErrorRate() + "%]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_UNERRORRATE_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_UNERRORRATE_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_UNERRORRATE_FLAG;
    }
}
