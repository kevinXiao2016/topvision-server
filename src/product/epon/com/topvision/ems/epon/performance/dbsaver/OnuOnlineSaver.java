/***********************************************************************
 * $Id: OnuOnlineSaver.java,v1.0 2015-4-22 下午4:33:03 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dbsaver;

import com.topvision.ems.engine.nbi.NbiSupportEngineSaver;
import com.topvision.ems.engine.performance.PerfEngineSaver;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.epon.performance.handle.OnuOnlineHandle;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.framework.annotation.Engine;

/**
 * @author flack
 * @created @2015-4-22-下午4:33:03
 *
 */
@Engine("onuOnlineSaver")
public class OnuOnlineSaver extends NbiSupportEngineSaver implements PerfEngineSaver<OnuOnlineResult, OperClass> {
    @Override
    public void save(OnuOnlineResult performanceResult) {
        if (performanceResult != null) {
            PerformanceData data = new PerformanceData(performanceResult.getEntityId(), OnuOnlineHandle.HANDLE_ID,
                    performanceResult);
            getCallback(PerformanceCallback.class).sendPerfomaceResult(data);
        }
    }
}
