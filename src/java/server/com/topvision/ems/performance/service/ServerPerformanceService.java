package com.topvision.ems.performance.service;

import com.topvision.ems.performance.domain.ServerPerformanceInfo;
import com.topvision.framework.service.Service;

public interface ServerPerformanceService extends Service{

    public void addDiskInfo(ServerPerformanceInfo diskInfo);

}
