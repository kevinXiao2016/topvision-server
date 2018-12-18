/***********************************************************************
 * $Id: CmtsNoiseHandle.java,v1.0 2013-9-30 下午1:55:21 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.domain.SingleNoise;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

/**
 * @author loyal
 * @created @2013-9-30-下午1:55:21
 *
 */
@Service("cmtsNoiseHandle")
public class CmtsNoiseHandle extends CmtsPerformanceHandle {
    public static String CMTS_NOISE_FLAG = "CMTS_NOISE";
    public static Integer CMTS_NOISE_ALERT_ID = -910;
    public static Integer CMTS_NOISE_EVENT_ID = -911;
    public static Integer IGNORE_NOISE_VALUE = 0;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        Entity entity = entityDao.selectByPrimaryKey(data.getEntityId());
        SingleNoise noise = (SingleNoise) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CMTS_NOISE_ALERT_ID);
        param.setClearEventId(CMTS_NOISE_EVENT_ID);
        param.setSource(getCmtsChannelSourceString(entity.getTypeId(), entity.getEntityId(), noise.getIfIndex()
                .longValue()));
        // Modify by loyal @2014-2-13 信噪比为0时不发告警
        if (IGNORE_NOISE_VALUE.equals(noise.getNoise())) {
            return null;
        }
        param.setPerfValue(noise.getNoise() + 0F);
        param.setTargetId(CMTS_NOISE_FLAG);
        param.setTargetIndex(noise.getIfIndex());
        param.setMessage("CMTS[" + getHost(data.getEntityId()) + "]" + param.getSource() + "_"
                + getString("PerformanceAlert.noise", "cmc") + "[" + noise.getNoise() / 10 + "dB]");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CMTS_NOISE_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CMTS_NOISE_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CMTS_NOISE_FLAG;
    }

}
