package com.topvision.ems.cmc.performance.dbsaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcDorLinePowerQualityPerfResult;
import com.topvision.ems.cmc.performance.facade.CmcDorLinePowerQuality;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.annotation.Engine;

@Engine("cmcDorLinePowerQualityPerfSaver")
public class CmcDorLinePowerQualityPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcDorLinePowerQualityPerfResult, OperClass> {

    private Logger logger = LoggerFactory.getLogger(CmcDorLinePowerQualityPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcDorLinePowerQualityPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcDorLinePowerQualityPerfSaver identifyKey[" + result.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        CmcPerfDao perfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        Long cmcId = result.getEntityId();
        CmcDorLinePowerQuality dorLinePowerQuality = result.getPerf();
        logger.info("CmcDorLinePowerQuality[{}] collect result: {}", cmcId, dorLinePowerQuality);

        if (dorLinePowerQuality.getDorLinePower() != null) {
            perfDao.insertCmcDorLinePowerQuality(result);
        }
    }

}
