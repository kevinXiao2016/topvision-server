/***********************************************************************
 * $Id: OnuOnlineSaver.java,v1.0 2015-4-22 下午4:33:03 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;
import com.topvision.ems.epon.performance.handle.OltPonLlidRevPowerHandle;
import com.topvision.ems.epon.performance.handle.OnuPonRevPowerHandle;
import com.topvision.ems.epon.performance.handle.OnuPonTransPowerHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author flack
 * @created @2015-4-22-下午4:33:03
 *
 */
@Engine("onuLinkQualitySaver")
public class OnuLinkQualitySaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<OnuLinkQualityResult, OperClass> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(OnuLinkQualityResult performanceResult) {
        OnuPerfDao onuPerfDao = engineDaoFactory.getEngineDao(OnuPerfDao.class);
        if (performanceResult != null) {
            Long onuId = performanceResult.getOnuId();
            //存储链路质量数据
            try {
                onuPerfDao.insertOnuLinkQuality(performanceResult);
            } catch (Exception e) {
                logger.error("insertOnuLinkQuality Failed", e);
            }
            // 性能阈值数据处理
            List<PerformanceData> pList = new ArrayList<PerformanceData>();
            if (performanceResult.getOnuPonRevPower() != null && performanceResult.getOnuPonRevPower() != 0) {
                PerformanceData onuRevPower = new PerformanceData(onuId, OnuPonRevPowerHandle.ONU_PON_RE_POWER,
                        performanceResult);
                pList.add(onuRevPower);
            }
            if (performanceResult.getOnuPonTransPower() != null && performanceResult.getOnuPonTransPower() != 0) {
                PerformanceData onuTransPower = new PerformanceData(onuId, OnuPonTransPowerHandle.ONU_PON_TX_POWER,
                        performanceResult);
                pList.add(onuTransPower);
            }
            if (performanceResult.getOltPonRevPower() != null && performanceResult.getOltPonRevPower() != 0) {
                PerformanceData oltRevPower = new PerformanceData(onuId, OltPonLlidRevPowerHandle.OLT_PONLLID_RE_POWER,
                        performanceResult);
                pList.add(oltRevPower);
            }
            redirctPerformanceData(performanceResult, performanceResult, performanceResult.getEntityId(),
                    performanceResult.getOnuIndex());
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        }
    }
}
