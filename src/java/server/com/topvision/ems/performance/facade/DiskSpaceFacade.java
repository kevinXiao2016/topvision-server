package com.topvision.ems.performance.facade;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.performance.domain.ServerPerformanceInfo;
import com.topvision.framework.annotation.EngineFacade;

@EngineFacade(serviceName = "DiskSpaceFacade", beanName = "diskSpaceFacade")
public interface DiskSpaceFacade extends Facade{
    
    ServerPerformanceInfo getDiskSpaceInfo();
}
