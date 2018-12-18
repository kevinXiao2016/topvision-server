/***********************************************************************
 * $Id: PingSaver.java,v1.0 2014-1-7 上午10:04:14 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.engine;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.PingResult;
import com.topvision.ems.performance.handle.PingHandle;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2014-1-7-上午10:04:14
 * 
 */
@Engine("pingSaver")
public class PingSaver extends BaseEngine implements PerfEngineSaver<PingResult, OperClass> {
    @Override
    public void save(PingResult performanceResult) {
        if (performanceResult != null) {
            PerformanceData data = new PerformanceData(performanceResult.getEntityId(), PingHandle.HANDLE_ID,
                    performanceResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
        }
    }
}
