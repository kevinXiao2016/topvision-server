/***********************************************************************
 * $Id: SnmpParser.java,v1.0 2017年5月17日 下午11:00:36 $
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

import org.springframework.stereotype.Service;

import com.topvision.ems.fault.domain.Event;

/**
 * @author vanzand
 * @created @2017年5月17日-下午11:00:36
 *
 */
@Service("snmpEventParser")
public class SnmpEventParser extends EventParser{
    
    public static final int SNMP_TIMEOUT = -101;
    public static final int SNMP_TIMEOUT_CLEAR = -100;
    
    private ExecutorService snmpExecutorService;
    private LinkedBlockingQueue<Event> snmpQueue;
    private final int threadNum = 1;

    @Override
    @PostConstruct
    public void initialize() {
        snmpQueue = new LinkedBlockingQueue<Event>();
        snmpExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        snmpExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("SnmpEventParser");
                while (true) {
                    try {
                        Event event = snmpQueue.take();
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
        snmpQueue = null;
        snmpExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == SNMP_TIMEOUT) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            snmpQueue.add(event);
            return true;
        } else if (event.getTypeId() == SNMP_TIMEOUT_CLEAR) {
            event.setClear(true);
            snmpQueue.add(event);
            return true;
        }
        return false;
    }

}
