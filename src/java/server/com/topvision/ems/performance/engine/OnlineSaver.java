/***********************************************************************
 * $Id: OnlineSaver.java,v1.0 2014-3-14 上午10:15:10 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.engine;

import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.performance.domain.OnlineResult;
import com.topvision.ems.performance.handle.ConnectivityHandle;
import com.topvision.framework.annotation.Engine;

/**
 * @author Rod John
 * @created @2014-3-14-上午10:15:10
 * 
 */
@Engine("onlineSaver")
public class OnlineSaver extends NbiSupportEngineSaver implements PerfEngineSaver<OnlineResult, OperClass> {
    public static String HANDLE_ID = "ONLINE";

    @Override
    public void save(OnlineResult performanceResult) {
        if (performanceResult != null) {
            Long entityId = performanceResult.getEntityId();
            PerformanceData connectivityData = new PerformanceData(entityId, ConnectivityHandle.HANDLE_ID,
                    performanceResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(connectivityData);
            redirctPerformanceData(performanceResult, performanceResult, entityId, 0L);
        }
    }
}
