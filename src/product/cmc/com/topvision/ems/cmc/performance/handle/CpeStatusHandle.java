/***********************************************************************
 * $Id: CpeStatusHandle.java,v1.0 2015年6月23日 下午8:21:35 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.handle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.cpe.service.CpeAnalyseService;
import com.topvision.ems.cmc.cpe.service.CpeService;
import com.topvision.ems.cmc.domain.CmCollectConfig;
import com.topvision.ems.cmc.domain.CpeCollectConfig;
import com.topvision.ems.cmc.facade.domain.CmCpe;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CpeStatusPerfResult;
import com.topvision.ems.cmc.topology.dao.CmcDiscoveryDao;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.handle.PerformanceHandle;

/**
 * @author Victor
 * @created @2015年6月23日-下午8:21:35
 *
 */
@Service("cpeStatusHandle")
public class CpeStatusHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CPE_STATUS";
    @Autowired
    private CmcDiscoveryDao cmcDiscoveryDao;
    @Autowired
    private CpeService cpeService;
    @Autowired
    private CpeAnalyseService cpeAnalyseService;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(HANDLE_ID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(HANDLE_ID);
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        return HANDLE_ID;
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        return null;
    }

    @Override
    public void handle(PerformanceData data) {
        CpeStatusPerfResult cpeStatusPerfResult = (CpeStatusPerfResult) data.getPerfData();
        List<TopCpeAttribute> attributes = cpeStatusPerfResult.getCpeAttributes();
        if (attributes != null) {
            List<CmCpe> cmCpes = new ArrayList<CmCpe>();
            for (TopCpeAttribute topCpeAttribute : attributes) {
                CmCpe cmCpe = new CmCpe();
                cmCpe.setTopCmCpeCcmtsIfIndex(topCpeAttribute.getTopCmCpeCcmtsIfIndex());
                cmCpe.setTopCmCpeCmStatusIndex(topCpeAttribute.getTopCmCpeCmStatusIndex());
                cmCpe.setTopCmCpeIpAddress(topCpeAttribute.getTopCmCpeIpAddress());
                cmCpe.setTopCmCpeMacAddressString(topCpeAttribute.getTopCmCpeMacAddress().toString());
                cmCpe.setTopCmCpeToCmMacAddr(topCpeAttribute.getTopCmCpeToCmMacAddr());
                cmCpe.setTopCmCpeType(topCpeAttribute.getTopCmCpeType());
                cmCpe.setEntityId(cpeStatusPerfResult.getEntityId());
                cmCpe.setUpdateTime(new Timestamp(cpeStatusPerfResult.getDt()));
                cmCpe.setTopCmCpeMacAddress(topCpeAttribute.getTopCmCpeMacAddress());
                cmCpes.add(cmCpe);
            }
            cmcDiscoveryDao.batchInsertOrUpdateCmCpe(cmCpes, cpeStatusPerfResult.getEntityId());
            cmcDiscoveryDao.batchInsertOrUpdateCpeAttribute(cpeStatusPerfResult.getDt(), attributes,
                    cpeStatusPerfResult.getEntityId());
        }

        CpeCollectConfig cpeCollectConfig = cpeService.getCpeCollectConfig();
        if (CmCollectConfig.START.intValue() == cpeCollectConfig.getCpeActionStatisticStatus().intValue()) {
            cpeAnalyseService.append(cpeStatusPerfResult);
        }
    }
}
