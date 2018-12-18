/***********************************************************************
 * $Id: PerfEngineSaver.java,v1.0 2015年6月10日 上午11:26:41 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.performance;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * 把原来com.topvision.ems.facade.domain.PerfService<T, V>改为engine接口
 * 主要实现性能数据的采集
 * 
 * @author Victor
 * @created @2015年6月10日-上午11:26:41
 *
 */
public interface PerfEngineSaver<T extends PerformanceResult<V>, V extends OperClass> {
    public void save(T performanceResult);

    //Modify by Victor@20150610检查所有实现都没有用这个方法，去掉
    //public void setUp(ScheduleMessage<V> scheduleMessage);
}
