/***********************************************************************
 * $Id: CmcTempQualityPerfSaver.java,v1.0 2013-9-5 上午10:15:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcTempQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.performance.handle.CmcDsModuleTempHandle;
import com.topvision.ems.cmc.performance.handle.CmcOutsideTempHandle;
import com.topvision.ems.cmc.performance.handle.CmcUsModuleTempHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-9-5-上午10:15:47
 * 
 */
@Engine("cmcTempQualityPerfSaver")
public class CmcTempQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcTempQualityPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcTempQualityPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcTempQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcTempQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao perfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();
        List<CmcTempQualityFor8800B> qualityFor8800Bs = result.getCmcTempQualityFor8800Bs();
        if (qualityFor8800Bs != null) {
            //性能阈值数据处理
            for (CmcTempQualityFor8800B tmp : qualityFor8800Bs) {
                //对模块温度超过100摄氏度的值不推送到性能阈值处理中心进行阈值告警处理
                if (tmp.getUsModuleTemp() != null && tmp.getUsModuleTemp() <= 100) {
                    PerformanceData usPerformanceData = new PerformanceData(cmcId,
                            CmcUsModuleTempHandle.CC_US_MODULE_TEMP_FLAG, tmp);
                    perfDataList.add(usPerformanceData);
                } else {
                    logger.error("usModule temp error : " + tmp.getUsModuleTemp());
                    tmp.setUsModuleTemp(null);
                }

                if (tmp.getDsModuleTemp() != null && tmp.getDsModuleTemp() <= 100) {
                    PerformanceData dsPerformanceData = new PerformanceData(cmcId,
                            CmcDsModuleTempHandle.CC_DS_MODULE_TEMP_FLAG, tmp);
                    perfDataList.add(dsPerformanceData);
                } else {
                    logger.error("dsModule temp error : " + tmp.getDsModuleTemp());
                    tmp.setDsModuleTemp(null);
                }

                /*
                PerformanceData insidePerformanceData = new PerformanceData(cmcId,
                        CmcInsideTempHandle.CC_INSIDE_MODULE_TEMP_FLAG, tmp);
                         perfDataList.add(insidePerformanceData);
                */

                if (tmp.getOutsideTemp() != null && tmp.getOutsideTemp() <= 100) {
                    PerformanceData outsidePerformanceData = new PerformanceData(cmcId,
                            CmcOutsideTempHandle.CC_OUTSIDE_MODULE_TEMP_FLAG, tmp);
                    perfDataList.add(outsidePerformanceData);
                } else {
                    logger.error("outsideModule temp error : " + tmp.getOutsideTemp());
                    tmp.setOutsideTemp(null);
                }

                //推送北向接口进行处理
                try {
                    redirctPerformanceData(tmp, result, cmcId, tmp.getCmcIndex());
                } catch (Exception e) {
                    logger.info("CmcTempQualityPerf Saver error", e);
                    logger.info("CmcTempQualityPerf Saver error,cmcTempQuality:" + tmp);
                }
            }
            perfDao.insertCmcTempQuality(qualityFor8800Bs);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
        }
    }
} 
