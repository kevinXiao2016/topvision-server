/***********************************************************************
 * $Id: OltPortOptTxPowerHandle.java,v1.0 2013-9-3 下午02:03:43 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.handle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.performance.domain.EponLinkQualityData;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2013-9-3-下午02:03:43
 * 
 */
@Service("oltPortOptTxPowerHandle")
public class OltPortOptTxPowerHandle extends PerformanceHandle {
    public static String OLT_OPTTXPOWER_FLAG = "OLT_PORT_TX_POWER";
    public static Integer OLT_OPTTXPOWER_ALERT_ID = -736;
    public static Integer OLT_OPTTXPOWER_EVENT_ID = -737;
    private static Integer OLT_GPON_PORT_TYPE = 3;

    @Autowired
    private OltPonDao oltPonDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.performanceHandle.PerformanceHandle#getEventParams(com.topvision
     * .ems.performance.domain.PerformanceData)
     */
    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        EponLinkQualityData result = (EponLinkQualityData) data.getPerfData();
        PerfThresholdEventParams param = new PerfThresholdEventParams();
        param.setAlertEventId(OLT_OPTTXPOWER_ALERT_ID);
        param.setClearEventId(OLT_OPTTXPOWER_EVENT_ID);
        param.setSource(EponIndex.getPortStringByIndex(result.getPortIndex()).toString());
        Float optTxPower = result.getTransPower();
        param.setPerfValue(optTxPower);
        param.setTargetId(OLT_OPTTXPOWER_FLAG);
        param.setTargetIndex(result.getPortIndex());
        param.setMessage("OLT[" + getHost(data.getEntityId()) + "] port " + param.getSource() + "_"
                + getString("PerformanceAlert.optRtPower", "epon") + "[" + df.format(optTxPower) + "dBm]");
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(OLT_OPTTXPOWER_FLAG, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(OLT_OPTTXPOWER_FLAG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.performanceHandle.PerformanceHandle#getFlag()
     */
    @Override
    public String getTypeCode(PerformanceData data) {
        EponLinkQualityData result = (EponLinkQualityData) data.getPerfData();
        Long entityId = result.getEntityId();
        Long ponIndex = result.getPortIndex();
        Integer ponType = oltPonDao.getPonPortType(entityId, ponIndex);
        if (OLT_GPON_PORT_TYPE.equals(ponType)) {
            return String.format("OLT_%s_TX_POWER", "GPON");
        } else {
            String portType = result.getPortType();
            return String.format("OLT_%s_TX_POWER", portType);
        }
    }

}
