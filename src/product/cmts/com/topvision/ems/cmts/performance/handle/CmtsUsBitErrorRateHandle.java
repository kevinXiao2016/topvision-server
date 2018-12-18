/***********************************************************************
 * $Id: CmtsUsBitErrorRateHandle.java,v1.0 2013-10-8 上午9:24:24 $
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
 * @created @2013-10-8-上午9:24:24
 * 
 */
@Service("cmtsUsBitErrorRateHandle")
public class CmtsUsBitErrorRateHandle extends CmtsPerformanceHandle {
    public static String CMTS_USBITERRORRATE_FLAG = "CMTS_ERRORCODE";
    public static Integer CMTS_USBITERRORRATE_ALERT_ID = -912;
    public static Integer CMTS_USBITERRORRATE_EVENT_ID = -913;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
        UsBitErrorRate usBitErrorRate = (UsBitErrorRate) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_USBITERRORRATE_ALERT_ID);
        param.setClearEventId(CMTS_USBITERRORRATE_EVENT_ID);
        param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(),
                usBitErrorRate.getChannelIndex()));
        param.setPerfValue(usBitErrorRate.getBitErrorRate() + 0F);
        param.setTargetId(CMTS_USBITERRORRATE_FLAG);
        param.setTargetIndex(usBitErrorRate.getChannelIndex());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.usBitError", "cmc") + "[" + usBitErrorRate.getBitErrorRate() + "%]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_USBITERRORRATE_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_USBITERRORRATE_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_USBITERRORRATE_FLAG;
    }

}
