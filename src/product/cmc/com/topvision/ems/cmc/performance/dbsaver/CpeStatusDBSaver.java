/***********************************************************************
 * $Id: CmStatusDBSaver.java,v1.0 2012-7-17 上午10:33:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.performance.domain.CpeStatusPerfResult;
import com.topvision.ems.cmc.performance.handle.CpeStatusHandle;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author loyal
 * @created @2012-7-17-上午10:33:10
 * 
 */
@Engine("cpeStatusPerfDBSaver")
public class CpeStatusDBSaver extends NbiSupportEngineSaver implements PerfEngineSaver<CpeStatusPerfResult, OperClass> {
    private Logger logger = LoggerFactory.getLogger(CpeStatusDBSaver.class);

    @Override
    public void save(CpeStatusPerfResult cpeStatusPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("CpeStatusDBSaver identifyKey[" + cpeStatusPerfResult.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        PerformanceData data = new PerformanceData(cpeStatusPerfResult.getEntityId(), CpeStatusHandle.HANDLE_ID,
                cpeStatusPerfResult);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
    }
}