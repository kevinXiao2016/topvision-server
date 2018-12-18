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

import com.topvision.ems.cmc.performance.domain.CmStatusPerfResult;
import com.topvision.ems.cmc.performance.handle.CmStatusHandle;
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
 * @modify by Rod 性能采集优化与重构
 * 
 */
@Engine("cmStatusDBSaver")
public class CmStatusDBSaver extends NbiSupportEngineSaver implements PerfEngineSaver<CmStatusPerfResult, OperClass> {
    private final Logger logger = LoggerFactory.getLogger(CmStatusDBSaver.class);

    @Override
    public void save(CmStatusPerfResult cmStatusPerfResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("CmStatusDBSaver identifyKey[" + cmStatusPerfResult.getDomain().getIdentifyKey()
                    + "] exec start.");
        }
        PerformanceData data = new PerformanceData(cmStatusPerfResult.getEntityId(), CmStatusHandle.HANDLE_ID,
                cmStatusPerfResult);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
    }
}
