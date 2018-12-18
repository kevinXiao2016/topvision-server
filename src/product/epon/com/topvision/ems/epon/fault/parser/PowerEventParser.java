/***********************************************************************
 * $Id: PowerRemoveEventParser.java,v1.0 2012-4-23 下午02:10:17 $
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

import com.topvision.ems.epon.fault.EponCode;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.ems.fault.parser.EventParser;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.message.service.MessageService;

/**
 * @author huqiao
 * @created @2012-4-23-下午02:10:17
 * 
 */
@Service("powerEventParser")
public class PowerEventParser extends EventParser {
    private ExecutorService powerExecutorService;
    private LinkedBlockingQueue<Event> powerQueue;
    private final int threadNum = 1;
    @Autowired
    private MessageService messageService;

    // 设置优先级
    // private final int cos = 200;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        // 初始化线程队列
        powerQueue = new LinkedBlockingQueue<Event>();
        // 初始化处理线程
        powerExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        powerExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("PowerRemoveEventParser");
                while (true) {
                    try {
                        Event event = powerQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSourceRaw());
                            evt.setActionName("onTrapMessage");
                            evt.setListener(TrapListener.class);
                            messageService.addMessage(evt);
                        }
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
        powerQueue = null;
        powerExecutorService.shutdown();
        getEventService().unRegistEventParser(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.parser.EventParser#parse(com.topvision.ems.fault.domain.Event)
     */
    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.BD_PWR_FAIL || event.getTypeId() == EponCode.BD_PWR_REMOVE) {
            // 表示该告警可以处理，返回true代表后续的eventParser不需处理
            powerQueue.add(event);
            return true;
        } else if (event.getTypeId() == EponCode.BD_PWR_OK || event.getTypeId() == EponCode.BD_PWR_INSERT) {
            event.setClear(true);
            powerQueue.add(event);
            return true;
        }
        return false;
    }

}
