/***********************************************************************
 * $ DefaultEventParser.java,v1.0 2012-1-12 17:13:42 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.domain.Event;

/**
 * 默认事件解析器 会单独开辟一个处理线程
 * 
 * @author jay
 * @created @2012-1-12-17:13:42
 */
@Service("defaultEventParser")
public class DefaultEventParser extends EventParser implements Runnable {
    private Logger logger = LoggerFactory.getLogger(DefaultEventParser.class);
    private ExecutorService executor;
    private LinkedBlockingQueue<Event> queue;
    private boolean exec = true;

    public void run() {
        Thread.currentThread().setName("DefaultEventParser");
        while (exec) {
            try {
                Event event = queue.take();
                doEvent(event);
            } catch (Throwable e) {
                //按李永成要求将Exception改为Throwable
                logger.error("", e);
            }
        }
    }

    /**
     * 销毁一个EventParser
     */
    @PreDestroy
    public void destroy() {
        exec = false;
        queue.clear();
        executor.shutdown();
        queue = null;
        executor = null;
        getEventService().unRegistEventParser(this);
    }

    /**
     * 初始化一个EventParser
     */
    @PostConstruct
    public void initialize() {
        setCos(Integer.MAX_VALUE);
        exec = true;
        queue = new LinkedBlockingQueue<Event>();
        executor = Executors.newFixedThreadPool(1);
        executor.execute(this);
        getEventService().registEventParser(this);
    }

    /**
     * 处理一个Event
     * 
     * @param event
     * @return 是否能够处理该Event 能处理返回true 不能处理返回false
     */

    public boolean parse(Event event) {
        if ((event.isClear() != null && event.isClear()) || isAlertClearByEvent(event)) {
            event.setClear(true);
        } else {
            event.setClear(false);
        }
        queue.add(event);
        return true;
    }
}
