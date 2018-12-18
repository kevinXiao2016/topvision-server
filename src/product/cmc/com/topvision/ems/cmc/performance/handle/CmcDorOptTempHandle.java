package com.topvision.ems.cmc.performance.handle;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.performance.facade.CmcDorOptTempQuality;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;

@Service("cmcDorOptTempHandle")
public class CmcDorOptTempHandle extends CmcPerformanceHandle {
    public static String CC_DOROPTTEMP_FLAG = "CC_DOROPT_TEMP";
    public static Integer CC_DOROPTTEMP_ALERT_ID = -854;
    public static Integer CC_DOROPTTEMP_EVENT_ID = -855;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        CmcDorOptTempQuality result = (CmcDorOptTempQuality) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(CC_DOROPTTEMP_ALERT_ID);
        param.setClearEventId(CC_DOROPTTEMP_EVENT_ID);
        param.setSource(getMac(result.getCmcId()));
        float dorOptTemp = result.getDorOptNodeTemp() / 10;
        param.setPerfValue(dorOptTemp);
        param.setTargetId(CC_DOROPTTEMP_FLAG);
        param.setTargetIndex(result.getCmcId());
        param.setMessage("CMTS[" + param.getSource() + "]" + getString("PerformanceAlert.dorOptTemp", "cmc") + "("
                + dorOptTemp + " " + getString("PerformanceAlert.tempUnit", "cmc") + ")");
        return param;
    }

    @Override
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(CC_DOROPTTEMP_FLAG, this);
    }

    @Override
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(CC_DOROPTTEMP_FLAG);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return CC_DOROPTTEMP_FLAG;
    }

}
