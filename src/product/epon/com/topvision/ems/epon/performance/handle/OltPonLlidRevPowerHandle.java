/***********************************************************************
 * $Id: OltPonLlidRevPowerHandle.java,v1.0 2015-4-22 下午5:21:15 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2015-4-22-下午5:21:15
 *
 */
@Service("oltPonLlidRevPowerHandle")
public class OltPonLlidRevPowerHandle extends PerformanceHandle {
    public static final String OLT_PONLLID_RE_POWER = "OLT_PONLLID_RE_POWER";
    public static final String OLT_GPONLLID_RE_POWER = "OLT_GPONLLID_RE_POWER";
    public static final Integer OLT_LLIDREVPOWER_ALERT_ID = -725;
    public static final Integer OLT_LLIDREVPOWER_CLEAR_ID = -724;

    @Autowired
    private OnuDao onuDao;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuLinkQualityResult result = (OnuLinkQualityResult) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_LLIDREVPOWER_ALERT_ID);
        param.setClearEventId(OLT_LLIDREVPOWER_CLEAR_ID);
        param.setSource("PON:" + EponIndex.getPortStringByIndex(result.getOnuIndex()).toString());
        Float rePower = result.getOltPonRevPower();
        param.setPerfValue(rePower);
        param.setTargetId(OLT_PONLLID_RE_POWER);
        param.setTargetIndex(result.getOnuIndex());
        param.setMessage("OLT[" + getHost(result.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("Performance.oltPonRePower", "performance") + "[" + df.format(rePower) + "dBm ]");
        return param;
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_PONLLID_RE_POWER, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_PONLLID_RE_POWER);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        OnuLinkQualityResult linkData = (OnuLinkQualityResult) data.getPerfData();
        OltOnuAttribute onu = onuDao.getOnuEntityById(linkData.getOnuId());
        if (GponConstant.GPON_ONU.equals(onu.getOnuEorG())) {
            return OLT_GPONLLID_RE_POWER;
        } else {
            return OLT_PONLLID_RE_POWER;
        }
    }
}
