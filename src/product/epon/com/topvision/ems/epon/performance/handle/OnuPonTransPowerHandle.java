/***********************************************************************
 * $Id: OnuPonTransPowerHandle.java,v1.0 2015-4-22 下午5:20:37 $
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
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2015-4-22-下午5:20:37
 *
 */
@Service("onuPonTransPowerHandle")
public class OnuPonTransPowerHandle extends PerformanceHandle {
    public static final String ONU_PON_TX_POWER = "ONU_PON_TX_POWER";
    private static final String ONU_GPON_TX_POWER = "ONU_GPON_TX_POWER";
    public static final Integer ONU_PONTRANSPOWER_ALERT_ID = -729;
    public static final Integer ONU_PONTRANSPOWER_CLEAR_ID = -728;
    @Autowired
    private OnuDao onuDao;

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        OnuLinkQualityResult linkData = (OnuLinkQualityResult) data.getPerfData();
        Entity onuEntity= entityDao.selectByPrimaryKey(linkData.getOnuId());
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(ONU_PONTRANSPOWER_ALERT_ID);
        param.setClearEventId(ONU_PONTRANSPOWER_CLEAR_ID);
        param.setSource("ONU:" + EponIndex.getOnuStringByIndex(linkData.getOnuIndex()).toString());
        Float transPower = linkData.getOnuPonTransPower();
        param.setPerfValue(transPower);
        param.setTargetId(ONU_PON_TX_POWER);
        param.setTargetIndex(linkData.getOnuIndex());
        param.setMessage("ONU[" + param.getSource() + "]" + 
                "[" + onuEntity.getName() + "]" +
                getString("Performance.onuPonTxPower", "performance") + "[" + df.format(transPower) + "dBm ]");
        return param;
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(ONU_PON_TX_POWER, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(ONU_PON_TX_POWER);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        OnuLinkQualityResult linkData = (OnuLinkQualityResult) data.getPerfData();
        OltOnuAttribute onu = onuDao.getOnuEntityById(linkData.getOnuId());
        if (GponConstant.GPON_ONU.equals(onu.getOnuEorG())) {
            return ONU_GPON_TX_POWER;
        } else {
            return ONU_PON_TX_POWER;
        }
    }
}
