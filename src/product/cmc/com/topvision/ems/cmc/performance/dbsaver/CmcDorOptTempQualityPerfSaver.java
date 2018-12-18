package com.topvision.ems.cmc.performance.dbsaver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcDorOptTempQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcDorOptTempQuality;
import com.topvision.ems.cmc.performance.handle.CmcDorOptTempHandle;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

@Engine("cmcDorOptTempQualityPerfSaver")
public class CmcDorOptTempQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcDorOptTempQualityPerfResult, OperClass> {

    private Logger logger = LoggerFactory.getLogger(CmcDorOptTempQualityPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcDorOptTempQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcDorOptTempQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }

        CmcPerfDao perfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        CmcDorOptTempQuality optTempQuality = result.getPerf();
        logger.info("CmcDorOptTempQuality[{}] collect result: {}", cmcId, optTempQuality);

        List<PerformanceData> perfDataList = new ArrayList<PerformanceData>();

        if (optTempQuality.getDorOptNodeTemp() != null) {
            perfDao.insertCmcDorOptTempQuality(result);
            PerformanceData cmcDorOptTempData = new PerformanceData(cmcId, CmcDorOptTempHandle.CC_DOROPTTEMP_FLAG,
                    result.getPerf());
            perfDataList.add(cmcDorOptTempData);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(perfDataList);
        }
    }

}
