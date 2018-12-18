/***********************************************************************
 * $ PerformanceFacade.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.performance;

import java.util.Map;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.framework.annotation.EngineFacade;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@EngineFacade(serviceName = "PerformanceFacade", beanName = "performanceFacade", category = "Performance")
public interface PerformanceFacade extends Facade {
    /**
     * 管理接口 接收采集消息，采集端都会缓存，采集端重启时能够重新启动性能采集。
     * 
     * @param message
     *            采集消息
     * @return 采集engine的id
     */
    public Integer invoke(ScheduleMessage<OperClass> message);

    /**
     * 每次启动Server时在调用invoke之前先调用这个方法，把engine原来采集线程停止并重新分配
     */
    public void clear();

    void createCollectTimeUtil(String name, Long now, Long l1);

    /**
     * @return 性能统计状态，包括实际性能采集数，线程、缓存等
     */
    Map<String, String> getStatus();
}