/***********************************************************************
 * $Id: PerformanceStatisticsImpl.java,v1.0 2013-6-13 上午10:35:39 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.performance.domain.PerfDelayItem;
import com.topvision.ems.performance.handle.PerformanceHandle;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.service.ThreadPoolService;

/**
 * @author Rod John
 * @created @2013-6-13-上午10:35:39
 * 
 * 
 * 
 */
@Service("performanceStatistics")
public class PerformanceStatisticsImpl extends BaseService implements PerformanceStatistics {
    @Autowired
    private ThreadPoolService threadPoolService;
    private LinkedBlockingQueue<PerformanceData> performanceQueue;
    private LinkedBlockingQueue<PerformanceData> realtimePerformanceQueue;
    // 阈值事件延时队列，用于处理时间段次数
    protected DelayQueue<PerfDelayItem> perfEventQueue;
    private Map<String, PerformanceHandle> performanceHandle;

    @Override
    @PreDestroy
    public void destroy() {
    }

    @Override
    @PostConstruct
    public void initialize() {
        performanceHandle = new HashMap<String, PerformanceHandle>();
        performanceQueue = new LinkedBlockingQueue<PerformanceData>();
        realtimePerformanceQueue = new LinkedBlockingQueue<PerformanceData>();

        perfEventQueue = new DelayQueue<PerfDelayItem>();
        Thread center = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("PerformanceStatisticsCenter");
                while (performanceQueue != null) {
                    try {
                        final PerformanceData result = performanceQueue.take();
                        String handleId = result.getHandleId();
                        final PerformanceHandle handle = performanceHandle.get(handleId);
                        if (handle != null) {
                            /*
                             * List<Event> events = handle.handle(result); if (events != null &&
                             * events.size() > 0) { EventSender.getInstance().send(events); }
                             */
                            Runnable r = new Runnable() {
                                public void run() {
                                    try {
                                        handle.handle(result);
                                    } catch (Exception e) {
                                        logger.error("PerformanceStatisticsCenter", e);
                                    }
                                }
                            };
                            while (true) {
                                try {
                                    threadPoolService.execute(r);
                                    break;
                                } catch (RejectedExecutionException e) {
                                    Thread.sleep(1000);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                    } catch (Exception e) {
                        logger.error("performanceQueue error {}", e);
                    }
                }
            }
        });
        center.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("RealtimePerformanceStatisticsCenter");
                while (realtimePerformanceQueue != null) {
                    try {
                        final PerformanceData result = realtimePerformanceQueue.take();
                        String handleId = result.getHandleId();
                        final PerformanceHandle handle = performanceHandle.get(handleId);
                        if (handle != null) {
                            /*
                             * List<Event> events = handle.handle(result); if (events != null &&
                             * events.size() > 0) { EventSender.getInstance().send(events); }
                             */
                            Runnable r = new Runnable() {
                                public void run() {
                                    try {
                                        handle.handle(result);
                                    } catch (Exception e) {
                                        logger.error("RealtimePerformanceStatisticsCenter", e);
                                    }
                                }
                            };
                            while (true) {
                                try {
                                    threadPoolService.execute(r);
                                    break;
                                } catch (RejectedExecutionException e) {
                                    Thread.sleep(1000);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                    } catch (Exception e) {
                        logger.error("performanceQueue error {}", e);
                    }
                }
            }
        }).start();

        Thread perfThread = new Thread() {
            @Override
            public void run() {
                setName("PerfThread");
                while (true) {
                    try {
                        // take从延时队列取出元素,不需要进行处理
                        @SuppressWarnings("unused")
                        PerfDelayItem item = perfEventQueue.take();
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                }
            }
        };
        perfThread.start();
    }

    /**
     * 事件触发点
     * 
     * @param event
     * @param checkNum
     * @param timeDelay
     *            单位是秒
     */
    public void checkPoint(Event event, int checkNum, int timeDelay) {
        // modify by lzt
        // 此处减去10s为了避免延时队列的处理时间延时问题 EMS-10574
        PerfDelayItem delayItem = new PerfDelayItem(timeDelay * 60 * 1000 - 10000, event);
        perfEventQueue.put(delayItem);
        while (perfEventQueue.poll() != null) {
            perfEventQueue.poll();
        }
        Iterator<PerfDelayItem> perfIterator = perfEventQueue.iterator();
        List<Event> items = new ArrayList<Event>();
        while (perfIterator.hasNext()) {
            Event tmp = perfIterator.next().getEvent();
            if (tmp.getEntityId().equals(event.getEntityId()) && tmp.getTypeId().equals(event.getTypeId())
                    && tmp.getSource().equals(event.getSource())) {
                items.add(tmp);
            }
        }
        if (items.size() >= checkNum) {
            EventSender.getInstance().send(event);
        }
    }

    /**
     * 清除事件发送
     * 
     * @param event
     */
    public void sendClearEvent(Event event) {
        EventSender.getInstance().send(event);
    }

    @Override
    public void registerPerformanceHandler(String handleFlag, PerformanceHandle handle) {
        if (!performanceHandle.containsKey(handleFlag)) {
            performanceHandle.put(handleFlag, handle);
        }
    }

    @Override
    public void unregisterPerformanceHandler(String handleFlag) {
        if (performanceHandle.containsKey(handleFlag)) {
            performanceHandle.remove(handleFlag);
        }
    }

    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendPerfomaceResult(PerformanceData result) {
        if (logger.isDebugEnabled()) {
            logger.debug("PerformanceResult:" + result);
        }
        try {
            performanceQueue.put(result);
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("send result error." + e.getMessage(), e);
            }
        }
    }

    /**
     * sendPerfomaceResult
     * 
     * @param result
     */
    public void sendPerfomaceResult(List<PerformanceData> result) {
        for (PerformanceData data : result) {
            sendPerfomaceResult(data);
        }
    }

    @Override
    public void sendRealtimePerfomaceResult(PerformanceData result) {
        if (logger.isDebugEnabled()) {
            logger.debug("PerformanceResult:" + result);
        }
        try {
            realtimePerformanceQueue.put(result);
        } catch (InterruptedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("send result error." + e.getMessage(), e);
            }
        }
    }

    @Override
    public void sendRealtimePerfomaceResult(List<PerformanceData> result) {
        if (logger.isDebugEnabled()) {
            logger.debug("PerformanceResult:" + result);
        }
        for (PerformanceData data : result) {
            sendRealtimePerfomaceResult(data);
        }
    }

    @Override
    public LinkedBlockingQueue<PerformanceData> getPerformanceQueue() {
        return performanceQueue;
    }

    @Override
    public LinkedBlockingQueue<PerformanceData> getRealtimePerformanceQueue() {
        return realtimePerformanceQueue;
    }

    @Override
    public Map<String, PerformanceHandle> getPerformanceHandle() {
        return performanceHandle;
    }
}
