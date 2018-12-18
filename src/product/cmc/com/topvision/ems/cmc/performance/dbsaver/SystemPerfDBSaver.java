/***********************************************************************
 * $Id: SystemPerfDBSaver.java,v1.0 2012-7-11 下午01:56:46 $
 *
 * @author: dosion
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.dbsaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.performance.domain.SystemPerfResult;
import com.topvision.ems.cmc.performance.handle.CmcSystemHandle;
import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author dosion
 * @created @2012-7-11-下午01:56:46
 * 
 * @modify by Rod 性能采集优化与重构
 */
@Engine("systemPerfDBSaver")
public class SystemPerfDBSaver extends NbiSupportEngineSaver implements PerfEngineSaver<SystemPerfResult, OperClass> {
    private final Logger logger = LoggerFactory.getLogger(SystemPerfDBSaver.class);

    // 将采集的数据保存到数据库中
    @Override
    public void save(SystemPerfResult sysPerf) {
        if (logger.isDebugEnabled()) {
            logger.debug("SystemPerfDBSaver identifyKey[" + sysPerf.getDomain().getIdentifyKey() + "] exec start.");
        }
        PerformanceData data = new PerformanceData(sysPerf.getEntityId(), CmcSystemHandle.HANDLE_ID, sysPerf);
        getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
    }
}
