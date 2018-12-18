/***********************************************************************
 * $Id: PingEventParser.java,v1.0 2017年5月25日 上午11:27:55 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.topvision.ems.fault.domain.Event;

/**
 * @author vanzand
 * @created @2017年5月25日-上午11:27:55
 *
 */
public class PingEventParser extends EventParser {

    public static final int ICMP_TIMEOUT = -201;
    public static final int ICMP_TIMEOUT_CLEAR = -200;

    private ExecutorService pingExecutorService;
    private LinkedBlockingQueue<Event> pingQueue;
    private final int threadNum = 1;

    @Override
    @PostConstruct
    public void initialize() {
        pingQueue = new LinkedBlockingQueue<Event>();
        pingExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        pingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("PingEventParser");
                while (true) {
                    try {
                        Event event = pingQueue.take();
                        doEvent(event);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        });
        // 将自身添加到事件处理器队列
        getEventService().registEventParser(this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        pingQueue = null;
        pingExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == ICMP_TIMEOUT) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            pingQueue.add(event);
            return true;
        } else if (event.getTypeId() == ICMP_TIMEOUT_CLEAR) {
            event.setClear(true);
            pingQueue.add(event);
            return true;
        }
        return false;
    }

}
