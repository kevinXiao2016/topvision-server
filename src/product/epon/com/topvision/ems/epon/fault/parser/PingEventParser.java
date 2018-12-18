/***********************************************************************
 * $Id: PingEventParser.java,v1.0 2012-4-25 上午09:28:43 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.ems.performance.job.PingJob;
import com.topvision.platform.message.event.PingEvent;
import com.topvision.platform.message.event.PingListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author huqiao
 * @created @2012-4-25-上午09:28:43
 * 
 */
@Service("pingEventParser")
public class PingEventParser extends EventParser {
    private ExecutorService pingExecutorService;
    private LinkedBlockingQueue<Event> pingQueue;
    private final int threadNum = 1;
    @Autowired
    private MessageService messageService;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        pingQueue = new LinkedBlockingQueue<Event>();
        pingExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        pingExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("PingEventParser");
                while (true) {
                    try {
                        Event event = pingQueue.take();
                        PingEvent pingEvent = new PingEvent(event);
                        pingEvent.setEntityId(event.getEntityId());
                        pingEvent.setCode(event.getTypeId());
                        pingEvent.setActionName("pingAction");
                        pingEvent.setListener(PingListener.class);
                        messageService.fireMessage(pingEvent);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        pingQueue = null;
        pingExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == PingJob.PING_DISCONNECT_TYPE) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            pingQueue.add(event);
            return true;
        } else if (event.getTypeId() == PingJob.PING_CONNECT_TYPE) {
            event.setClear(true);
            pingQueue.add(event);
            return true;
        }
        return false;
    }
}
