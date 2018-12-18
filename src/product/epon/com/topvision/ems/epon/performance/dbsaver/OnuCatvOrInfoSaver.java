/***********************************************************************
 * $Id: OnuCatvOrInfoSaver.java,v1.0 2016-5-9 下午2:35:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoResult;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;
import com.topvision.ems.epon.performance.handle.OnuCatvRfOutVoltageHandle;
import com.topvision.ems.epon.performance.handle.OnuCatvRxPowerHandle;
import com.topvision.ems.epon.performance.handle.OnuCatvTemperatureHandle;
import com.topvision.ems.epon.performance.handle.OnuCatvVoltageHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2016-5-9-下午2:35:01
 *
 */
@Engine("onuCatvOrInfoSaver")
public class OnuCatvOrInfoSaver extends BaseEngine implements PerfEngineSaver<OnuCatvOrInfoResult, OperClass> {
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    /* (non-Javadoc)
     * @see com.topvision.ems.engine.performance.PerfEngineSaver#save(com.topvision.ems.facade.domain.PerformanceResult)
     */
    @Override
    public void save(OnuCatvOrInfoResult performanceResult) {
        OnuPerfDao onuPerfDao = engineDaoFactory.getEngineDao(OnuPerfDao.class);
        if (performanceResult != null) {
            Long onuId = performanceResult.getOnuId();
            try {
                onuPerfDao.insertOnuCatvQuality(performanceResult);
            } catch (Exception e) {
                logger.error("insertOnuCatvQuality Failed", e);
            }
            // 性能阈值数据处理
            List<PerformanceData> pList = new ArrayList<PerformanceData>();
            if (performanceResult.getOnuCatvOrInfoRxPower() != 0) {
                PerformanceData data = new PerformanceData(onuId, OnuCatvRxPowerHandle.ONU_CATV_RXPOWER,
                        performanceResult);
                pList.add(data);
            }
            if (performanceResult.getOnuCatvOrInfoRfOutVoltage() != 0) {
                PerformanceData data = new PerformanceData(onuId, OnuCatvRfOutVoltageHandle.ONU_CATV_OUT_VOLTAGE,
                        performanceResult);
                pList.add(data);
            }
            if (performanceResult.getOnuCatvOrInfoTemperature() != 0) {
                PerformanceData data = new PerformanceData(onuId, OnuCatvTemperatureHandle.ONU_CATV_TEMP,
                        performanceResult);
                pList.add(data);
            }
            if (performanceResult.getOnuCatvOrInfoVoltage() != 0) {
                PerformanceData data = new PerformanceData(onuId, OnuCatvVoltageHandle.ONU_CATV_VOLTAGE,
                        performanceResult);
                pList.add(data);
            }
            getCallback(PerformanceCallback.class).sendPerfomaceResult(pList);
        }

    }

}
