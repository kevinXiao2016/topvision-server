/***********************************************************************
 * $Id: CmcCmNumberPerfSaver.java,v1.0 2013-8-12 下午05:06:09 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.perf.engine.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CmcCmNumberPerfResult;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2013-8-12-下午05:06:09
 * 
 */
@Engine("cmcCmNumberPerfSaver")
public class CmcCmNumberPerfSaver extends NbiSupportEngineSaver implements
        PerfEngineSaver<CmcCmNumberPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CmcCmNumberPerfSaver.class);
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void save(CmcCmNumberPerfResult result) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcCmNumberPerfSaver identifyKey[" + result.getDomain().getIdentifyKey() + "] exec start.");
        }
        CmcPerfDao cmcPerfDao = engineDaoFactory.getEngineDao(CmcPerfDao.class);
        // Long cmcId = performanceResult.getEntityId();
        cmcPerfDao.insertCmcCmNumberQuality(result.getCmcCmNumbers());
    }
}
