/***********************************************************************
 * $Id: OnuDeleteEventParser.java,v1.0 2017年1月13日 下午1:51:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

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
 * @author lizongtian
 * @created @2017年1月13日-下午1:51:28
 *
 */
@Service("onuDeleteEventParser")
public class OnuDeleteEventParser extends EventParser {
    private ExecutorService onuDeleteExecutorService;
    private LinkedBlockingQueue<Event> onuDeleteQueue;
    private final int threadNum = 10;
    @Autowired
    private MessageService messageService;

    @Override
    public void initialize() {
        // 初始化线程队列
        onuDeleteQueue = new LinkedBlockingQueue<Event>();
        onuDeleteExecutorService = java.util.concurrent.Executors.newFixedThreadPool(threadNum);
        onuDeleteExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("OnuDeleteEventParser");
                while (true) {
                    try {
                        Event event = onuDeleteQueue.take();
                        if (event.getUserObject() instanceof Trap) {
                            TrapEvent evt = new TrapEvent(event.getUserObject());
                            evt.setTrap((Trap) event.getUserObject());
                            evt.setEntityId(event.getEntityId());
                            evt.setCode(event.getTypeId());
                            evt.setSource(event.getSourceRaw());
                            evt.setDeviceIndex(event.getDeviceIndex());
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

    @Override
    public void destroy() {
        onuDeleteQueue = null;
        getEventService().unRegistEventParser(this);
        onuDeleteExecutorService.shutdown();
    }

    @Override
    public boolean parse(Event event) {
        if (event.getTypeId() == EponCode.ONU_DELETE) {
            onuDeleteQueue.add(event);
            return true;
        }
        return false;
    }

}
