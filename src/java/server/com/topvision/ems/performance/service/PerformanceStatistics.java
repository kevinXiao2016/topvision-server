/***********************************************************************
 * $Id: PerformanceStatistics.java,v1.0 2013-6-13 上午11:05:57 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.framework.service.Service;

/**
 * @author Rod John
 * @created @2013-6-13-上午11:05:57
 *
 */
public interface PerformanceStatistics extends Service {
    public void sendPerfomaceResult(PerformanceData result);

    public void sendPerfomaceResult(List<PerformanceData> result);

    public void sendRealtimePerfomaceResult(PerformanceData result);

    public void sendRealtimePerfomaceResult(List<PerformanceData> result);

    /**
     * 注册性能解析器
     * 
     * @param handleFlag
     * @param handle
     */
    void registerPerformanceHandler(String handleFlag, PerformanceHandle handle);

    /**
     * 解注册性能解析器
     * 
     * @param handleFlag
     */
    void unregisterPerformanceHandler(String handleFlag);

    public void checkPoint(Event event, int checkNum, int timeDelay);

    public void sendClearEvent(Event event);

    public LinkedBlockingQueue<PerformanceData> getPerformanceQueue();

    public LinkedBlockingQueue<PerformanceData> getRealtimePerformanceQueue();

    public Map<String, PerformanceHandle> getPerformanceHandle();
}
